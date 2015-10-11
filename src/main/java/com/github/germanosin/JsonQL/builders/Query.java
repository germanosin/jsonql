package com.github.germanosin.JsonQL.builders;

import com.github.germanosin.JsonQL.filters.CompositeFilter;
import com.github.germanosin.JsonQL.filters.Filter;
import com.github.germanosin.JsonQL.orders.*;
import com.github.germanosin.JsonQL.operands.SelectList;

import java.util.ArrayList;

public class Query {

    private SelectList select;
    private  OrderList order;
    private Filter filter;
    private  Integer limit;
    private  Integer offset;

    public Query() {
        filter = new CompositeFilter(Filter.Type.ALL, new ArrayList<Filter<?>>());
    }

    public SelectList getSelect() {
        return select;
    }

    public void setSelect(SelectList select) {
        this.select = select;
    }

    public OrderList getOrder() {
        return order;
    }

    public void setOrder(OrderList order) {
        this.order = order;
    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }
}
