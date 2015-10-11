package com.github.germanosin.JsonQL.parsers;

import com.github.germanosin.JsonQL.arguments.Argument;
import com.github.germanosin.JsonQL.builders.Q;
import com.github.germanosin.JsonQL.exceptions.CQLParserException;
import com.github.germanosin.JsonQL.exceptions.CQLUnsupportedException;
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

    boolean right = false;

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
        throw new CQLUnsupportedException("function");
    }

    @Override
    public void visit(SignedExpression signedExpression) {
        sign = signedExpression.getSign() == '-' ? -1 : 1;
        signedExpression.getExpression().accept(this);
    }

    @Override
    public void visit(JdbcParameter jdbcParameter) {
        throw new CQLUnsupportedException("jdbcParameter");
    }

    @Override
    public void visit(JdbcNamedParameter jdbcNamedParameter) {
        throw new CQLUnsupportedException("jdbcNamedParameter");
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
        throw new CQLUnsupportedException("parenthesis");
    }


    @Override
    public void visit(StringValue stringValue) {
        String value = stringValue.getValue();
        if (value.startsWith("$")) {
            this.value = Q.Value(stringValue);
        } else {
            this.value = stringValue;
        }
    }

    @Override
    public void visit(Addition addition) {
        throw new CQLUnsupportedException("addition");
    }

    @Override
    public void visit(Division division) {
        throw new CQLUnsupportedException("division");
    }

    @Override
    public void visit(Multiplication multiplication) {
        throw new CQLUnsupportedException("multiplication");
    }

    @Override
    public void visit(Subtraction subtraction) {
        throw new CQLUnsupportedException("subtraction");
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
        acceptLeft(expression.getLeftExpression());
        Object left = value;
        if (left instanceof String) {
            acceptRight(expression.getRightExpression());
            Object right = value;
            this.filters.add(new BaseFilter(type, (String)left, right));
        }
    }

    protected void acceptRight(Expression expression) {
        this.right = true;
        expression.accept(this);
        this.right = false;
    }

    protected void acceptLeft(Expression expression) {
        boolean right = this.right;
        this.right = false;
        expression.accept(this);
        this.right = right;
    }

    @Override
    public void visit(InExpression inExpression) {
        inExpression.getLeftExpression().accept(this);

        inExpression.getLeftExpression().accept(this);
        Object field = value;
        if (field instanceof String) {
            inExpression.getRightItemsList().accept(this);

            if (inExpression.isNot()) {
                this.filters.add(Q.NotInList((String)field, inValues));
            } else {
                this.filters.add(Q.InList((String)field, inValues));
            }
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
            if (!this.right) {
                value = column.getFullyQualifiedName();
            } else {
                value = Q.Value(column.getFullyQualifiedName());
            }
        }
    }

    @Override
    public void visit(SubSelect subSelect) {
        throw new CQLUnsupportedException("subSelect");
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
        throw new CQLUnsupportedException("multiExpressionList");
    }

    @Override
    public void visit(CaseExpression caseExpression) {
        throw new CQLUnsupportedException("caseExpression");
    }

    @Override
    public void visit(WhenClause whenClause) {
        throw new CQLUnsupportedException("whenClause");
    }

    @Override
    public void visit(ExistsExpression existsExpression) {
        throw new CQLUnsupportedException("exists");
    }

    @Override
    public void visit(AllComparisonExpression allComparisonExpression) {
        throw new CQLUnsupportedException("all");
    }

    @Override
    public void visit(AnyComparisonExpression anyComparisonExpression) {
        throw new CQLUnsupportedException("any");
    }

    @Override
    public void visit(Concat concat) {
        List<Argument> arguments = new ArrayList<Argument>();

        this.applyConcat(concat.getLeftExpression(), arguments);
        this.applyConcat(concat.getRightExpression(), arguments);

        this.value = Q.Func("concat", arguments);
    }

    public void applyConcat(Expression expression, List<Argument> arguments) {
        if (expression instanceof Concat) {
            applyConcat(((Concat) expression).getLeftExpression(), arguments);
            applyConcat(((Concat) expression).getRightExpression(), arguments);
        } else {
            expression.accept(this);
            arguments.add( Q.Value(value));
        }
    }

    @Override
    public void visit(Matches matches) {
        throw new CQLUnsupportedException("matches");
    }

    @Override
    public void visit(BitwiseAnd bitwiseAnd) {
        throw new CQLUnsupportedException("bitwiseAnd");
    }

    @Override
    public void visit(BitwiseOr bitwiseOr) {
        throw new CQLUnsupportedException("bitwiseOr");
    }

    @Override
    public void visit(BitwiseXor bitwiseXor) {
        throw new CQLUnsupportedException("bitwiseXor");
    }

    @Override
    public void visit(CastExpression castExpression) {
        throw new CQLUnsupportedException("castExpression");
    }

    @Override
    public void visit(Modulo modulo) {
        throw new CQLUnsupportedException("modulo");
    }

    @Override
    public void visit(AnalyticExpression analyticExpression) {
        throw new CQLUnsupportedException("analyticExpression");
    }

    @Override
    public void visit(WithinGroupExpression withinGroupExpression) {
        throw new CQLUnsupportedException("withinGroupExpression");
    }

    @Override
    public void visit(ExtractExpression extractExpression) {
        throw new CQLUnsupportedException("extractExpression");
    }

    @Override
    public void visit(IntervalExpression intervalExpression) {
        throw new CQLUnsupportedException("intervalExpression");
    }

    @Override
    public void visit(OracleHierarchicalExpression oracleHierarchicalExpression) {
        throw new CQLUnsupportedException("oracleHierarchical");
    }

    @Override
    public void visit(RegExpMatchOperator regExpMatchOperator) {
        throw new CQLUnsupportedException("regExpMatch");
    }

    @Override
    public void visit(JsonExpression jsonExpression) {
        throw new CQLUnsupportedException("jsonExpression");
    }

    @Override
    public void visit(RegExpMySQLOperator regExpMySQLOperator) {
        throw new CQLUnsupportedException("regExpMySQL");
    }

    @Override
    public void visit(UserVariable userVariable) {
        throw new CQLUnsupportedException("userVariable");
    }

    @Override
    public void visit(NumericBind numericBind) {
        throw new CQLUnsupportedException("numericBind");
    }

    @Override
    public void visit(KeepExpression keepExpression) {
        throw new CQLUnsupportedException("keepExpression");
    }

    @Override
    public void visit(MySQLGroupConcat mySQLGroupConcat) {
        throw new CQLUnsupportedException("mySQLGroupConcat");
    }

    @Override
    public void visit(RowConstructor rowConstructor) {
        throw new CQLUnsupportedException("rowConstructor");
    }

    public Filter filter() throws CQLParserException {
        if (this.filters.size()>0) {
            return this.filters.get(0);
        } else {
            throw new CQLParserException("filters not activated");
        }
    }
}
