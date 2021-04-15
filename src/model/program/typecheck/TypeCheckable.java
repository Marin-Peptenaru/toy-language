package model.program.typecheck;

import exception.TypeException;
import exception.VariableRedeclaredException;
import exception.VariableUndeclaredException;
import exception.SymbolException;
import model.values.Type;

public interface TypeCheckable {
    Type typeCheck(TypeTableInterface typeTable) throws TypeException, SymbolException;
}
