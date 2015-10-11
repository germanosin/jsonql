package com.github.germanosin.JsonQL.parsers;

import com.github.germanosin.JsonQL.builders.Q;
import com.github.germanosin.JsonQL.exceptions.CQLParserException;
import com.github.germanosin.JsonQL.filters.BaseFilter;
import com.github.germanosin.JsonQL.filters.Filter;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.arithmetic.*;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.*;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.SubSelect;

import java.util.ArrayList;
import java.util.List;


public class CQLExpressionVisitor implements ExpressionVisitor, ItemsListVisitor {
    private List<Filter<?>> filters = new ArrayList<Filter<?>>();

    Object value;

    List<Object> inValues = new ArrayList<Object>();

    int sign = 1;


    public CQLExpressionVisitor() {

    }

    public List<Filter<?>> getFilters() {
        return filters;
    }

    @Override
    public void visit(NullValue nullValue) {
        value = null;
    }

    @Override
    public void visit(Function function) {

    }

    @Override
    public void visit(SignedExpression signedExpression) {
        sign = signedExpression.getSign() == '-' ? -1 : 1;
        signedExpression.getExpression().accept(this);
    }

    @Override
    public void visit(JdbcParameter jdbcParameter) {

    }

    @Override
    public void visit(JdbcNamedParameter jdbcNamedParameter) {

    }


    @Override
    public void visit(DoubleValue doubleValue) {
        value = doubleValue.getValue() * sign;
        sign = 1;
    }

    @Override
    public void visit(LongValue longValue) {
        value = longValue.getValue() * sign;
        sign = 1;
    }

    @Override
    public void visit(HexValue hexValue) {
        value = hexValue.getValue();
    }

    @Override
    public void visit(DateValue dateValue) {
        value = dateValue.getValue();
    }

    @Override
    public void visit(TimeValue timeValue) {
        value = timeValue.getValue();
    }

    @Override
    public void visit(TimestampValue timestampValue) {
        value = timestampValue.getValue();
    }

    @Override
    public void visit(Parenthesis parenthesis) {

    }


    @Override
    public void visit(StringValue stringValue) {
        value = stringValue.getValue();
    }

    @Override
    public void visit(Addition addition) {

    }

    @Override
    public void visit(Division division) {

    }

    @Override
    public void visit(Multiplication multiplication) {

    }

    @Override
    public void visit(Subtraction subtraction) {

    }


    @Override
    public void visit(AndExpression andExpression) {
        CQLExpressionVisitor visitor = new CQLExpressionVisitor();
        this.visit(andExpression, visitor);
        this.filters.add(Q.And(visitor.getFilters()));
    }

    public void visit(AndExpression andExpression, CQLExpressionVisitor visitor) {

        if (andExpression.getLeftExpression() instanceof AndExpression) {
            this.visit((AndExpression)andExpression.getLeftExpression(), visitor);
        } else {
            andExpression.getLeftExpression().accept(visitor);
        }

        if (andExpression.getRightExpression() instanceof AndExpression) {
            this.visit((AndExpression)andExpression.getRightExpression(), visitor);
        }  else {
            andExpression.getRightExpression().accept(visitor);
        }
    }

    @Override
    public void visit(OrExpression orExpression) {
        CQLExpressionVisitor visitor = new CQLExpressionVisitor();
        this.visit(orExpression, visitor);
        this.filters.add(Q.Or(visitor.getFilters()));
    }

    public void visit(OrExpression orExpression, CQLExpressionVisitor visitor) {

        if (orExpression.getLeftExpression() instanceof OrExpression) {
            this.visit((OrExpression)orExpression.getLeftExpression(), visitor);
        } else {
            orExpression.getLeftExpression().accept(visitor);
        }

        if (orExpression.getRightExpression() instanceof OrExpression) {
            this.visit((OrExpression)orExpression.getRightExpression(), visitor);
        }  else {
            orExpression.getRightExpression().accept(visitor);
        }
    }

    @Override
    public void visit(Between between) {
        between.getLeftExpression().accept(this);
        Object field = value;
        if (field instanceof String) {
            between.getBetweenExpressionStart().accept(this);
            Object start = value;
            between.getBetweenExpressionEnd().accept(this);
            Object end = value;
            this.filters.add(Q.Btw((String) field, start, end));
        }
        between.getLeftExpression().accept(this);
    }

    @Override
    public void visit(EqualsTo equalsTo) {
        parseSimple(equalsTo, equalsTo.isNot() ? Filter.Type.NOT_EQUALS : Filter.Type.EQUALS);
    }

    @Override
    public void visit(NotEqualsTo notEqualsTo) {
        parseSimple(notEqualsTo, Filter.Type.NOT_EQUALS);
    }

