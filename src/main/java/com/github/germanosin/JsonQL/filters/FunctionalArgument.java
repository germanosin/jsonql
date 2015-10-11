package com.github.germanosin.JsonQL.filters;


import com.fasterxml.jackson.databind.JsonNode;
import com.github.germanosin.JsonQL.forms.Argument;

import java.util.List;


public class FunctionalArgument extends Argument<FunctionalOperand> {


    public FunctionalArgument(String functionName, List<Argument> args) {

        super(new FunctionalOperand(functionName, args), Type.FUNCTION);
    }

    @Override
    public JsonNode toJson() {
        return this.getValue().toJson();
    }
}
