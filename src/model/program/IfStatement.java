package model.program;

import exception.MyException;
import exception.SymbolException;
import exception.TypeException;
import model.expressions.Expression;
import model.program.symbdict.SymbolTableInterface;
import model.program.typecheck.TypeTableInterface;
import model.values.Type;

public final class IfStatement implements Statement {
    private final Expression condition;
    private final Statement thenBranch;
    private final Statement elseBranch;

    public IfStatement(Expression condition, Statement thenBranch){
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch  = new NopStatement();
    }

    public IfStatement(Expression condition, Statement thenBranch, Statement elseBranch) {
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }

    @Override
    public ProgramState execute(ProgramState currentState) throws MyException{
        if((Boolean)condition.evaluate(currentState.getSymbolTable(), currentState.getHeap()).getValue())
            currentState.getExecutionStack().push(thenBranch);
        else
            currentState.getExecutionStack().push(elseBranch);
        return null;
    }

    @Override
    public String toString() {
        String ifString = "If " + condition  + " then: " + thenBranch;
        if(!(elseBranch instanceof NopStatement))
            ifString += "; else: " + elseBranch;
        return ifString;
    }

    @Override
    public Type typeCheck(TypeTableInterface typeTable) throws TypeException, SymbolException {
        Type.expectType(condition.typeCheck(typeTable),Type.Bool);
        thenBranch.typeCheck(typeTable.getCopy());
        elseBranch.typeCheck(typeTable.getCopy());
        return Type.Void;
    }
}
