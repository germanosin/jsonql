package com.github.germanosin.JsonQL.arguments;


import com.fasterxml.jackson.databind.JsonNode;
import com.github.germanosin.JsonQL.operands.FunctionalOperand;

import java.util.List;


public class FunctionalArgument extends BaseArgument<FunctionalOperand> {


    public FunctionalArgument(String functionName, List<Argument> args) {

        super(new FunctionalOperand(functionName, args), Argument.Type.FUNCTION);
    }

    @Override
    public JsonNode toJson() {
        return this.getValue().toJson();
    }

    @Override
    public void accept(ArgumentVisitor argumentVisitor) {
        argumentVisitor.visit(this);
    }
}
