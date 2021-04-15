package model.program.symbdict;

import exception.SymbolException;
import javafx.beans.property.ReadOnlyMapProperty;
import javafx.beans.property.ReadOnlyMapWrapper;
import javafx.beans.property.SimpleMapProperty;
import javafx.collections.FXCollections;
import model.values.ReferenceValue;
import model.values.Type;
import model.values.Value;

import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

public final class SymbolTable implements SymbolTableInterface {

    private final ReadOnlyMapWrapper<String,Value<?>> symbols = new ReadOnlyMapWrapper<>(FXCollections.observableHashMap());

    public SymbolTable(){ }

    @Override
    public void declareVariable(Type variableType, String variableName) throws SymbolException {
        if(symbols.containsKey(variableName))
            throw new SymbolException("Variable " + variableName + " is already declared!");
       symbols.put(variableName, Type.getDefaultValue(variableType));
    }

    @Override
    public void defineVariable(String variableName, Value<?> newValue) throws SymbolException {
        if(!symbols.containsKey(variableName))
            throw new SymbolException("Variable " + variableName + " is not declared!");
        symbols.put(variableName, newValue);
    }

    @Override
    public boolean checkVariableIsDeclared(String variableName) {
        return symbols.containsKey(variableName);
    }


    @Override
    public Value<?> getVariableValue(String variableName) throws SymbolException {
        if(!symbols.containsKey(variableName))
            throw new SymbolException("Variable " + variableName + " is not declared!");
        return symbols.get(variableName);
    }

    @Override
    public SymbolTableInterface getCopy() {
        SymbolTable copy = new SymbolTable();
        this.symbols.forEach(copy.symbols::put);
        return copy;
    }

    @Override
    public Set<Integer> getAddresses() {
        return symbols.values().stream().filter(v -> v instanceof ReferenceValue)
                .map(v -> ((ReferenceValue) v).getAddress()).collect(Collectors.toSet());
    }

    @Override
    public void updateTable(SymbolTableInterface otherTable) throws SymbolException {
        for(String key: symbols.keySet())
            if(otherTable.checkVariableIsDeclared(key))
                symbols.put(key, otherTable.getVariableValue(key));
    }

    @Override
    public ReadOnlyMapProperty<String,Value<?>> variablesProperty(){
        return symbols.getReadOnlyProperty();
    }

    @Override
    public String toString() {
        StringBuilder tableString = new StringBuilder("{\n");
        Set<Map.Entry<String,Value<?>>> symbolEntries = symbols.entrySet();
        Iterator<Map.Entry<String,Value<?>>> it = symbolEntries.iterator();
        Map.Entry<String,Value<?>> variable;
        while(it.hasNext()){
            variable = it.next();
            tableString.append(variable.getValue().getType()); // append var type
            tableString.append(" ").append(variable.getKey()); // append var name
            tableString.append(" ---> ").append(variable.getValue()); // append var value
            tableString.append("\n");
        }
        tableString.append("}");
        return new String(tableString);
    }


}
