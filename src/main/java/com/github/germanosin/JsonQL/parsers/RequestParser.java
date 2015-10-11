package com.github.germanosin.JsonQL.parsers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.github.germanosin.JsonQL.exceptions.WrongFormatException;
import com.github.germanosin.JsonQL.forms.Argument;
import com.github.germanosin.JsonQL.filters.FunctionalArgument;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public abstract class RequestParser {

    protected typeDefinition<?> findoutClass(JsonNode jsonNode) throws WrongFormatException {
        typeDefinition<?> result = null;
        if(jsonNode.isTextual()) {result = new typeDefinition<String>(String.class, new ArrayList<String>(), jsonNode.asText());}
        else if(jsonNode.isInt()) {result = new typeDefinition<Integer>(Integer.class, new ArrayList<Integer>(), jsonNode.asInt());}
        else if(jsonNode.isLong()) {result = new typeDefinition<Long>(Long.class, new ArrayList<Long>(), jsonNode.asLong());}
        else if(jsonNode.isBoolean()) {result = new typeDefinition<Boolean>(Boolean.class, new ArrayList<Boolean>(), jsonNode.asBoolean());}
        else if(jsonNode.isDouble()) {result = new typeDefinition<Double>(Double.class, new ArrayList<Double>(), jsonNode.asDouble());}
        else if(jsonNode.isArray()) {
            List elems = new ArrayList();
            for(JsonNode n : (ArrayNode) jsonNode){
                typeDefinition td = findoutClass(n);
                elems.add(td.value);
            }
            result = new typeDefinition<List>(List.class, new ArrayList<List>(), elems);
        }
        if(result == null) throw new WrongFormatException(String.format("couldn't find out class of array member in value : %s", jsonNode.toString()));
        return result;
    }

    protected class typeDefinition<T>{
        public Class<T> clazz;
        public List<T> array;
        public T value;

        private typeDefinition(Class<T> clazz, List<T> array, T value) {
            this.clazz = clazz;
            this.array = array;
            this.value = value;
        }
    }

    protected Argument prepareArguments(JsonNode n) throws WrongFormatException {
        Argument arg;
        if(n.isArray() && n.get(0).isTextual() && n.get(0).asText().startsWith("@")){
            String funcName = n.get(0).asText().substring(1);
            List<Argument> args = new ArrayList<Argument>();
            if(n.size() > 1){
                Iterator<JsonNode> iter = n.elements();
                iter.next();
                while (iter.hasNext()){
                    Argument argument = prepareArguments(iter.next());
                    args.add(argument);
                }
            }
            arg = new FunctionalArgument(funcName, args);
        } else{
            typeDefinition def = findoutClass(n);
            if(def.clazz.equals(String.class) && ((String)def.value).startsWith("$")){
                arg = new Argument<String>(((String) def.value).substring(1), Argument.Type.FIELD);
            } else{
                arg = new Argument(def.value, Argument.Type.BASE);
            }

        }
        return arg;
    }

}
