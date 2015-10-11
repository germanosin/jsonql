package com.github.germanosin.JsonQL.builders;

public class BaseQueryBuilder extends QueryBuilder {

    private Query query;

    public BaseQueryBuilder() {
        query = new Query();
    }

    @Override
    public SelectBuilder select() {
        return new SelectBuilder(this);
    }

    @Override
    public FilterBuilder where() {
        return new FilterBuilder(this);
    }

    @Override
    public OrderBuilder order() {
        return new OrderBuilder(this);
    }

    @Override
    public QueryBuilder limit(int value) {
        query.setLimit(value);
        return this;
    }

    @Override
    public QueryBuilder offset(int value) {
        query.setOffset(value);
        return this;
    }

    @Override
    public Query generate() {
        return query;
    }

    public Query getQuery() {
        return query;
    }
}
