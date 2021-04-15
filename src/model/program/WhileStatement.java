package model.program;

import exception.MyException;
import exception.SymbolException;
import exception.TypeException;
import model.expressions.Expression;
import model.program.symbdict.SymbolTableInterface;
import model.program.typecheck.TypeTableInterface;
import model.values.Type;

public class WhileStatement implements Statement {

    private final Expression condition;
    private final Statement statement;

    public WhileStatement(Expression condition, Statement statement) {
        this.condition = condition;
        this.statement = statement;
    }

    @Override
    public ProgramState execute(ProgramState currentState) throws MyException {
        SymbolTableInterface symbols = currentState.getSymbolTable();
        if((Boolean)condition.evaluate(symbols, currentState.getHeap()).getValue()){
            currentState.getExecutionStack().push(this);
            statement.execute(currentState);
        }
        return null;
    }

    @Override
    public Type typeCheck(TypeTableInterface typeTable) throws TypeException, SymbolException {
        Type.expectType(condition.typeCheck(typeTable),Type.Bool);
        statement.typeCheck(typeTable.getCopy());
        return Type.Void;
    }

    @Override
    public String toString() {
        return "While (" + condition + "do " + statement + ")";
    }
}
