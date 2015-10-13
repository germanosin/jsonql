package com.github.germanosin.JsonQL.builders;

import com.github.germanosin.JsonQL.arguments.*;
import com.github.germanosin.JsonQL.filters.*;
import com.github.germanosin.JsonQL.orders.*;
import com.sun.org.apache.xpath.internal.Arg;

import java.util.Arrays;
import java.util.List;


public class Q {

    public static FunctionalArgument Function(String functionName, Argument... args) {
        return new FunctionalArgument(functionName, Arrays.asList(args));
    }

    public static FieldArgument Field(String fieldName) {
        return new FieldArgument(fieldName);
    }

    public static VarArgument Var(String varName) {
        return new VarArgument(varName);
    }

    public static Argument<String> Arg(String name) {
        if (name.startsWith("$")) {
            return Var(name.substring(1));
        } else if (name.startsWith("&")) {
            return Field(name.substring(1));
        } else {
            return Value(name);
        }
    }

    public static  <T> BaseArgument Value(T value) {
        return new BaseArgument <T> (value, BaseArgument.Type.BASE);
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

    public static <T> InFilter In(String fieldName, T ... values){
        return new InFilter<T> (fieldName, Arrays.asList(values), false);
    }

    public static <T> InFilter NotIn(String fieldName, T ... values){
        return new InFilter<T> (fieldName, Arrays.asList(values), true);
    }

    public static <T> InFilter In(String fieldName, List<T> values){
        return new InFilter<T> (fieldName, values, false);
    }

    public static <T> InFilter NotIn(String fieldName, List<T> values){
        return new InFilter<T> (fieldName, values, true);
    }

    public static <T> InFilter InList(String fieldName, List<T> values){
        return new InFilter<T> (fieldName, values, false);
    }

    public static <T> InFilter NotInList(String fieldName, List<T> values){
        return new InFilter<T> (fieldName, values, true);
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

    public static FunctionFilter Func(String functionName, Argument... arguments){
        return new FunctionFilter(Filter.Type.FUNCTION, functionName, Arrays.asList(arguments));
    }

    public static FunctionFilter Func(String functionName,List<Argument> arguments){
        return new FunctionFilter(Filter.Type.FUNCTION, functionName, arguments);
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
