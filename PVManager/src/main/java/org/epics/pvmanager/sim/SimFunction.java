/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager.sim;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.epics.pvmanager.Collector;
import org.epics.pvmanager.TimeStamp;
import org.epics.pvmanager.ValueCache;
import org.epics.pvmanager.data.AlarmSeverity;
import org.epics.pvmanager.data.VDouble;
import org.epics.pvmanager.data.ValueFactory;

/**
 * Base class for all simulated signals. It provides the common mechanism for
 * registering the update on a timer and a few other utilities.
 *
 * @author carcassi
 */
public abstract class SimFunction<T> {

    private static final Logger log = Logger.getLogger(SimFunction.class.getName());

    private double secondsBeetwenSamples;
    private Class<T> classToken;

    /**
     * Creates a new simulation function.
     *
     * @param secondsBeetwenSamples seconds between each samples
     */
    public SimFunction(double secondsBeetwenSamples, Class<T> classToken) {
        if (secondsBeetwenSamples <= 0.0) {
            throw new IllegalArgumentException("Interval must be greater than zero (was " + secondsBeetwenSamples + ")");
        }
        this.secondsBeetwenSamples = secondsBeetwenSamples;
        this.classToken = classToken;
    }

    /**
     * Calculates and returns the next value.
     *
     * @return the next value
     */
    protected abstract T nextValue();

    private TimerTask task;

    /**
     * Starts notifications of new values using the timer thread.
     *
     * @param timer timer on which to execute the updates
     * @param collector collector notified of updates
     * @param cache cache to put the new value in
     */
    public void start(Timer timer, final Collector collector, final ValueCache<T> cache) {
        if (!cache.getType().equals(classToken)) {
            throw new IllegalArgumentException("Function is of type " + classToken.getSimpleName() + " (requested " + cache.getType().getSimpleName() + ")");
        }

        // The timer only accepts interval up to the millisecond.
        // For intervals shorter than that, we calculate the extra samples
        // we need to generate within each time execution.
        long intervalBetweenExecution = (long) (secondsBeetwenSamples * 1000);
        if (intervalBetweenExecution == 0)
            intervalBetweenExecution = 1;
        final int samplesPerExecution = (int) ((double) intervalBetweenExecution / (secondsBeetwenSamples * 1000.0));

        if (task != null)
            task.cancel();
        task = new TimerTask() {
            int innerCounter;
            SimulationDataSource.ValueProcessor<Object, T> processor = new SimulationDataSource.ValueProcessor<Object, T>(collector, cache) {

                @Override
                public void close() {
                    log.fine("Closing " + this);
                    cancel();
                }

                @Override
                public boolean updateCache(Object payload, ValueCache<T> cache) {
                    cache.setValue(nextValue());
                    return true;
                }
            };

            @Override
            public void run() {
                // Protect the timer thread for possible problems.
                try {
                    for (int i = 0; i < samplesPerExecution; i++) {
                        processor.processValue(null);
                    }
                } catch (Exception ex) {
                    log.log(Level.WARNING, "Data simulation problem", ex);
                }
            }
        };
        timer.scheduleAtFixedRate(task, 0, intervalBetweenExecution);
        log.log(Level.FINE, "Synch starting {0}", task);
    }

    /**
     * Stops the variable from further notifications.
     */
    public void stop() {
        if (task != null) {
            task.cancel();
            log.log(Level.FINE, "Synch closing {0}", task);
        }
        task = null;
    }

    /**
     * Creating new value based on the metadata from the old value.
     *
     * @param value new numeric value
     * @param oldValue old VDouble
     * @return new VDouble
     */
    protected static VDouble newValue(double value, VDouble oldValue) {
        // Calculate new AlarmSeverity, using oldValue ranges
        AlarmSeverity severity = AlarmSeverity.NONE;
        if (value <= oldValue.getLowerAlarmLimit() || value >= oldValue.getUpperAlarmLimit())
            severity = AlarmSeverity.MAJOR;
        else if(value <= oldValue.getLowerWarningLimit() || value >= oldValue.getUpperWarningLimit())
            severity = AlarmSeverity.MINOR;

        return ValueFactory.newVDouble(value, severity, Constants.NO_ALARMS,
                null, TimeStamp.now(), oldValue);
    }

}
