package com.github.germanosin.JsonQL.filters;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.github.germanosin.JsonQL.utils.Json;

import java.util.List;

/**
 * Created by germanosin on 11.10.15.
 */
public class InFilter<T> extends Filter<T> {
    private String key;
    private Iterable<T> values;

    public InFilter(String key, Iterable<T> values, boolean not) {
        super( not ? Type.NOT_IN : Type.IN);
        this.key = key;
        this.values = values;
    }


    @Override
    public JsonNode toJson() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer();
        ArrayNode node = mapper.createArrayNode();
        node.add(type.toString());
        node.add(key);
        for(Object obj:this.values){
            node.add(Json.toJson(obj));
        }
        return node;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Iterable<T> getValues() {
        return values;
    }

    public void setValues(Iterable<T> values) {
        this.values = values;
    }

    @Override
    public void accept(FilterVisitor filterVisitor) {
        filterVisitor.visit(this);
    }
}
