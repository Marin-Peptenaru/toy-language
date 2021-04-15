package model.program;

import exception.MyException;
import exception.SymbolException;
import exception.TypeException;
import model.expressions.Expression;
import model.program.heap.HeapInterface;
import model.program.symbdict.SymbolTableInterface;
import model.program.typecheck.TypeTableInterface;
import model.values.ReferenceType;
import model.values.Type;

public class MemoryAllocationStatement implements Statement {
    private final String variableName;
    private final Expression expression;

    public MemoryAllocationStatement(String variableName, Expression expression) {
        this.variableName = variableName;
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState currentState) throws MyException {
        SymbolTableInterface variables = currentState.getSymbolTable();
        HeapInterface heap = currentState.getHeap();
        Type memoryLocationType = ((ReferenceType)variables.getVariableValue(variableName).getType()).getInner();
        synchronized (heap){
            variables.defineVariable(variableName,heap.allocateMemory(memoryLocationType));
            heap.writeAtLocation((Integer)variables.getVariableValue(variableName).getValue(),expression.evaluate(variables,heap));
            return null;
        }
    }

    @Override
    public Type typeCheck(TypeTableInterface typeTable) throws TypeException, SymbolException {
        Type variableType = typeTable.getVariableType(variableName);
        if(! (variableType instanceof ReferenceType))
            throw new TypeException("Expected ReferenceType instead of " + variableType + "!");
        Type.expectType(expression.typeCheck(typeTable),((ReferenceType)variableType).getInner());
        return Type.Void;
    }

    @Override
    public String toString() {
        return "New " + variableName + ": " + expression;
    }
}
