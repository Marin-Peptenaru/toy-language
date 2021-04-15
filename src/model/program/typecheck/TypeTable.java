package model.program.typecheck;

import exception.SymbolException;
import model.values.Type;
import java.util.Map;
import java.util.HashMap;

public final class TypeTable implements TypeTableInterface {

    private final Map<String,Type> typeMap;

    public TypeTable(){
        typeMap = new HashMap<>();
    }

    private TypeTable(Map<String,Type> map){
        typeMap = map;
    }

    @Override
    public void declareVariableType(Type type, String variableName) throws SymbolException {
        if(typeMap.containsKey(variableName))
            throw new SymbolException("Variable " + variableName + " is already declared!");
        typeMap.put(variableName, type);
    }

    @Override
    public Type getVariableType(String variableName) throws SymbolException {
        if(!typeMap.containsKey(variableName))
            throw new SymbolException("Variable " + variableName + " is not declared!");
        return typeMap.get(variableName);
    }

    @Override
    public boolean checkVariableDeclared(String variableName) {
        return typeMap.containsKey(variableName);
    }

    @Override
    public TypeTableInterface getCopy() {
        Map<String,Type> cloneMap = new HashMap<>();
        for(Map.Entry<String,Type> entry: typeMap.entrySet())
            cloneMap.put(entry.getKey(), entry.getValue());
       return new TypeTable(cloneMap);
    }
}
