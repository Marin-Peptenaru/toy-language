package model.program;

import exception.SymbolException;
import exception.TypeException;
import exception.VariableRedeclaredException;
import exception.VariableUndeclaredException;
import model.program.symbdict.SymbolTableInterface;
import model.program.typecheck.TypeCheckable;
import model.program.typecheck.TypeTableInterface;
import model.values.Type;

public final class VariableDeclarationStatement implements Statement, TypeCheckable {

    private final Type type;
    private final String name;

    public VariableDeclarationStatement(Type type, String name) {
        this.type = type;
        this.name = name;
    }


    @Override
    public ProgramState execute(ProgramState currentState) throws SymbolException {
        SymbolTableInterface variables = currentState.getSymbolTable();
        variables.declareVariable(type, name);
        return null;
    }

    @Override
    public String toString() {
        return type + " " + name;
    }

    @Override
    public Type typeCheck(TypeTableInterface typeTable) throws SymbolException {
        typeTable.declareVariableType(type,name);
        return type;
    }
}
