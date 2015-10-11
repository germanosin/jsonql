package com.github.germanosin.JsonQL.parsers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.github.germanosin.JsonQL.exceptions.OperationNotFoundException;
import com.github.germanosin.JsonQL.exceptions.WrongFormatException;
import com.github.germanosin.JsonQL.filters.*;
import com.github.germanosin.JsonQL.forms.*;
import com.github.germanosin.JsonQL.utils.Json;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


public class FilterRequestRequestParser extends RequestParser {

    private static FilterRequestRequestParser instance = null;

    public static FilterRequestRequestParser getInstance(){
        if(instance == null){
            synchronized (FilterRequestRequestParser.class){
                if(instance == null){
                    instance = new FilterRequestRequestParser();
                }
            }
        }
        return instance;
    }

    private FilterRequestRequestParser() {

    }

    public Filter parse(String json) throws WrongFormatException, OperationNotFoundException, WrongFormatException{
        return parse(Json.parse(json));
    }

    public Filter parse(JsonNode node) throws WrongFormatException, OperationNotFoundException, WrongFormatException{
        if(!node.isArray()) throw new WrongFormatException("filter is not an JSON array");
        String operator = node.get(0).asText();
        return getFilter(operator, node);
    }

    private Filter getFilter(String operator, JsonNode node) throws WrongFormatException, OperationNotFoundException{
        Filter.Type [] base = {
                        Filter.Type.EQUALS,
                        Filter.Type.GREATER,
                        Filter.Type.GREATER_OR_EQUALS,
                        Filter.Type.LESS,
                        Filter.Type.LESS_OR_EQUALS,
                        Filter.Type.LIKE,
                        Filter.Type.NOT_EQUALS

                };

        Filter.Type [] inTypes = {
                        Filter.Type.IN,
                        Filter.Type.NOT_IN
                };

        Filter.Type [] isnull = {
                        Filter.Type.IS_NULL,
                        Filter.Type.IS_NOT_NULL
        };


        Filter.Type [] composite = {
                        Filter.Type.ALL,
                        Filter.Type.ANY,
                        Filter.Type.NONE
                };

        Filter filter = null;
        Filter.Type type = getTypeByString(operator);
        if(Arrays.asList(base).contains(type)) filter = generateBaseFilter(type, node);
        else if(Arrays.asList(inTypes).contains(type)) filter = generateInFilter(type, node);
        else if(Arrays.asList(isnull).contains(type)) filter = generateIsNullFilter(type, node);
        else if(Arrays.asList(composite).contains(type)) filter = generateCompositeFilter(type, node);
        else if(type.equals(Filter.Type.BETWEEN)) filter = generateBetweenFilter(node);
        else if(type.equals(Filter.Type.FUNCTION)) filter = generateFunctionFilter(operator, node);
        else throw new OperationNotFoundException(operator);

        return filter;
    }

    private Filter generateIsNullFilter(Filter.Type type, JsonNode node) throws WrongFormatException {
        JsonNode field = node.get(1);
        if(field == null || !field.isTextual()) throw new WrongFormatException(String.format("there is no valid fieldname: %s", node.toString()));
        return new IsNullFilter(type, field.asText());
    }

    private Filter generateBetweenFilter(JsonNode node) throws WrongFormatException {
        if(!node.isArray() && ((ArrayNode)node).size() != 4) throw new WrongFormatException(String.format("between node must contain 4 fields: %s", node.toString()));
        ArrayNode arrayNode = (ArrayNode)node;
        String fieldName = arrayNode.get(1).asText();
        typeDefinition def1 = findoutClass(arrayNode.get(2));
        typeDefinition def2 = findoutClass(arrayNode.get(3));
        if(!def1.clazz.equals(def2.clazz)) throw new WrongFormatException(String.format("operands in between must be the same type: %s", node.toString()));

        return new BetweenFilter(fieldName, def1.value, def2.value);
    }

    private Filter generateFunctionFilter(String operator, JsonNode node) throws WrongFormatException {
        List<Argument> arguments = new ArrayList<Argument>();
        Iterator<JsonNode> iter = node.elements();
        iter.next();
        while (iter.hasNext()){
            JsonNode n = iter.next();
            Argument arg = prepareArguments(n);
            arguments.add(arg);
        }

        return new FunctionFilter(Filter.Type.FUNCTION, operator.substring(1), arguments);
    }

