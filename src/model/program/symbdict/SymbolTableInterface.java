package model.program.symbdict;


import exception.SymbolException;
import javafx.beans.property.ReadOnlyMapProperty;
import model.values.Type;
import model.values.Value;

import java.util.Set;

public interface SymbolTableInterface{
    void declareVariable(Type variableType, String variableName) throws SymbolException;
    void defineVariable(String variableName, Value<?> newValue) throws SymbolException;
    boolean checkVariableIsDeclared(String variableName);
    Value<?> getVariableValue(String variableName) throws SymbolException;
    String toString();
    SymbolTableInterface getCopy();
    Set<Integer> getAddresses();
    void updateTable(SymbolTableInterface otherTable) throws SymbolException;
    ReadOnlyMapProperty<String,Value<?>> variablesProperty();

}
