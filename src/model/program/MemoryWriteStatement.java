package model.program;


import exception.MyException;
import exception.SymbolException;
import exception.TypeException;
import model.expressions.Expression;
import model.program.heap.HeapInterface;
import model.program.symbdict.SymbolTableInterface;
import model.program.typecheck.TypeTableInterface;
import model.values.ReferenceType;
import model.values.ReferenceValue;
import model.values.Type;

public class MemoryWriteStatement implements Statement {

    private final String variableName;
    private final Expression newValue;

    public MemoryWriteStatement(String variableName, Expression newValue) {
        this.variableName = variableName;
        this.newValue = newValue;
    }

    @Override
    public ProgramState execute(ProgramState currentState) throws MyException {
        SymbolTableInterface variables = currentState.getSymbolTable();
        HeapInterface heap = currentState.getHeap();
        ReferenceValue reference = (ReferenceValue)variables.getVariableValue(variableName);
        synchronized(heap){
            heap.writeAtLocation(reference.getAddress(), newValue.evaluate(variables,heap));
            return null;
        }
    }

    @Override
    public Type typeCheck(TypeTableInterface typeTable) throws TypeException, SymbolException {
        Type variableType = typeTable.getVariableType(variableName);
        if(!(variableType instanceof ReferenceType))
            throw new TypeException("Expected ReferenceType instead of + " + variableType + "!");
        Type.expectType(newValue.typeCheck(typeTable),((ReferenceType)variableType).getInner());
        return Type.Void;
    }

    @Override
    public String toString() {
        return "@" + variableName + "<--- " + newValue;
    }
}
