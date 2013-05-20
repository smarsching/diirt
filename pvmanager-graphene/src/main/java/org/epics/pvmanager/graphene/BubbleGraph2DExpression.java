/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.graphene;

import org.epics.graphene.BubbleGraph2DRendererUpdate;
import org.epics.graphene.Graph2DRendererUpdate;
import org.epics.graphene.ScatterGraph2DRendererUpdate;
import org.epics.pvmanager.ReadFunction;
import org.epics.pvmanager.expression.DesiredRateExpression;
import org.epics.pvmanager.expression.DesiredRateExpressionImpl;
import org.epics.pvmanager.expression.DesiredRateExpressionList;
import org.epics.pvmanager.expression.DesiredRateExpressionListImpl;
import org.epics.vtype.VString;
import org.epics.vtype.VTable;
import static org.epics.pvmanager.graphene.ExpressionLanguage.*;
        
/**
 * @author shroffk
 *
 */
public class BubbleGraph2DExpression extends DesiredRateExpressionImpl<Graph2DResult> implements Graph2DExpression<BubbleGraph2DRendererUpdate> {

    BubbleGraph2DExpression(DesiredRateExpressionList<?> childExpressions,
            ReadFunction<Graph2DResult> function, String defaultName) {
        super(childExpressions, function, defaultName);
    }

    BubbleGraph2DExpression(DesiredRateExpression<?> tableData,
	    DesiredRateExpression<?> xColumnName,
	    DesiredRateExpression<?> yColumnName,
	    DesiredRateExpression<?> tooltipColumnName) {
        super(ExpressionLanguage.<Object>createList(tableData, xColumnName, yColumnName, tooltipColumnName),
                new ScatterGraph2DFunction(functionOf(tableData),
                functionOf(xColumnName), functionOf(yColumnName), functionOf(tooltipColumnName)),
                "Bubble Graph");
    }

    @Override
    public void update(BubbleGraph2DRendererUpdate update) {
        if (getFunction() instanceof BubbleGraph2DRendererUpdate) {
            ((BubbleGraph2DFunction) getFunction()).getRendererUpdateQueue().writeValue(update);
        }
    }

    @Override
    public BubbleGraph2DRendererUpdate newUpdate() {
        return new BubbleGraph2DRendererUpdate();
    }
}
