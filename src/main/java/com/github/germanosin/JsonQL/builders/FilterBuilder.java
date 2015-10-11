package com.github.germanosin.JsonQL.builders;

import com.github.germanosin.JsonQL.filters.CompositeFilter;
import com.github.germanosin.JsonQL.filters.Filter;

import java.util.ArrayList;
import java.util.List;


public class FilterBuilder extends ChildrenBuildder {

    private List<Filter<?>> filters;

    public FilterBuilder(BaseQueryBuilder parent) {
        super(parent);
        filters = new ArrayList<Filter<?>>();
    }

    public <T> FilterBuilder eq(String fieldName, T value){
        filters.add(Q.Eq(fieldName, value));
        return this;
    }

    public <T> FilterBuilder lt(String fieldName, T value){
        filters.add(Q.Lt(fieldName, value));
        return this;
    }

    public <T> FilterBuilder gt(String fieldName, T value){
        filters.add(Q.Gt(fieldName, value));
        return this;
    }

    public <T> FilterBuilder le(String fieldName, T value){
        filters.add(Q.Le(fieldName, value));
        return this;
    }

    public <T> FilterBuilder ge(String fieldName, T value){
        filters.add(Q.Ge(fieldName, value));
        return this;
    }

    public <T> FilterBuilder ne(String fieldName, T value){
        filters.add(Q.Ne(fieldName, value));
        return this;
    }

    public <T> FilterBuilder in(String fieldName, T ... values){
        filters.add(Q.In(fieldName, values));
        return this;
    }

    public <T> FilterBuilder notIn(String fieldName, T ... values){
        filters.add(Q.NotIn(fieldName, values));
        return this;
    }

    public  FilterBuilder isNull(String fieldName){
        filters.add(Q.IsNull(fieldName));
        return this;
    }

    public  FilterBuilder isNotNull(String fieldName){
        filters.add(Q.IsNotNull(fieldName));
        return this;
    }

    public <T> FilterBuilder like(String fieldName, T value){
        filters.add(Q.Like(fieldName, value));
        return this;
    }

    public <T> FilterBuilder btw(String fieldName, T from, T till){
        filters.add(Q.Btw(fieldName, from, till));
        return this;
    }

    public <T> FilterBuilder and(Filter<?> ... filterArray){
        filters.add(Q.And(filterArray));
        return this;
    }

    public <T> FilterBuilder or(Filter<?> ... filterArray){
        filters.add(Q.Or(filterArray));
        return this;
    }

    public <T> FilterBuilder none(Filter<?> ... filterArray){
        filters.add(Q.None(filterArray));
        return this;
    }



    @Override
    protected void goToParent() {
        if(filters.size() > 1){
            Filter filter = new CompositeFilter(Filter.Type.ALL, filters);
            parent.getQuery().setFilter(filter);
        } else if(filters.size() == 1){
            parent.getQuery().setFilter(filters.get(0));
        }
    }
}