    private Filter generateCompositeFilter(Filter.Type type, JsonNode node) throws WrongFormatException, OperationNotFoundException{
        List<Filter<?>> nodes = new ArrayList<Filter<?>>();
        Iterator<JsonNode> iter = node.elements();
        iter.next();
        while (iter.hasNext()){
            JsonNode n = iter.next();
            FilterRequestRequestParser parser = FilterRequestRequestParser.getInstance();
            Filter<?> f = parser.parse(n);
            nodes.add(f);
        }

        return new CompositeFilter(type, nodes);
    }




    private  Filter generateBaseFilter(Filter.Type type, JsonNode node) throws WrongFormatException {
        JsonNode jsonKey = node.get(1);
        JsonNode jsonValue = node.get(2);
        if(jsonKey == null) throw new WrongFormatException("filter key is null");
        String key = jsonKey.asText();
        if(key == null) throw new WrongFormatException("filter key couldn't be parsed as String");
        if(jsonValue == null) throw new WrongFormatException("filter value is null");
        Filter result;
        if(jsonValue.isTextual()) result = new BaseFilter<String>(type, key, jsonValue.asText());
        else if(jsonValue.isInt()) result = new BaseFilter<Integer>(type, key, jsonValue.asInt());
        else if(jsonValue.isLong()) result = new BaseFilter<Long>(type, key, jsonValue.asLong());
        else if(jsonValue.isBoolean()) result = new BaseFilter<Boolean>(type, key, jsonValue.asBoolean());
        else if(jsonValue.isDouble()) result = new BaseFilter<Double>(type, key, jsonValue.asDouble());
        else if(jsonValue.isArray()){
            result = generateArrayInFilter(type, jsonValue, key);
        }
        else throw new WrongFormatException("couldn't parse value");
        return result;
    }

    private Filter generateInFilter(Filter.Type type, JsonNode node) throws WrongFormatException {
        JsonNode jsonKey = node.get(1);
        if(jsonKey == null) throw new WrongFormatException("filter key is null");
        String key = jsonKey.asText();
        if(key == null) throw new WrongFormatException("filter key couldn't be parsed as String");
        Filter result;
        List<JsonNode> nodes = new ArrayList<JsonNode>();
        Iterator<JsonNode> iter = node.elements();
        iter.next();
        iter.next();
        while (iter.hasNext()){
            nodes.add(iter.next());
        }
        result = getArrayFilter(type, key, nodes);


        return result;
    }

    private Filter generateArrayInFilter(Filter.Type type, JsonNode jsonValue, String key) throws WrongFormatException {
        Filter result;
        List<JsonNode> nodes = new ArrayList<JsonNode>();

        Iterator<JsonNode> iter = jsonValue.elements();
        while (iter.hasNext()){
            nodes.add(iter.next());
        }
        result = getArrayFilter(type, key, nodes);
        return result;
    }

    private Filter getArrayFilter(Filter.Type type, String key, List<JsonNode> nodes) throws WrongFormatException {
        Filter result;
        if(nodes.size() == 0) throw new WrongFormatException("filter value is empty array");
        typeDefinition<?> td = findoutClass(nodes.get(0));
        Class clazz  = td.clazz;
        List array = td.array;

        for(JsonNode n : nodes){
            typeDefinition<?> tdn = findoutClass(n);
            if(!tdn.clazz.equals(clazz)) throw new WrongFormatException("filter value is array with different type members");
            array.add(tdn.value);
        }

        result = new BaseFilter<List<?>>(type, key, array);
        return result;
    }

    private Filter.Type getTypeByString(String operator) throws OperationNotFoundException {
        Filter.Type result = null;
        for(Filter.Type t: Filter.Type.values()){
            if(t.toString().equals(operator)) result = t;
        }

        if(result == null && operator.startsWith("@") && operator.length() > 1) result = Filter.Type.FUNCTION;

        if(result == null) throw new OperationNotFoundException(operator);

        return result;
    }

}
