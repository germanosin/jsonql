package com.github.germanosin.JsonQL.arguments;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.germanosin.JsonQL.filters.FilterVisitor;
import com.github.germanosin.JsonQL.utils.Json;


public class BaseArgument<T> implements Argument<T> {

    public Argument.Type type;

    public BaseArgument(T value, Argument.Type type) {
        this.value = value;
        this.type = type;
    }

    protected T value;

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public Argument.Type getType() {
        return type;
    }

    public JsonNode toJson() {
        return Json.toJson(value);
    }

    public void accept(ArgumentVisitor argumentVisitor) {
        argumentVisitor.visit(this);
    }

    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass().equals(value.getClass())) {
            return obj.equals(value);
        } else if (obj instanceof BaseArgument) {
            return ((BaseArgument) obj).getValue().equals(value) && ((BaseArgument) obj).getType().equals(getType());
        } else {
            return super.equals(obj);
        }
    }
}
