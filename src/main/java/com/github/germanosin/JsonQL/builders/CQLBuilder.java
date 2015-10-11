package com.github.germanosin.JsonQL.builders;

import com.github.germanosin.JsonQL.filters.Filter;

/**
 * Created by germanosin on 11.10.15.
 */
public class CQLBuilder {
    public static String fromFilter(Filter filter) {
        CQLExpressionVisitor visitor = new CQLExpressionVisitor();
        filter.accept(visitor);
        return visitor.toString();
    }
}
