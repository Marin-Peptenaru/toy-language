package model.program;

import exception.MyException;
import exception.SymbolException;
import exception.TypeException;
import model.program.exestack.ExecutionStack;
import model.program.exestack.ExecutionStackInterface;
import model.program.symbdict.SymbolTableInterface;
import model.program.typecheck.TypeTableInterface;
import model.values.Type;

public class ForkStatement implements Statement {
    private final Statement threadStatement;

    public ForkStatement(Statement threadStatement) {
        this.threadStatement = threadStatement;
    }

    @Override
    public ProgramState execute(ProgramState currentState) throws MyException {
        ExecutionStackInterface stack = new ExecutionStack(threadStatement);
        SymbolTableInterface symbols = currentState.getSymbolTable().getCopy();
        return new ProgramState(stack, currentState.getOutput(), symbols, currentState.getFileTable(), currentState.getHeap(), currentState.getLocks(), currentState.getId());
    }

    @Override
    public Type typeCheck(TypeTableInterface typeTable) throws TypeException, SymbolException {
        return threadStatement.typeCheck(typeTable.getCopy());
    }

    @Override
    public String toString() {
        return "fork( " + threadStatement + " )";
    }
}
