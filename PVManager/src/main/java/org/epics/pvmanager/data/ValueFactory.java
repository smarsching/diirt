/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.data;

import java.text.NumberFormat;
import java.util.Collections;
import java.util.List;
import org.epics.util.array.ListDouble;
import org.epics.util.array.ListInt;
import org.epics.util.time.Timestamp;

/**
 * Factory class for all concrete implementation of the types.
 * <p>
 * The factory methods do not do anything in terms of defensive copy and
 * immutability to the objects, which they are passed as they are. It's the
 * client responsibility to prepare them appropriately, which is automatically
 * done anyway for all objects except collections.
 *
 * @author carcassi
 */
public class ValueFactory {
    
    /**
     * Creates a new VString.
     * 
     * @param value the string value
     * @param alarm the alarm
     * @param time the time
     * @return the new value
     */
    public static VString newVString(final String value, final Alarm alarm, final Time time) {
        return new IVString(value, alarm.getAlarmSeverity(), alarm.getAlarmStatus(),
                time.getTimestamp(), time.getTimeUserTag(), time.isTimeValid());
    }

    
    /**
     * Creates a new VMultiDouble.
     * 
     * @param values the values
     * @param alarm the alarm
     * @param time the time
     * @param display the display
     * @return the new value
     */
    public static VMultiDouble newVMultiDouble(List<VDouble> values, final Alarm alarm, final Time time, final Display display) {
        return new IVMultiDouble(values, alarm.getAlarmSeverity(), alarm.getAlarmStatus(),
                time.getTimestamp(), time.getTimeUserTag(), time.isTimeValid(),
                display.getLowerDisplayLimit(), display.getLowerCtrlLimit(), display.getLowerAlarmLimit(), display.getLowerWarningLimit(),
                display.getUnits(), display.getFormat(),
                display.getUpperWarningLimit(), display.getUpperAlarmLimit(), display.getUpperCtrlLimit(), display.getUpperDisplayLimit());
    }


    /**
     * Creates a new VInt.
     * 
     * @param value the value
     * @param alarm the alarm
     * @param time the time
     * @param display the display
     * @return the new value
     */
    public static VInt newVInt(final Integer value, final Alarm alarm, final Time time, final Display display) {
        return new IVInt(value, alarm.getAlarmSeverity(), alarm.getAlarmStatus(),
                time.getTimestamp(), time.getTimeUserTag(), time.isTimeValid(),
                display.getLowerDisplayLimit(), display.getLowerCtrlLimit(), display.getLowerAlarmLimit(), display.getLowerWarningLimit(),
                display.getUnits(), display.getFormat(),
                display.getUpperWarningLimit(), display.getUpperAlarmLimit(), display.getUpperCtrlLimit(), display.getUpperDisplayLimit());
    }
    
    /**
     * New alarm with the given severity and status.
     * 
     * @param alarmSeverity the alarm severity
     * @param alarmStatus the alarm status
     * @return the new alarm
     */
    public static Alarm newAlarm(final AlarmSeverity alarmSeverity, final AlarmStatus alarmStatus) {
        return new Alarm() {

            @Override
            public AlarmSeverity getAlarmSeverity() {
                return alarmSeverity;
            }

            @Override
            public AlarmStatus getAlarmStatus() {
                return alarmStatus;
            }
        };
    }
    
    private static final Alarm alarmNone = newAlarm(AlarmSeverity.NONE, AlarmStatus.NONE);
    
    /**
     * No alarm.
     * 
     * @return severity and status NONE
     */
    public static Alarm alarmNone() {
        return alarmNone;
    }
    
    /**
     * Alarm based on the value and the display ranges.
     * 
     * @param value the value
     * @param display the display information
     * @return the new alarm
     */
    public static Alarm newAlarm(Number value, Display display) {
        // Calculate new AlarmSeverity, using display ranges
        AlarmSeverity severity = AlarmSeverity.NONE;
        AlarmStatus status = AlarmStatus.NONE;
        if (value.doubleValue() <= display.getLowerAlarmLimit() || value.doubleValue() >= display.getUpperAlarmLimit()) {
            status = AlarmStatus.RECORD;
            severity = AlarmSeverity.MAJOR;
        } else if (value.doubleValue() <= display.getLowerWarningLimit() || value.doubleValue() >= display.getUpperWarningLimit()) {
            status = AlarmStatus.RECORD;
            severity = AlarmSeverity.MINOR;
        }
        
        return newAlarm(severity, status);
    }
    
