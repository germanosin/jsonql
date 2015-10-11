package com.github.germanosin.JsonQL.arguments;

/**
 * Created by germanosin on 11.10.15.
 */
public interface ArgumentVisitor {
    void visit(BaseArgument baseArgument);
    void visit(VarArgument varArgument);
    void visit(FunctionalArgument functionalArgument);
}
