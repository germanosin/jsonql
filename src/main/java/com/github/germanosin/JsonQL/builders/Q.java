package com.github.germanosin.JsonQL.builders;

import com.github.germanosin.JsonQL.filters.*;
import com.github.germanosin.JsonQL.forms.*;

import java.util.Arrays;
import java.util.List;


public class Q {

    public static FunctionalArgument Function(String functionName, Argument ... args) {
        FunctionalArgument arg = new FunctionalArgument(functionName, Arrays.asList(args));
        return arg;
    }

    public static  Argument Field(String fieldName) {
        Argument<String> argument = new Argument(fieldName, Argument.Type.FIELD);
        return argument;
    }

    public static  <T> Argument Value(T value) {
        Argument<T> argument = new Argument(value, Argument.Type.BASE);
        return argument;
    }

    public static <T> BaseFilter Eq(String fieldName, T value){
        return new BaseFilter<T>(Filter.Type.EQUALS, fieldName, value);
    }

    public static <T> BaseFilter Lt(String fieldName, T value){
        return new BaseFilter<T>(Filter.Type.LESS, fieldName, value);
    }

    public static <T> BaseFilter Gt(String fieldName, T value){
        return new BaseFilter<T>(Filter.Type.GREATER, fieldName, value);
    }

    public static <T> BaseFilter Ge(String fieldName, T value){
        return new BaseFilter<T>(Filter.Type.GREATER_OR_EQUALS, fieldName, value);
    }

    public static <T> BaseFilter Le(String fieldName, T value){
        return new BaseFilter<T>(Filter.Type.LESS_OR_EQUALS, fieldName, value);
    }

    public static <T> BaseFilter Ne(String fieldName, T value){
        return new BaseFilter<T>(Filter.Type.NOT_EQUALS, fieldName, value);
    }

    public static <T> BaseFilter In(String fieldName, T ... values){
        return new BaseFilter <List<T>> (Filter.Type.IN, fieldName, Arrays.asList(values));
    }

    public static <T> BaseFilter NotIn(String fieldName, T ... values){
        return new BaseFilter <List<T>> (Filter.Type.NOT_IN, fieldName, Arrays.asList(values));
    }

    public static IsNullFilter IsNull(String fieldName){
        return new IsNullFilter(Filter.Type.IS_NULL, fieldName);
    }

    public static IsNullFilter IsNotNull(String fieldName){
        return new IsNullFilter(Filter.Type.IS_NOT_NULL, fieldName);
    }

    public static <T> BaseFilter Like(String fieldName, T value){
        return new BaseFilter<T>(Filter.Type.LIKE, fieldName, value);
    }

    public static <T> BetweenFilter Btw(String fieldName, T from, T till){
        return new BetweenFilter<T>(fieldName, from, till);
    }

    public static CompositeFilter And(Filter<?> ... filters){
        return new CompositeFilter(Filter.Type.ALL, Arrays.asList(filters));
    }

    public static CompositeFilter And(Iterable<Filter<?>> filters){
        return new CompositeFilter(Filter.Type.ALL, filters);
    }

    public static CompositeFilter Or(Filter<?> ... filters){
        return new CompositeFilter(Filter.Type.ANY, Arrays.asList(filters));
    }

    public static CompositeFilter Or(Iterable<Filter<?>> filters){
        return new CompositeFilter(Filter.Type.ANY, filters);
    }

    public static CompositeFilter None(Filter<?> ... filters){
        return new CompositeFilter(Filter.Type.NONE, Arrays.asList(filters));
    }

    public static FunctionFilter Func(String functionName, Argument ... arguments){
        return new FunctionFilter(Filter.Type.FUNCTION, functionName, Arrays.asList(arguments));
    }

    public static Order Asc(String fieldName) {
        return new Order(Order.Type.ASC, fieldName);
    }

    public static Order Desc(String fieldName) {
        return new Order(Order.Type.DESC, fieldName);
    }

    public static OrderList Orders(Order ... orders) {
       return Orders(Arrays.asList(orders));
    }

    public static OrderList Orders(List<Order> orders) {
        return new OrderList(orders);
    }

}
