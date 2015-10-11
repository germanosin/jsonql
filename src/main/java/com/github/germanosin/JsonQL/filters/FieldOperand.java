package com.github.germanosin.JsonQL.filters;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.github.germanosin.JsonQL.forms.SelectOperand;
import com.github.germanosin.JsonQL.utils.Json;


public class FieldOperand extends SelectOperand {

    private String fieldName;


    public FieldOperand(String fieldName) {
        super(Type.SIMPLE);
        this.fieldName = fieldName;
    }

    public FieldOperand(String fieldName, String selectAs) {
        super(Type.SIMPLE);
        this.fieldName = fieldName;
        this.selectAs = selectAs;
    }

    @Override
    public JsonNode toJson() {
        JsonNode result;
        if(selectAs == null){
            result = Json.toJson(fieldName);
        } else {
            ObjectMapper mapper = new ObjectMapper();
            ArrayNode arr = mapper.createArrayNode();
            arr.add(Json.toJson(fieldName));
            arr.add(Json.toJson(selectAs));
            result = arr;
        }
        return result;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getSelectAs() {
        return selectAs != null? selectAs : fieldName;
    }

}
