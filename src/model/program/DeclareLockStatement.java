package model.program;

import exception.MyException;
import exception.SymbolException;
import exception.TypeException;
import model.expressions.VariableExpression;
import model.program.ProgramState;
import model.program.Statement;
import model.program.typecheck.TypeTableInterface;
import model.values.IntValue;
import model.values.Type;

public class DeclareLockStatement implements Statement {

    private final String varName;

    public DeclareLockStatement(String v) {
        this.varName = v;
    }

    @Override
    public ProgramState execute(ProgramState currentState) throws MyException {
        var stack = currentState.getExecutionStack();
        var symbols = currentState.getSymbolTable();
        var locks = currentState.getLocks();

        Integer lockLocation = locks.newLock();
        symbols.defineVariable(varName, new IntValue(lockLocation));

        return null;
    }

    @Override
    public Type typeCheck(TypeTableInterface typeTable) throws TypeException, SymbolException {
        Type.expectType(typeTable.getVariableType(varName),Type.Int);
        return Type.Void;
    }

    @Override
    public String toString() {
        return "NewLock( " + varName + " )";
    }
}
