package model.expressions;
import exception.*;
import model.program.heap.HeapInterface;
import model.program.symbdict.SymbolTableInterface;
import model.program.typecheck.TypeTableInterface;
import model.values.Type;
import model.values.Value;
import java.util.EnumMap;
import java.util.function.BiFunction;
import model.values.BoolValue;

public final class LogicalExpression implements Expression {


    public enum Operator{
        AND,
        OR;

        private static final EnumMap<Operator, String> operatorString = new EnumMap<>(Operator.class);
        private static final EnumMap<Operator, BiFunction<BoolValue,BoolValue,BoolValue>> operatorFunc = new EnumMap<>(Operator.class);

        // initialisation for static data of logical operators
        static{
            operatorString.put(Operator.AND,"&&");
            operatorString.put(Operator.OR, "||");
            operatorFunc.put(Operator.AND,
                    (BoolValue leftValueOperand, BoolValue rightValueOperand) ->
                            new BoolValue(leftValueOperand.getValue() && rightValueOperand.getValue()));
            operatorFunc.put(Operator.OR,
                    (BoolValue leftValueOperand, BoolValue rightValueOperand) ->
                            new BoolValue(leftValueOperand.getValue() || rightValueOperand.getValue()));
        }

        public String toString(){
            return operatorString.get(this);
        }

        public BoolValue apply(BoolValue leftOperandValue, BoolValue rightOperandValue){
            return operatorFunc.get(this).apply(leftOperandValue,rightOperandValue);
        }
    }
    private final Expression leftOperand;
    private final Expression rightOperand;
    private final Operator operator;

    public LogicalExpression(Expression leftOperator, Expression rightOperator, Operator operator) {
        this.leftOperand = leftOperator;
        this.rightOperand = rightOperator;
        this.operator = operator;
    }


    @Override
    public BoolValue evaluate(SymbolTableInterface symbols, HeapInterface heap) throws MyException {
        Value<?> leftValue = leftOperand.evaluate(symbols, heap);
        Value<?> rightValue  = rightOperand.evaluate(symbols, heap);
        return operator.apply((BoolValue)leftValue,(BoolValue)rightValue);
    }

    @Override
    public Type typeCheck(TypeTableInterface typeTable) throws SymbolException, TypeException {
        Type.expectType(leftOperand.typeCheck(typeTable), Type.Bool);
        Type.expectType(rightOperand.typeCheck(typeTable), Type.Bool);
        return Type.Bool;
    }

    @Override
    public String toString() {
        return "(" + leftOperand.toString() + operator + rightOperand.toString() + ")";
    }
}
