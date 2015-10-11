package com.github.germanosin.JsonQL.utils;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.germanosin.JsonQL.exceptions.WrongFormatException;


public class Json {

    private static volatile ObjectMapper objectMapper = null;

    public static JsonNode toJson(final Object data, Class serializationView) {
        try {
            ObjectWriter objectWriter = new ObjectMapper().writerWithView(serializationView);
            return parse(objectWriter.writeValueAsString(data));
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static ObjectMapper mapper() {
        if(objectMapper == null){
            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            objectMapper = mapper;
        }

        return objectMapper;
    }

    public static <A>  A fromJson(JsonNode json, java.lang.Class<A> clazz) throws WrongFormatException {
        try {
            return mapper().treeToValue(json, clazz);
        }catch (JsonMappingException e){
            throw new WrongFormatException("Не удалось десереализовать json: "+e.getMessage());
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static JsonNode toJson(final Object data) {
        return PlayJson.toJson(data);
    }

    public static ObjectNode newObject() {
        return PlayJson.newObject();
    }

    public static String stringify(JsonNode json) {
        return PlayJson.stringify(json);
    }

    public static JsonNode parse(String src) {
        return PlayJson.parse(src);
    }

}
