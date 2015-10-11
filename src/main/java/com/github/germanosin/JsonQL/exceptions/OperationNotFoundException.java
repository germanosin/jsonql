package com.github.germanosin.JsonQL.exceptions;


public class OperationNotFoundException extends ParseException {
    public OperationNotFoundException(String operator) {
        super(String.format("данный оператор не обрабатывается фильтрами %s", operator));
    }
}
