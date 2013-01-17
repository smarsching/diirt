/**
 * Copyright (C) 2012 University of Michigan
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene.rrdtool;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import org.epics.graphene.Bubble2DGraphRenderer;
import org.epics.graphene.Point3DWithLabelDataset;
import org.epics.graphene.Point3DWithLabelDatasets;
import org.epics.graphene.Scatter2DGraphRenderer;
import org.epics.util.array.ListDouble;
import org.epics.util.time.TimeDuration;
import org.epics.util.time.Timestamp;
import org.epics.util.time.TimestampFormat;

/**
 *
 * @author carcassi
 */
public class RrdToolBubbleGraphMain {
    
    private static TimestampFormat format = new TimestampFormat("yyyyMMddHHmmss");
    
    public static void main(String[] args) throws Exception {
        List<String> signals = new ArrayList<>();
        Timestamp start = null;
        Timestamp end = null;
        String filename = null;
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.equals("-s")) {
                i++;
                start = format.parse(args[i]);
            } else if (arg.equals("-e")) {
                i++;
                end = format.parse(args[i]);
            } else if (arg.equals("-o")) {
                i++;
                filename = args[i];
            } else  {
                signals.add(arg);
            }
        }
        List<TimeSeries> data = RrdToolDB.fetchData(signals, start, end);
        Map<String, TimeSeries> series = new HashMap<>();
        for (int i = 0; i < signals.size(); i++) {
            series.put(signals.get(i), data.get(i));
        }
        TimeSeriesMulti correlated = TimeSeriesMulti.synchronizeSeries(series);
        final Point3DWithLabelDataset dataset = Point3DWithLabelDatasets.build(correlated.getValues().get(signals.get(0)), 
                                                correlated.getValues().get(signals.get(1)), 
                                                correlated.getValues().get(signals.get(2)), 
                                                Collections.nCopies(correlated.getValues().get(signals.get(0)).size(), "label"));
        BubbleUtil.createBubblePlot(filename, dataset);
    }

}