    /**
     * Creates a new time.
     * 
     * @param timestamp the timestamp
     * @param timeUserTag the user tag
     * @param timeValid whether the time is valid
     * @return the new time
     */
    public static Time newTime(final Timestamp timestamp, final Integer timeUserTag, final boolean timeValid) {
        return new Time() {

            @Override
            public Timestamp getTimestamp() {
                return timestamp;
            }

            @Override
            public Integer getTimeUserTag() {
                return timeUserTag;
            }

            @Override
            public boolean isTimeValid() {
                return timeValid;
            }
        };
    }
    
    /**
     * New time, with no user tag and valid data.
     * 
     * @param timestamp the timestamp
     * @return the new time
     */
    public static Time newTime(final Timestamp timestamp) {
        return newTime(timestamp, null, true);
    }
    
    /**
     * New time with the current timestamp, no user tag and valid data.
     * 
     * @return the new time
     */
    public static Time timeNow() {
        return newTime(Timestamp.now(), null, true);
    }
    
    /**
     * Creates a new display
     * 
     * @param lowerDisplayLimit lower display limit
     * @param lowerAlarmLimit lower alarm limit
     * @param lowerWarningLimit lower warning limit
     * @param units the units
     * @param numberFormat the formatter
     * @param upperWarningLimit the upper warning limit
     * @param upperAlarmLimit the upper alarm limit
     * @param upperDisplayLimit the upper display limit
     * @param lowerCtrlLimit the lower control limit
     * @param upperCtrlLimit the upper control limit
     * @return the new display
     */
    public static Display newDisplay(final Double lowerDisplayLimit, final Double lowerAlarmLimit, final Double lowerWarningLimit,
            final String units, final NumberFormat numberFormat, final Double upperWarningLimit,
            final Double upperAlarmLimit, final Double upperDisplayLimit,
            final Double lowerCtrlLimit, final Double upperCtrlLimit) {
        return new Display() {
            @Override
            public Double getLowerCtrlLimit() {
                return lowerCtrlLimit;
            }

            @Override
            public Double getUpperCtrlLimit() {
                return upperCtrlLimit;
            }

            @Override
            public Double getLowerDisplayLimit() {
                return lowerDisplayLimit;
            }

            @Override
            public Double getLowerAlarmLimit() {
                return lowerAlarmLimit;
            }

            @Override
            public Double getLowerWarningLimit() {
                return lowerWarningLimit;
            }

            @Override
            public String getUnits() {
                return units;
            }

            @Override
            public NumberFormat getFormat() {
                return numberFormat;
            }

            @Override
            public Double getUpperWarningLimit() {
                return upperWarningLimit;
            }

            @Override
            public Double getUpperAlarmLimit() {
                return upperAlarmLimit;
            }

            @Override
            public Double getUpperDisplayLimit() {
                return upperDisplayLimit;
            }

        };
    }
    
    private static final Display displayNone = newDisplay(null, null, null, null, null, null, null, null, null, null);
    
    /**
     * Empty display information.
     * 
     * @return no display
     */
    public static Display displayNone() {
        return displayNone;
    }
    
    
    /**
     * Creates a new VDouble.
     * 
     * @param value the value
     * @param alarm the alarm
     * @param time the time
     * @param display the display
     * @return the new value
     */
    public static VDouble newVDouble(final Double value, final Alarm alarm, final Time time, final Display display) {
        return new IVDouble(value, alarm.getAlarmSeverity(), alarm.getAlarmStatus(),
                time.getTimestamp(), time.getTimeUserTag(), time.isTimeValid(),
                display.getLowerDisplayLimit(), display.getLowerCtrlLimit(), display.getLowerAlarmLimit(), display.getLowerWarningLimit(),
                display.getUnits(), display.getFormat(),
                display.getUpperWarningLimit(), display.getUpperAlarmLimit(), display.getUpperCtrlLimit(), display.getUpperDisplayLimit());
    }

    /**
     * Creates a new VDouble using the given value, time, display and
     * generating the alarm from the value and display information.
     * 
     * @param value the new value
     * @param time the time
     * @param display the display information
     * @return the new value
     */
    public static VDouble newVDouble(Double value, Time time, Display display) {
        return newVDouble(value, newAlarm(value, display), time, display);
    }
    
    /**
     * Creates new immutable VDouble by using metadata from the old value,
     * now as timestamp and computing alarm from the metadata range.
     * 
     * @param value new numeric value
     * @param display metadata
     * @return new value
     */
    public static VDouble newVDouble(Double value, Display display) {
        return newVDouble(value, timeNow(), display);
    }
    
    /**
     * Creates a new VDouble, no alarm, time now, no display.
     * 
     * @param value the value
     * @return the new value
     */
    public static VDouble newVDouble(Double value) {
        return newVDouble(value, alarmNone(), timeNow(), displayNone());
    }
    
