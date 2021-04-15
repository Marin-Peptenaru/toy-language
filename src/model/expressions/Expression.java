package model.expressions;
import exception.ExpressionException;
import exception.MyException;
import exception.SymbolException;
import model.program.heap.HeapInterface;
import model.program.typecheck.TypeCheckable;
import model.values.Value;
import model.program.symbdict.SymbolTableInterface;

public interface Expression extends  TypeCheckable {
    Value<?> evaluate(SymbolTableInterface symbols, HeapInterface heap) throws MyException;
}
