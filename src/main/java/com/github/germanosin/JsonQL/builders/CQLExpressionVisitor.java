package com.github.germanosin.JsonQL.builders;

import com.github.germanosin.JsonQL.arguments.*;
import com.github.germanosin.JsonQL.filters.*;
import com.github.germanosin.JsonQL.operands.FunctionalOperand;

/**
 * Created by germanosin on 11.10.15.
 */
public class CQLExpressionVisitor implements FilterVisitor,ArgumentVisitor {
    StringBuilder sb = new StringBuilder();

    @Override
    public void visit(BaseArgument baseArgument) {
        sb.append(baseArgument.getValue());
    }

    @Override
    public void visit(VarArgument varArgument) {
        sb.append(varArgument.getValue());
    }

    @Override
    public void visit(FunctionalArgument functionalArgument) {
        FunctionalOperand operand = functionalArgument.getValue();

        if (operand.getFunctionName().equals("concat")) {
           concatArguments(operand.getArguments(), " || ");
        } else {
            concatArguments(operand.getArguments(), ", ");
        }
    }

    @Override
    public void visit(BaseFilter baseFilter) {
        sb.append(baseFilter.getKey());
        switch (baseFilter.getType()) {
            case GREATER: sb.append(" > "); break;
            case GREATER_OR_EQUALS: sb.append(" >= "); break;
            case LESS: sb.append(" < "); break;
            case LESS_OR_EQUALS: sb.append(" <= "); break;
            case EQUALS: sb.append(" = "); break;
            case NOT_EQUALS: sb.append(" != "); break;
        }
        Object value = baseFilter.getValue();
        if (value instanceof Iterable) {
            sb.append(" IN (");
            concatValues((Iterable)value, ", ");
            sb.append(")");
        } if (value instanceof Filter) {
            ((Filter) value).accept(this);
        } else {
            sb.append(baseFilter.getValue());
        }
    }

    @Override
    public void visit(BetweenFilter betweenFilter) {
        sb.append(betweenFilter.getKey());
        sb.append(" BETWEEN ");
        sb.append(betweenFilter.getFirstOperand());
        sb.append(" AND ");
        sb.append(betweenFilter.getSecondOperand());
    }

    @Override
    public void visit(CompositeFilter compositeFilter) {
        String con =compositeFilter.getType().equals(Filter.Type.ALL) ? " AND " : " OR ";
        Iterable<Filter<?>> filters = compositeFilter.getChildren();
        boolean first = true;
        for (Filter<?> filter : filters) {
            if (first) {
                first = false;
            } else {
                sb.append(con);
            }
            filter.accept(this);
        }
    }

    @Override
    public void visit(InFilter inFilter) {
        sb.append(inFilter.getKey());
        if (inFilter.getType().equals(Filter.Type.IN)) {
            sb.append(" IN (");
        } else{
            sb.append(" NOT IN (");
        }
       concatValues(inFilter.getValues(), ",");
        sb.append(")");
    }

    @Override
    public void visit(FunctionFilter functionFilter) {
        if (functionFilter.getFunctionName().equals("concat")) {
            concatArguments(functionFilter.getArguments(), " || ");
        } else {
            sb.append(functionFilter.getFunctionName());
            sb.append("(");
            concatArguments(functionFilter.getArguments(), ",");
            sb.append(")");
        }
    }

    @Override
    public void visit(IsNullFilter isNullFilter) {
        sb.append(isNullFilter.getKey());
        if (isNullFilter.getType().equals(Filter.Type.IS_NULL)) {
            sb.append(" IS NULL");
        } else {
            sb.append(" IS NOT NULL");
        }
    }

    public String toString() {
        return sb.toString();
    }

    private void concatValues(Iterable<Object> objects, String concat) {
        boolean first = true;
        for (Object object : objects) {
            if (!first) {
                sb.append(concat);
            } else {
                first = false;
            }
            sb.append(object);
        }
    }

    private void concatArguments(Iterable<Argument> arguments, String concat) {
        boolean first = true;
        for (Argument argument : arguments) {
            if (!first) {
                sb.append(concat);
            } else {
                first = false;
            }
            argument.accept(this);
        }
    }

}
