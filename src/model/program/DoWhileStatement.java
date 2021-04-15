package model.program;

import exception.MyException;
import exception.SymbolException;
import exception.TypeException;
import model.expressions.Expression;
import model.program.typecheck.TypeCheckable;
import model.program.typecheck.TypeTableInterface;
import model.values.Type;

public class DoWhileStatement implements Statement, TypeCheckable {

    private final Expression condition;
    private final Statement statement;

    public DoWhileStatement(Expression condition, Statement statement) {
        this.condition = condition;
        this.statement = statement;
    }


    @Override
    public ProgramState execute(ProgramState currentState) throws MyException {
        statement.execute(currentState);
        if((Boolean)condition.evaluate(currentState.getSymbolTable(), currentState.getHeap()).getValue())
            currentState.getExecutionStack().push(this);
        return null;
    }

    @Override
    public Type typeCheck(TypeTableInterface typeTable) throws TypeException, SymbolException {
        Type.expectType(condition.typeCheck(typeTable),Type.Bool);
        statement.typeCheck(typeTable);
        return Type.Void;
    }

    @Override
    public String toString() {
        return "Do  (" + statement + ") " + "while ( " + condition + ")" ;
    }
}
