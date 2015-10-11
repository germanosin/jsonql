package com.github.germanosin.JsonQL.operands;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.github.germanosin.JsonQL.operands.SelectOperand;

import java.util.List;


public class SelectList {
    List<SelectOperand> operands;

    public SelectList(List<SelectOperand> operands) {
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