    @Override
    public void visit(GreaterThan greaterThan) {
        parseSimple(greaterThan, Filter.Type.GREATER);
    }

    @Override
    public void visit(GreaterThanEquals greaterThanEquals) {
        parseSimple(greaterThanEquals, Filter.Type.GREATER_OR_EQUALS);
    }

    @Override
    public void visit(MinorThan minorThan) {
        parseSimple(minorThan, Filter.Type.LESS);
    }

    @Override
    public void visit(MinorThanEquals minorThanEquals) {
        parseSimple(minorThanEquals, Filter.Type.LESS_OR_EQUALS);
    }

    public void parseSimple(BinaryExpression expression, Filter.Type type) {
        expression.getLeftExpression().accept(this);
        Object left = value;
        if (left instanceof String) {
            expression.getRightExpression().accept(this);
            Object right = value;
            this.filters.add(new BaseFilter(type, (String)left, right));
        }
    }

    @Override
    public void visit(InExpression inExpression) {
        inExpression.getLeftExpression().accept(this);

        inExpression.getLeftExpression().accept(this);
        Object field = value;
        if (field instanceof String) {
            inExpression.getRightItemsList().accept(this);

            Filter.Type type = inExpression.isNot() ? Filter.Type.NOT_IN : Filter.Type.IN;
            this.filters.add(new BaseFilter(type, (String)field, inValues));
        }
    }

    @Override
    public void visit(IsNullExpression isNullExpression) {
        isNullExpression.getLeftExpression().accept(this);
        String field = (String)value;
        if (field!=null) {
            this.filters.add(Q.Eq(field, null));
        }
    }

    @Override
    public void visit(LikeExpression likeExpression) {
        likeExpression.getLeftExpression().accept(this);
        String field = (String)value;
        if (field!=null) {
            likeExpression.getRightExpression().accept(this);
            this.filters.add(Q.Like(field, value));
        }
    }

    @Override
    public void visit(Column column) {
        if (column.getColumnName().equals("true")) {
            value = true;
        } else if (column.getColumnName().equals("false")) {
            value = false;
        } else {
            value = column.getFullyQualifiedName();
        }
    }

    @Override
    public void visit(SubSelect subSelect) {

    }

    @Override
    public void visit(ExpressionList expressionList) {
        this.inValues.clear();
        List<Expression> expressions = expressionList.getExpressions();
        for (Expression expression : expressions) {
            expression.accept(this);
            this.inValues.add(value);
        }
    }

    @Override
    public void visit(MultiExpressionList multiExpressionList) {

    }

    @Override
    public void visit(CaseExpression caseExpression) {

    }

    @Override
    public void visit(WhenClause whenClause) {

    }

    @Override
    public void visit(ExistsExpression existsExpression) {

    }

    @Override
    public void visit(AllComparisonExpression allComparisonExpression) {

    }

    @Override
    public void visit(AnyComparisonExpression anyComparisonExpression) {

    }

    @Override
    public void visit(Concat concat) {
        concat.getLeftExpression().accept(this);
        String left = (String)value;

        concat.getRightExpression().accept(this);
        value = left + (String)value;
    }

    @Override
    public void visit(Matches matches) {

    }

    @Override
    public void visit(BitwiseAnd bitwiseAnd) {

    }

    @Override
    public void visit(BitwiseOr bitwiseOr) {

    }

    @Override
    public void visit(BitwiseXor bitwiseXor) {

    }

    @Override
    public void visit(CastExpression castExpression) {

    }

    @Override
    public void visit(Modulo modulo) {

    }

    @Override
    public void visit(AnalyticExpression analyticExpression) {

    }

    @Override
    public void visit(WithinGroupExpression withinGroupExpression) {

    }

    @Override
    public void visit(ExtractExpression extractExpression) {

    }

    @Override
    public void visit(IntervalExpression intervalExpression) {

    }

    @Override
    public void visit(OracleHierarchicalExpression oracleHierarchicalExpression) {

    }

    @Override
    public void visit(RegExpMatchOperator regExpMatchOperator) {

    }

    @Override
    public void visit(JsonExpression jsonExpression) {

    }

    @Override
    public void visit(RegExpMySQLOperator regExpMySQLOperator) {

    }

    @Override
    public void visit(UserVariable userVariable) {

    }

    @Override
    public void visit(NumericBind numericBind) {

    }

    @Override
    public void visit(KeepExpression keepExpression) {

    }

    @Override
    public void visit(MySQLGroupConcat mySQLGroupConcat) {

    }

    @Override
    public void visit(RowConstructor rowConstructor) {

    }

    public Filter filter() throws CQLParserException {
        if (this.filters.size()>0) {
            return this.filters.get(0);
        } else {
            throw new CQLParserException("filters not activated");
        }
    }
}
