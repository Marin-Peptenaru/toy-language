package model.expressions;

import exception.ExpressionException;
import exception.MyException;
import exception.SymbolException;
import exception.TypeException;
import model.program.heap.HeapInterface;
import model.program.symbdict.SymbolTableInterface;
import model.program.typecheck.TypeTableInterface;
import model.values.BoolValue;
import model.values.IntValue;
import model.values.Type;

import java.util.EnumMap;
import java.util.function.BiFunction;

public class RelationalExpression implements Expression {

    public enum Operator{
        LT, GT, LE, GE, EQ, NE;

        private static final EnumMap<Operator,String> operatorString = new EnumMap<>(Operator.class);
        private static final EnumMap<Operator, BiFunction<IntValue,IntValue, BoolValue>> operatorFunc = new EnumMap<>(Operator.class);

        static{
            operatorString.put(LT,"<");
            operatorString.put(GT,">");
            operatorString.put(LE,"<=");
            operatorString.put(GE,">=");
            operatorString.put(EQ,"==");
            operatorString.put(NE,"!=");

            operatorFunc.put(LT,(leftValue,rightValue)
                    -> new BoolValue(leftValue.getValue().compareTo(rightValue.getValue()) < 0));
            operatorFunc.put(GT,(leftValue,rightValue)
                    -> new BoolValue(leftValue.getValue().compareTo(rightValue.getValue()) > 0));
            operatorFunc.put(LE,(leftValue,rightValue)
                    -> new BoolValue(leftValue.getValue().compareTo(rightValue.getValue()) <= 0));
            operatorFunc.put(GE,(leftValue,rightValue)
                    -> new BoolValue(leftValue.getValue().compareTo(rightValue.getValue()) >= 0));
            operatorFunc.put(EQ,(leftValue,rightValue)
                    -> new BoolValue(leftValue.equals(rightValue)));
            operatorFunc.put(NE,(leftValue,rightValue)
                    -> new BoolValue(!leftValue.equals(rightValue)));

        }

        public String toString(){
            return operatorString.get(this);
        }

        public BoolValue apply(IntValue leftOperandValue, IntValue rightOperandValue) {
            return operatorFunc.get(this).apply(leftOperandValue,rightOperandValue);
        }
    }

    public RelationalExpression(Operator operator, Expression leftOperand, Expression rightOperand) {
        this.operator = operator;
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    private final Operator operator;
    private final Expression leftOperand;
    private final Expression rightOperand;

    @Override
    public BoolValue evaluate(SymbolTableInterface symbols, HeapInterface heap) throws MyException {
        IntValue leftValue = (IntValue)leftOperand.evaluate(symbols, heap);
        IntValue rightValue = (IntValue)rightOperand.evaluate(symbols, heap);
        return operator.apply(leftValue,rightValue);
    }

    @Override
    public Type typeCheck(TypeTableInterface typeTable) throws TypeException, SymbolException {
        Type.expectType(leftOperand.typeCheck(typeTable),Type.Int);
        Type.expectType(rightOperand.typeCheck(typeTable),Type.Int);
        return Type.Bool;
    }

    @Override
    public String toString() {
        return "(" + leftOperand + operator + rightOperand + ")";
    }
}
