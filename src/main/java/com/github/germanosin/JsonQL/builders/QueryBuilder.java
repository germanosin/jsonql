package com.github.germanosin.JsonQL.builders;

public abstract class QueryBuilder {

    public static  BaseQueryBuilder create(){
        return new BaseQueryBuilder();
    }

    public abstract SelectBuilder select();
    public abstract FilterBuilder where();
    public abstract OrderBuilder order();
    public abstract QueryBuilder limit(int value);
    public abstract QueryBuilder offset(int value);
    public abstract Query generate();

}
