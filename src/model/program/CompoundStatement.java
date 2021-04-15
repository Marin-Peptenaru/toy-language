package model.program;

import exception.MyException;
import exception.SymbolException;
import exception.TypeException;
import exception.VariableUndeclaredException;
import model.program.exestack.ExecutionStackInterface;
import model.program.typecheck.TypeTableInterface;
import model.values.Type;

import java.io.IOException;

public final class CompoundStatement implements Statement {

    private final Statement firstStatement;
    private final Statement secondStatement;

    public CompoundStatement(Statement firstStatement, Statement secondStatement) {
        this.firstStatement = firstStatement;
        this.secondStatement = secondStatement;
    }



    @Override
    public ProgramState execute(ProgramState currentState) throws MyException {
        currentState.getExecutionStack().push(secondStatement);
        currentState.getExecutionStack().push(firstStatement);
        return null;
    }

    @Override
    public String toString() {
        return firstStatement + " | " + secondStatement;
    }

    @Override
    public Type typeCheck(TypeTableInterface typeTable) throws TypeException, SymbolException {
        firstStatement.typeCheck(typeTable);
        secondStatement.typeCheck(typeTable);
        return Type.Void;
    }
}
