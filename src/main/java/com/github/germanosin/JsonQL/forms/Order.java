package com.github.germanosin.JsonQL.forms;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.github.germanosin.JsonQL.exceptions.WrongFormatException;


public class Order {

    public enum Type {
        ASC("asc"),
        DESC("desc");

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

        public static Type getByString(String s) throws WrongFormatException {
            Type result;
            if(s.equals(ASC.toString())) result = ASC;
            else if(s.equals(DESC.toString())) result = DESC;
            else throw new WrongFormatException("there is no such type : "+s);
            return result;
        }
    }

    private Type type;
    private String fieldName;

    public Order(Type type, String fieldName) {
        this.type = type;
        this.fieldName = fieldName;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public JsonNode toJson() {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode node = mapper.createArrayNode();
        node.add(type.toString());
        node.add(fieldName);
        return node;
    }
}
