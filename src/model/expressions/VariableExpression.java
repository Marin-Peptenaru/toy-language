package model.expressions;

import exception.ExpressionException;
import exception.SymbolException;
import exception.VariableUndeclaredException;
import model.program.heap.HeapInterface;
import model.program.symbdict.SymbolTableInterface;
import model.program.typecheck.TypeTableInterface;
import model.values.Type;
import model.values.Value;

public final class VariableExpression implements Expression{

    private final String variableName;

    public VariableExpression(String variableName) {
        this.variableName = variableName;
    }

    @Override
    public Value<?> evaluate(SymbolTableInterface symbols, HeapInterface heap) throws ExpressionException, SymbolException {
        if(!symbols.checkVariableIsDeclared(variableName))
            throw new VariableUndeclaredException("Variable " +  variableName + " not declared!");
        return symbols.getVariableValue(variableName);
    }

    @Override
    public String toString() {
        return variableName;
    }

    @Override
    public Type typeCheck(TypeTableInterface typeTable) throws SymbolException {

        return typeTable.getVariableType(variableName);
    }
}
