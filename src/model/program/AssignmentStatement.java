package model.program;

import exception.MyException;
import exception.SymbolException;
import exception.TypeException;
import exception.VariableUndeclaredException;
import model.expressions.Expression;
import model.program.symbdict.SymbolTableInterface;
import model.program.typecheck.TypeTableInterface;
import model.values.Type;

public final class AssignmentStatement implements Statement {
    private final String variableName;
    private final Expression expression;

    public AssignmentStatement(String variableName, Expression expression) {
        this.variableName = variableName;
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState currentState) throws MyException {
        SymbolTableInterface variables = currentState.getSymbolTable();
        if(!variables.checkVariableIsDeclared(variableName))
            throw new VariableUndeclaredException("Variable " + variableName + " is not declared.");
        variables.defineVariable(variableName, expression.evaluate(variables, currentState.getHeap()));
        return null;
    }

    @Override
    public String toString() {
        return variableName + " = " + expression;
    }

    @Override
    public Type typeCheck(TypeTableInterface typeTable) throws TypeException, SymbolException {
        if(!typeTable.checkVariableDeclared(variableName))
            throw new VariableUndeclaredException(this + ": Variable " + variableName + " is not declared!");
        Type variableType = typeTable.getVariableType(variableName);
        Type.expectType(expression.typeCheck(typeTable),variableType);
        return variableType;
    }
}
