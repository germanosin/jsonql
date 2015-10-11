package com.github.germanosin.JsonQL.exceptions;

/**
 * Created by germanosin on 11.10.15.
 */
public class CQLUnsupportedException extends RuntimeException {
    public CQLUnsupportedException(String name) {
        super(name + " is unsupported now");
    }
}
