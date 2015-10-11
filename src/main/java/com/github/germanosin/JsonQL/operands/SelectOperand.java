package com.github.germanosin.JsonQL.operands;


import com.fasterxml.jackson.databind.JsonNode;


public abstract class SelectOperand {

    protected Type type;
    protected String selectAs = null;

    public SelectOperand(Type type) {
        this.type = type;
    }

    public enum Type{

        SIMPLE("simple"),
        FUNCTIONAL("functional");

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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public abstract JsonNode toJson();

    public abstract String getSelectAs();

    public void setSelectAs(String selectAs) {
        this.selectAs = selectAs;
    }
}
