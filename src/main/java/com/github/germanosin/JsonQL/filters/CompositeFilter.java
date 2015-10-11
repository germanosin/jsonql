package com.github.germanosin.JsonQL.filters;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class CompositeFilter extends Filter {

    private Iterable<Filter<?>> children;

    public CompositeFilter(Type type, Iterable<Filter<?>> children) {
        super(type);
        this.children = children;
    }

    public Iterable<Filter<?>> getChildren() {
        return children;
    }

    public void setChildren(Iterable<Filter<?>> children) {
        this.children = children;
    }

    @Override
    public JsonNode toJson() {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode node = mapper.createArrayNode();
        node.add(type.toString());
        for(Filter filter : children){
            node.add(filter.toJson());
        }
        return node;
    }

    @Override
    public void accept(FilterVisitor filterVisitor) {
        filterVisitor.visit(this);
    }
}
