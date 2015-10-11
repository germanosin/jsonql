package com.github.germanosin.JsonQL.builders;


public abstract class ChildrenBuilder extends QueryBuilder {

    protected BaseQueryBuilder parent;

    public ChildrenBuilder(BaseQueryBuilder parent) {
        this.parent = parent;
    }

    @Override
    public SelectBuilder select() {
        goToParent();
        return parent.select();
    }

    @Override
    public FilterBuilder where() {
        goToParent();
        return parent.where();
    }

    @Override
    public OrderBuilder order() {
        goToParent();
        return parent.order();
    }

    @Override
    public QueryBuilder limit(int value) {
        goToParent();
        return parent.limit(value);
    }

    @Override
    public QueryBuilder offset(int value) {
        goToParent();
        return parent.offset(value);
    }

    @Override
    public Query generate() {
        goToParent();
        return parent.generate();
    }

    protected abstract void goToParent();
}
