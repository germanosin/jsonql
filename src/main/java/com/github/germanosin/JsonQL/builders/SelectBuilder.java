package com.github.germanosin.JsonQL.builders;

import com.github.germanosin.JsonQL.arguments.Argument;
import com.github.germanosin.JsonQL.arguments.BaseArgument;
import com.github.germanosin.JsonQL.operands.FieldOperand;
import com.github.germanosin.JsonQL.operands.FunctionalOperand;
import com.github.germanosin.JsonQL.operands.SelectList;
import com.github.germanosin.JsonQL.operands.SelectOperand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SelectBuilder  extends ChildrenBuilder {

    List<SelectOperand> selects;
    SelectOperand last;

    public SelectBuilder(BaseQueryBuilder parent) {
        super(parent);
        selects = new ArrayList<SelectOperand>();
    }

    public SelectBuilder as(String selectAs){
        if(last != null){
            last.setSelectAs(selectAs);
        }
        return this;
    }

    public SelectBuilder field(String fieldName){
        SelectOperand field = new FieldOperand(fieldName);
        selects.add(field);
        last = field;
        return this;
    }

    public SelectBuilder function(String functionName, Argument... args){
        FunctionalOperand function = new FunctionalOperand(functionName, Arrays.asList(args));
        return this.function(function);
    }

    public SelectBuilder function(FunctionalOperand function){
        selects.add(function);
        last = function;
        return this;
    }



    @Override
    protected void goToParent() {
        SelectList request = new SelectList(this.selects);
        parent.getQuery().setSelect(request);
    }
}
