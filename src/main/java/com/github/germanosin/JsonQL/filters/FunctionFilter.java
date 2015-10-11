package com.github.germanosin.JsonQL.filters;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.github.germanosin.JsonQL.arguments.Argument;
import com.github.germanosin.JsonQL.arguments.BaseArgument;
import com.github.germanosin.JsonQL.utils.Json;


import java.util.List;


public class FunctionFilter extends Filter {

    private List<Argument> arguments;
    private String functionName;

    public FunctionFilter(Type type, String functionName, List<Argument> arguments) {
        super(type);
        this.functionName = functionName;
        this.arguments = arguments;
    }



    @Override
    public JsonNode toJson() {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode node = mapper.createArrayNode();
        node.add("@"+functionName);
        for(Object obj: arguments){
            node.add(Json.toJson(obj));
        }
        return node;
    }

    public List<Argument> getArguments() {
        return arguments;
    }

    public void setArguments(List<Argument> arguments) {
        this.arguments = arguments;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    @Override
    public void accept(FilterVisitor filterVisitor) {
        filterVisitor.visit(this);
    }
}
