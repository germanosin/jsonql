package com.github.germanosin.JsonQL.arguments;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.germanosin.JsonQL.utils.Json;

/**
 * Created by germanosin on 11.10.15.
 */
public class VarArgument extends BaseArgument<String> {

    public VarArgument(String value) {
        super(value, Argument.Type.FIELD);
    }

    public String getValue() {
        return "$"+value;
    }

    public JsonNode toJson() {
        return Json.toJson("$"+value);
    }

    public String toString() {
        return "$"+value;
    }

}
