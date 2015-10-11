package com.github.germanosin.JsonQL.filters;

/**
 * Created by germanosin on 11.10.15.
 */
public interface FilterVisitor {
    void visit(BaseFilter baseFilter);
    void visit(BetweenFilter betweenFilter);
    void visit(CompositeFilter compositeFilter);
    void visit(InFilter inFilter);
    void visit(FunctionFilter functionFilter);
    void visit(IsNullFilter isNullFilter);
}
