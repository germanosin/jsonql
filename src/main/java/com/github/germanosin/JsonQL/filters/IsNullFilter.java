package com.github.germanosin.JsonQL.filters;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;


public class IsNullFilter extends Filter {

    private String key;

    public IsNullFilter(Type type, String key) {
        super(type);
        this.key = key;
    }

    public String getKey(){
        return key;
    }

    @Override
    public JsonNode toJson() {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode node = mapper.createArrayNode();
        node.add(type.toString());
        return null;
    }
}
