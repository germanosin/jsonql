package com.github.germanosin.JsonQL.parsers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.github.germanosin.JsonQL.exceptions.WrongFormatException;
import com.github.germanosin.JsonQL.filters.FieldOperand;
import com.github.germanosin.JsonQL.filters.FunctionalOperand;
import com.github.germanosin.JsonQL.forms.*;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class SelectRequestRequestParser extends RequestParser {

    public static SelectRequestRequestParser instance = null;

    public static SelectRequestRequestParser getInstance(){
        if(instance == null){
            synchronized (SelectRequestRequestParser.class){
                if(instance == null){
                    instance = new SelectRequestRequestParser();
                }
            }
        }

        return instance;
    }


    private SelectRequestRequestParser() {

    }

    public SelectRequest parse(JsonNode node) throws WrongFormatException {
        if(!node.isArray()) throw new WrongFormatException("select request is not an JSON array");
        ArrayNode arrayNode = (ArrayNode)node;
        if(arrayNode.size() < 1) throw new WrongFormatException("there is no fields for selection");
        Iterator<JsonNode> iter = arrayNode.elements();
        List<SelectOperand> operands = new ArrayList<SelectOperand>();
        while (iter.hasNext()){
            operands.add(bakeOperator(iter.next()));
        }
        return new SelectRequest(operands);
    }

    private SelectOperand bakeOperator(JsonNode node) throws WrongFormatException{
        SelectOperand operand;
        if(node.isTextual()  ) operand = new FieldOperand(node.asText());
        else if(node.isArray()){
            if(node.get(0).isArray()) operand = bakeFunctionalOperand(node);
            else {
                String fieldName = node.get(0).asText();
                String selectAs = node.get(1).asText();
                if(fieldName == null) throw new WrongFormatException(String.format("field name is null '$' %s", node.toString()));
                if(selectAs == null) throw new WrongFormatException(String.format("selectAs parametr is null '$' %s", node.toString()));
                operand = new FieldOperand(fieldName, selectAs);
            }

        }
        else throw new WrongFormatException(String.format("couldn't parse select parametr %s", node.toString()));
        return operand;
    }

    private SelectOperand bakeFunctionalOperand(JsonNode node) throws WrongFormatException {
        ArrayNode funcBody;
        String selectAs = null;
        if(node.get(0).isTextual() && node.get(0).asText().startsWith("@")){
            funcBody = (ArrayNode)node;
        }
        else if(node.get(0).isArray() && node.get(1) != null && node.get(1).isTextual() && node.get(0).get(0).isTextual() && node.get(0).get(0).asText().startsWith("@")){
            selectAs = node.get(1).asText();
            funcBody = (ArrayNode)node.get(0);
        }
        else throw new WrongFormatException(String.format("format of functional operand is wrong: %s", node));
        List<Argument> args = new ArrayList<Argument>();
        Iterator<JsonNode> iter = funcBody.elements();
        iter.next();
        while (iter.hasNext()){
            args.add(prepareArguments(iter.next()));
        }
        return new FunctionalOperand(funcBody.get(0).asText().substring(1), selectAs, args);
    }
}
