package com.github.germanosin.JsonQL.forms;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.util.List;


public class SelectRequest {
    List<SelectOperand> operands;

    public SelectRequest(List<SelectOperand> operands) {
        this.operands = operands;
    }

    public List<SelectOperand> getOperands() {
        return operands;
    }

    public void setOperands(List<SelectOperand> operands) {
        this.operands = operands;
    }

    public JsonNode toJson(){
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode node = mapper.createArrayNode();
        for(SelectOperand op: operands){
            node.add(op.toJson());
        }
        return node;
    }
}