    /**
     * Creates a new VDouble, no alarm, no display.
     * 
     * @param value the value
     * @param time the time
     * @return the new value
     */
    public static VDouble newVDouble(Double value, Time time) {
        return newVDouble(value, alarmNone(), time, displayNone());
    }

    /**
     * Creates a new VStatistics.
     * 
     * @param average average
     * @param stdDev standard deviation
     * @param min minimum
     * @param max maximum
     * @param nSamples number of samples
     * @param alarm the alarm
     * @param time the time
     * @param display the display
     * @return the new value 
     */
    public static VStatistics newVStatistics(final double average, final double stdDev,
            final double min, final double max, final int nSamples, final Alarm alarm,
            final Time time, final Display display) {
        return new IVStatistics(average, stdDev, min, max, nSamples,
                alarm.getAlarmSeverity(), alarm.getAlarmStatus(),
                time.getTimestamp(), time.getTimeUserTag(), time.isTimeValid(),
                display.getLowerDisplayLimit(), display.getLowerCtrlLimit(), display.getLowerAlarmLimit(), display.getLowerWarningLimit(),
                display.getUnits(), display.getFormat(),
                display.getUpperWarningLimit(), display.getUpperAlarmLimit(), display.getUpperCtrlLimit(), display.getUpperDisplayLimit());
    }
    
    /**
     * Creates a new VDoubleArray.
     * 
     * @param values array values
     * @param sizes sizes
     * @param alarm the alarm
     * @param time the time
     * @param display the display
     * @return the new value
     */
    public static VDoubleArray newVDoubleArray(final double[] values, final List<Integer> sizes, Alarm alarm, Time time, Display display) {
        return new IVDoubleArray(values, sizes, alarm.getAlarmSeverity(), alarm.getAlarmStatus(),
                time.getTimestamp(), time.getTimeUserTag(), time.isTimeValid(),
                display.getLowerDisplayLimit(), display.getLowerCtrlLimit(), display.getLowerAlarmLimit(), display.getLowerWarningLimit(),
                display.getUnits(), display.getFormat(),
                display.getUpperWarningLimit(), display.getUpperAlarmLimit(), display.getUpperCtrlLimit(), display.getUpperDisplayLimit());
    }
    
    /**
     * Creates a new VDoubleArray.
     * 
     * @param values array values
     * @param alarm the alarm
     * @param time the time
     * @param display the display
     * @return the new value
     */
    public static VDoubleArray newVDoubleArray(final double[] values, Alarm alarm, Time time, Display display) {
        return newVDoubleArray(values, Collections.singletonList(values.length), alarm, time, display);
    }
    
    /**
     * Creates a new VDoubleArray.
     * 
     * @param data array data
     * @param alarm the alarm
     * @param time the time
     * @param display the display
     * @return the new value
     */
    public static VDoubleArray newVDoubleArray(ListDouble data, Alarm alarm, Time time, Display display) {
        return new IVDoubleArray(data, Collections.singletonList(data.size()), alarm,
                time, display);
    }
    
    /**
     * Creates a new VDoubleArray.
     * 
     * @param values array values
     * @param display the display
     * @return the new value
     */
    public static VDoubleArray newVDoubleArray(final double[] values, Display display) {
        return newVDoubleArray(values, Collections.singletonList(values.length), alarmNone(), timeNow(), display);
    }


    public static VImage newVImage(int height, int width, byte[] data) {
        return new IVImage(height, width, data);
    }
    
    public static VIntArray newVIntArray(final int[] values, final List<Integer> sizes, Alarm alarm, Time time, Display display) {
        return new IVIntArray(values, sizes, alarm.getAlarmSeverity(), alarm.getAlarmStatus(),
                time.getTimestamp(), time.getTimeUserTag(), time.isTimeValid(),
                display.getLowerDisplayLimit(), display.getLowerCtrlLimit(), display.getLowerAlarmLimit(), display.getLowerWarningLimit(),
                display.getUnits(), display.getFormat(),
                display.getUpperWarningLimit(), display.getUpperAlarmLimit(), display.getUpperCtrlLimit(), display.getUpperDisplayLimit());
    }
    
    public static VIntArray newVIntArray(final int[] values, Alarm alarm, Time time, Display display) {
        return newVIntArray(values, Collections.singletonList(values.length), alarm, time, display);
    }
    
    public static VIntArray newVIntArray(final ListInt values, Alarm alarm, Time time, Display display) {
        return new IVIntArray(values, Collections.singletonList(values.size()), alarm,
                time, display);
    }
    
    public static VIntArray newVIntArray(final int[] values, Display display) {
        return newVIntArray(values, Collections.singletonList(values.length), alarmNone(), timeNow(), display);
    }

}
