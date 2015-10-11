package com.github.germanosin.JsonQL.filters;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.github.germanosin.JsonQL.utils.Json;


public class BetweenFilter<T> extends Filter<T> {

    private String key;
    private T firstOperand;
    private T secondOperand;
    private Class clazz;

    public BetweenFilter(String fieldName, T firstOperand, T secondOperand) {
        super(Type.BETWEEN);
        this.key = fieldName;
        this.firstOperand = firstOperand;
        this.secondOperand = secondOperand;
        this.clazz = firstOperand.getClass();
    }

    @Override
    public JsonNode toJson() {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode node = mapper.createArrayNode();
        node.add(type.toString());
        node.add(key);
        node.add(Json.toJson(firstOperand));
        node.add(Json.toJson(secondOperand));
        return node;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public T getFirstOperand() {
        return firstOperand;
    }

    public void setFirstOperand(T firstOperand) {
        this.firstOperand = firstOperand;
    }

    public T getSecondOperand() {
        return secondOperand;
    }

    public void setSecondOperand(T secondOperand) {
        this.secondOperand = secondOperand;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    @Override
    public void accept(FilterVisitor filterVisitor) {
        filterVisitor.visit(this);
    }
}
