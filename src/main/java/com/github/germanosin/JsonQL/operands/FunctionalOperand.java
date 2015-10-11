package com.github.germanosin.JsonQL.operands;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.github.germanosin.JsonQL.arguments.Argument;
import com.github.germanosin.JsonQL.arguments.BaseArgument;

import java.util.List;


public class FunctionalOperand extends SelectOperand {

    private List<Argument> arguments;
    private String functionName;

    public FunctionalOperand(String functionName, List<Argument> elems) {
        super(Type.FUNCTIONAL);
        this.arguments = elems;
        this.functionName = functionName;
    }

    public FunctionalOperand(String functionName, String selectAs, List<Argument> arguments) {
        super(Type.FUNCTIONAL);
        this.arguments = arguments;
        this.functionName = functionName;
        this.selectAs = selectAs;
    }

    @Override
    public JsonNode toJson() {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode result = null;
        ArrayNode funcBody = mapper.createArrayNode();
        funcBody.add("@"+functionName);
        for(Argument arg: arguments){
            funcBody.add(arg.toJson());
        }

        if(selectAs == null){
            result = funcBody;
        } else {
            ArrayNode body = mapper.createArrayNode();
            body.add(funcBody);
            body.add(selectAs);
            result = body;
        }
        return result;
    }

    public List<Argument> getArguments() {
        return arguments;
    }

    public void setArguments(List arguments) {
        this.arguments = arguments;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public String getSelectAs() {
        return selectAs != null? selectAs : functionName;
    }

}
