package com.github.germanosin.JsonQL.arguments;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Created by germanosin on 11.10.15.
 */
public interface Argument<T> {

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

    public Argument.Type getType();

    public void accept(ArgumentVisitor argumentVisitor);

    public JsonNode toJson();
}
