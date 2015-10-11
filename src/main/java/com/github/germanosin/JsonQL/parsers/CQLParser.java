package com.github.germanosin.JsonQL.parsers;

import com.github.germanosin.JsonQL.exceptions.CQLParserException;
import com.github.germanosin.JsonQL.filters.Filter;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;


public class CQLParser {


    private static CQLParser instance = null;

    public static CQLParser getInstance(){
        if(instance == null){
            synchronized (CQLParser.class){
                if(instance == null){
                    instance = new CQLParser();
                }
            }
        }
        return instance;
    }


    public Filter parse(String query) throws CQLParserException, JSQLParserException {
        return toJsonQL(CCJSqlParserUtil.parseCondExpression(query));
    }


    public static Filter toJsonQL(Expression expression) throws CQLParserException {
        CQLExpressionVisitor expressionVisitor = new CQLExpressionVisitor();
        expression.accept(expressionVisitor);
        return expressionVisitor.filter();
    }

}
