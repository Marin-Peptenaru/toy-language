package model.expressions;

import exception.ExpressionException;
import exception.MyException;
import exception.SymbolException;
import exception.TypeException;
import model.program.heap.HeapInterface;
import model.program.symbdict.SymbolTableInterface;
import model.program.typecheck.TypeTableInterface;
import model.values.ReferenceType;
import model.values.ReferenceValue;
import model.values.Type;
import model.values.Value;

public class DereferenceExpression implements Expression{

    private final Expression reference;

    public DereferenceExpression(Expression reference) {
        this.reference = reference;
    }

    @Override
    public Value<?> evaluate(SymbolTableInterface symbols, HeapInterface heap) throws MyException {
        synchronized (heap){
            int address = ((ReferenceValue)reference.evaluate(symbols,heap)).getAddress();
            return heap.readFromLocation(address);
        }
    }

    @Override
    public Type typeCheck(TypeTableInterface typeTable) throws TypeException, SymbolException {
        Type expressionType = reference.typeCheck(typeTable);
        if(!(expressionType instanceof ReferenceType))
            throw new TypeException("Expect ReferenceType instead of " + expressionType + "!");
        return ((ReferenceType)expressionType).getInner();
    }

    @Override
    public String toString() {
        return "*(" + reference + " )" ;
    }
}
