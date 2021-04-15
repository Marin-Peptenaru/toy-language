package model.program;

import exception.MyException;
import exception.TypeException;
import exception.VariableUndeclaredException;
import model.program.typecheck.TypeTableInterface;
import model.values.Type;

public class NopStatement implements Statement {

    @Override
    public ProgramState execute(ProgramState currentState) throws MyException {
        return null;
    }

    @Override
    public String toString() {
        return "NOP";
    }

    @Override
    public Type typeCheck(TypeTableInterface typeTable){
        return Type.Void;
    }
}
