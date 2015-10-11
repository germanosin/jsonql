package com.github.germanosin.JsonQL.filters;


import com.fasterxml.jackson.databind.JsonNode;


public abstract class Filter<T> {

    public Filter(Type type) {
        this.type = type;
    }

    public enum Type {
        EQUALS("=="),
        BETWEEN("between"),
        LIKE("~~"),
        NOT_EQUALS("!="),
        GREATER(">"),
        GREATER_OR_EQUALS(">="),
        LESS("<"),
        LESS_OR_EQUALS("<="),
        IN("in"),
        NOT_IN("!in"),
        ALL("all"),
        ANY("any"),
        NONE("none"),
        FUNCTION("@"),
        IS_NULL("null"),
        IS_NOT_NULL("!null")
        ;

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

    protected Filter.Type type;

    public Filter.Type getType() {
        return type;
    }

    public void setType(Filter.Type type) {
        this.type = type;
    }

    public abstract JsonNode toJson();

    public abstract void accept(FilterVisitor filterVisitor);

}
