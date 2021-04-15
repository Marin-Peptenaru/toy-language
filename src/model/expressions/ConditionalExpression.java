package model.expressions;

import exception.MyException;
import exception.SymbolException;
import exception.TypeException;
import model.program.heap.HeapInterface;
import model.program.symbdict.SymbolTableInterface;
import model.program.typecheck.TypeTableInterface;
import model.values.Type;
import model.values.Value;

public class ConditionalExpression implements Expression{
    private final Expression condition;
    private final Expression trueValue;
    private final Expression falseValue;

    public ConditionalExpression(Expression condition, Expression trueValue, Expression falseValue) {
        this.condition = condition;
        this.trueValue = trueValue;
        this.falseValue = falseValue;
    }

    @Override
    public Value<?> evaluate(SymbolTableInterface symbols, HeapInterface heap) throws MyException {
        if((Boolean)condition.evaluate(symbols,heap).getValue())
            return trueValue.evaluate(symbols,heap);
        return falseValue.evaluate(symbols,heap);
    }

    @Override
    public Type typeCheck(TypeTableInterface typeTable) throws TypeException, SymbolException {
        Type.expectType(condition.typeCheck(typeTable),Type.Bool);
        Type expressionType = trueValue.typeCheck(typeTable);
        Type.expectType(falseValue.typeCheck(typeTable),expressionType);
        return expressionType;
    }

    @Override
    public String toString() {
        return condition + "? " + trueValue + " : " + falseValue;
    }
}
