package com.github.germanosin.JsonQL.builders;

import com.github.germanosin.JsonQL.forms.Order;
import com.github.germanosin.JsonQL.forms.OrderList;

import java.util.ArrayList;
import java.util.List;


public class OrderBuilder extends ChildrenBuildder{

    List<Order> orderList;

    public OrderBuilder(BaseQueryBuilder parent) {
        super(parent);
        orderList = new ArrayList<Order>();
    }

    public OrderBuilder desc(String fieldName){
        orderList.add(new Order(Order.Type.DESC, fieldName));
        return this;
    }

    public OrderBuilder asc(String fieldName){
        orderList.add(new Order(Order.Type.ASC, fieldName));
        return this;
    }

    @Override
    protected void goToParent() {
        this.parent.getQuery().setOrder(new OrderList(orderList));
    }
}
