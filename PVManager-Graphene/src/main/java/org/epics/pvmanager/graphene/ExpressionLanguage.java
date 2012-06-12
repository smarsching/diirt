/*
 * Copyright 2010-11 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager.graphene;

import org.epics.pvmanager.data.*;
import java.util.List;
import org.epics.pvmanager.BasicTypeSupport;
import org.epics.pvmanager.expression.DesiredRateExpression;
import org.epics.pvmanager.expression.DesiredRateExpressionList;
import org.epics.pvmanager.expression.SourceRateExpression;
import org.epics.pvmanager.expression.SourceRateExpressionList;
import static org.epics.pvmanager.ExpressionLanguage.*;
import org.epics.pvmanager.expression.DesiredRateExpressionImpl;
import org.epics.pvmanager.expression.DesiredRateExpressionListImpl;

/**
 *
 * @author carcassi
 */
public class ExpressionLanguage {
    private ExpressionLanguage() {}

    static {
        // Add support for Epics types.
        DataTypeSupport.install();
        // Add support for Basic types
        BasicTypeSupport.install();
    }

    public static Histogram1DPlot histogramOf(SourceRateExpression<? extends VNumber> vDoubles) {
        DesiredRateExpression<? extends List<? extends VNumber>> queue = newValuesOf(vDoubles);
        return new Histogram1DPlot(queue, new Histogram1DFunction(queue.getFunction()), "histogram");
    }

    public static LineGraphPlot lineGraphOf(DesiredRateExpression<? extends VNumberArray> vDoubleArray) {
        return new LineGraphPlot(vDoubleArray, new LineGraphFunction(vDoubleArray.getFunction()), "lineGraph");
    }

    public static LineGraphPlot lineGraphOf(DesiredRateExpression<? extends VNumberArray> yArray,
            DesiredRateExpression<? extends VNumber> xInitialOffset,
            DesiredRateExpression<? extends VNumber> xIncrementSize) {
        return new LineGraphPlot(new DesiredRateExpressionListImpl<Object>().and(yArray).and(xInitialOffset).and(xIncrementSize),
                new LineGraphFunction(yArray.getFunction(), xInitialOffset.getFunction(), xIncrementSize.getFunction()), "lineGraph");
    }

    public static LineGraphPlot lineGraphOf(DesiredRateExpression<? extends VNumberArray> xVDoubleArray, DesiredRateExpression<? extends VNumberArray> yVDoubleArray) {
        return new LineGraphPlot(new DesiredRateExpressionListImpl<Object>().and(xVDoubleArray).and(yVDoubleArray),
                new LineGraphFunction(xVDoubleArray.getFunction(), yVDoubleArray.getFunction()), "lineGraph");
    }

}
