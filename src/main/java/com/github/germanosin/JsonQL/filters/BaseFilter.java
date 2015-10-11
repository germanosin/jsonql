package com.github.germanosin.JsonQL.filters;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.github.germanosin.JsonQL.utils.Json;
import java.util.List;


public class BaseFilter<T> extends Filter<T> {
    private String key;
    private T value;
    private Class clazz;


    public BaseFilter(Filter.Type type, String key, T value) {
        super(type);
        this.key = key;
        this.value = value;
        if(value != null){
            this.clazz = value.getClass();
        }
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public JsonNode toJson() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer();
        ArrayNode node = mapper.createArrayNode();
        JsonNode valueJson = Json.toJson(this.value);
        node.add(type.toString());
        node.add(key);

        if(clazz != null){
            node.add(valueJson);
        }
        else{
            node.add(NullNode.getInstance());
        }

        return node;
    }

    @Override
    public void accept(FilterVisitor filterVisitor) {
        filterVisitor.visit(this);
    }

}
