package com.github.germanosin.JsonQL.exceptions;


import com.github.germanosin.JsonQL.exceptions.ParseException;

public class WrongFormatException extends ParseException {
    public WrongFormatException(String message) {
        super(message);
    }
}
