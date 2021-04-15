package model.program.typecheck;
import exception.SymbolException;
import model.values.Type;

public interface TypeTableInterface {
    void declareVariableType(Type type, String variableName) throws SymbolException;
    Type getVariableType(String variableName) throws SymbolException;
    boolean checkVariableDeclared(String variableName);
    TypeTableInterface getCopy();
}
