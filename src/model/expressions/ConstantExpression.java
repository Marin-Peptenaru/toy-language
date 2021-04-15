package model.expressions;
import model.program.heap.HeapInterface;
import model.program.symbdict.SymbolTableInterface;
import model.program.typecheck.TypeTableInterface;
import model.values.Type;
import model.values.Value;
public final class ConstantExpression implements Expression {

    private final Value<?> value;

    public ConstantExpression(Value<?> value) {
        this.value = value;
    }

    @Override
    public Value<?> evaluate(SymbolTableInterface symbols, HeapInterface heap) {
        return this.value;
    }

    @Override
    public String toString() {
        return this.value.toString();
    }

    @Override
    public Type typeCheck(TypeTableInterface typeTable) {
        return value.getType();
    }
}
