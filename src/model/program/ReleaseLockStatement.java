package model.program;

import exception.MyException;
import exception.SymbolException;
import exception.TypeException;
import model.program.synchronization.Lock;
import model.program.typecheck.TypeTableInterface;
import model.values.Type;

public class ReleaseLockStatement implements Statement {

    private String varName ;
    public ReleaseLockStatement(String v) {
        this.varName = v;
    }


    @Override
    public ProgramState execute(ProgramState currentState) throws MyException {
        int pid = currentState.getId();
        var stack = currentState.getExecutionStack();
        var symbols =currentState.getSymbolTable();
        var locks = currentState.getLocks();
        Integer lockLocation = (Integer)symbols.getVariableValue(varName).getValue();
        Lock lock = locks.getLock(lockLocation);
        synchronized (lock){
            if(lock.isHeld(pid))
                lock.release();
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
        return "Unlock( " + varName + " )";
    }
}
