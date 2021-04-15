package model.expressions;

import exception.*;
import model.program.heap.HeapInterface;
import model.program.symbdict.SymbolTableInterface;
import model.program.typecheck.TypeTableInterface;
import model.values.IntValue;
import model.values.Type;
import model.values.Value;

import java.util.EnumMap;
import java.util.function.BiFunction;

public final class ArithmeticExpression implements Expression{


    public enum Operator{
        ADD, SUB, DIV, MUL;

        private static final EnumMap<Operator,String> operatorString = new EnumMap<>(Operator.class);
        private static final EnumMap<Operator,BiFunction<IntValue,IntValue,IntValue>> operatorFunc = new EnumMap<>(Operator.class);

        static{
            operatorString.put(ADD,"+");
            operatorString.put(SUB,"-");
            operatorString.put(MUL,"*");
            operatorString.put(DIV,"/");

            operatorFunc.put(ADD,(IntValue leftOperandValue,IntValue rightOperandValue) ->
                    new IntValue(leftOperandValue.getValue() + rightOperandValue.getValue()));

            operatorFunc.put(SUB,(IntValue leftOperandValue,IntValue rightOperandValue) ->
                    new IntValue(leftOperandValue.getValue() - rightOperandValue.getValue()));

            operatorFunc.put(MUL,(IntValue leftOperandValue,IntValue rightOperandValue) ->
                    new IntValue(leftOperandValue.getValue() * rightOperandValue.getValue()));



            operatorFunc.put(DIV,(IntValue leftOperandValue,IntValue rightOperandValue) ->
                    {
                        if(rightOperandValue.getValue() == 0)
                            throw new DivisionByZeroException();
                        return new IntValue(leftOperandValue.getValue() / rightOperandValue.getValue());
                    } );
        }

        public String toString(){
            return operatorString.get(this);
        }

        public IntValue apply(IntValue leftOperandValue, IntValue rightOperandValue) {
            return operatorFunc.get(this).apply(leftOperandValue,rightOperandValue);
        }
    }

    private final Expression leftOperand;
    private final Expression rightOperand;
    private final Operator operator;


    public ArithmeticExpression(Expression left, Expression right, Operator operator){
        this.leftOperand  = left;
        this.rightOperand = right;
        this.operator = operator;
    }
    @Override
    public IntValue evaluate(SymbolTableInterface symbols, HeapInterface heap) throws MyException {
        Value<?> leftValue = leftOperand.evaluate(symbols, heap);
        Value<?> rightValue = rightOperand.evaluate(symbols, heap);
        return operator.apply((IntValue)leftValue,(IntValue)rightValue);
    }

    @Override
    public Type typeCheck(TypeTableInterface typeTable) throws SymbolException, TypeException {
        Type.expectType(leftOperand.typeCheck(typeTable), Type.Int);
        Type.expectType(rightOperand.typeCheck(typeTable), Type.Int);
        return Type.Int;
    }

    @Override
    public String toString() {
        return "(" + leftOperand.toString() + operator + rightOperand.toString() + ")";
    }
}
