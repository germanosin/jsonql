package com.github.germanosin.JsonQL.forms;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.germanosin.JsonQL.utils.Json;


public class Argument<T> {

    public Type type;

    public enum Type {
        BASE("base"),
        FIELD("field"),
        FUNCTION("function");

        private final String text;

        /**
         * @param text
         */
        private Type(final String text) {
            this.text = text;
        }

        /* (non-Javadoc)
         * @see java.lang.Enum#toString()
         */
        @Override
        public String toString() {
            return text;
        }
    }

    public Argument(T value, Type type) {
        this.value = value;
        this.type = type;
    }

    private T value;

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public Type getType() {
        return type;
    }

    public JsonNode toJson() {
        JsonNode result = null;
        if(type.equals(Type.BASE)){
            result = Json.toJson(value);
        } else if(type.equals(Type.FIELD)){
            result = Json.toJson("$"+value);
        }

        return result;
    }
}
