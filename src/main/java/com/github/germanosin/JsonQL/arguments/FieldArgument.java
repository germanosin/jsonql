package com.github.germanosin.JsonQL.arguments;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.germanosin.JsonQL.utils.Json;

/**
 * Created by germanosin on 12.10.15.
 */
public class FieldArgument extends BaseArgument<String> {
    public FieldArgument(String value) {
        super(value, Type.FIELD);
    }

    public String getValue() {
        return value;
    }

    public JsonNode toJson() {
        return Json.toJson("&" + value);
    }

    public String toString() {
        return "&"+value;
    }

    public void accept(ArgumentVisitor argumentVisitor) {
        argumentVisitor.visit(this);
    }
}
