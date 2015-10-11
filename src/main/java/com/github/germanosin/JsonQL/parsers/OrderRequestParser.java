package com.github.germanosin.JsonQL.parsers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.github.germanosin.JsonQL.exceptions.WrongFormatException;
import com.github.germanosin.JsonQL.forms.Order;
import com.github.germanosin.JsonQL.forms.OrderList;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class OrderRequestParser extends RequestParser {

    private static OrderRequestParser instance = null;

    public static OrderRequestParser getInstance(){
        if(instance == null){
            synchronized (OrderRequestParser.class){
                if(instance == null){
                    instance = new OrderRequestParser();
                }
            }
        }

        return instance;
    }

    private OrderRequestParser() {

    }


    public OrderList parse(JsonNode node) throws WrongFormatException {
        if(!node.isArray()) throw new WrongFormatException(String.format("order request is not an JSON array : %s", node.toString()));
        ArrayNode arrayNode = (ArrayNode) node;
        JsonNode firstOperand = arrayNode.get(0);
        List<Order> orders = new ArrayList<Order>();
        if(firstOperand.isArray()){
            Iterator<JsonNode> iter = arrayNode.elements();
            while (iter.hasNext()){
                orders.add(toOrder(iter.next()));
            }
        } else{
            orders.add(toOrder(arrayNode));
        }
        return new OrderList(orders);
    }

    private Order toOrder(JsonNode node) throws WrongFormatException {
        if(!node.isArray()) throw new WrongFormatException(String.format("order request is not an JSON array : %s", node.toString()));
        JsonNode type = node.get(0);
        JsonNode field = node.get(1);
        if(type == null || !type.isTextual()) throw new WrongFormatException(String.format("type must be an String : %s", node.toString()));
        if(field == null || !field.isTextual()) throw new WrongFormatException(String.format("field must be an String : %s", node.toString()));
        return new Order(Order.Type.getByString(type.asText()), field.asText());
    }

}
