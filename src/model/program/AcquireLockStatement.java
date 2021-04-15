package model.program;

import exception.MyException;
import exception.SymbolException;
import exception.TypeException;
import model.program.synchronization.Lock;
import model.program.typecheck.TypeTableInterface;
import model.values.IntValue;
import model.values.Type;

public class AcquireLockStatement implements Statement {


    private final String varName;


    public AcquireLockStatement(String v){
        this.varName = v;
    }


    @Override
    public ProgramState execute(ProgramState currentState) throws MyException {
        var stack = currentState.getExecutionStack();
        var symbols = currentState.getSymbolTable();
        var locks = currentState.getLocks();

        int pid = currentState.getId();
        Integer varValue = ((IntValue)symbols.getVariableValue(varName)).getValue();
        Lock lock = locks.getLock(varValue);
        synchronized(lock){
            if(!lock.isHeld())
                lock.acquire(pid);
            else
                stack.push(this);
        }
        return null;
    }

    @Override
    public Type typeCheck(TypeTableInterface typeTable) throws TypeException, SymbolException {
        Type.expectType(typeTable.getVariableType(varName), Type.Int);
        return Type.Void;
    }

    @Override
    public String toString() {
        return "Lock( " + varName + " )";
    }
}
