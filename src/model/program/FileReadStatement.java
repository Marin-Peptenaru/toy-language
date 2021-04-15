package model.program;

import exception.FileError;
import exception.MyException;
import exception.SymbolException;
import exception.TypeException;
import model.expressions.Expression;
import model.program.typecheck.TypeTableInterface;
import model.values.IntValue;
import model.values.StringValue;
import model.values.Type;

import java.io.IOException;

public class FileReadStatement implements Statement {
    private final Expression fileName;
    private final String variableName;

    public FileReadStatement(Expression fileName, String variableName) {
        this.fileName = fileName;
        this.variableName = variableName;
    }

    @Override
    public ProgramState execute(ProgramState currentState) throws MyException {
        var fileTable = currentState.getFileTable();
        var symbolTable = currentState.getSymbolTable();
        synchronized(fileTable){
            var fileDescriptor = fileTable.getFileDescriptor((StringValue)fileName.evaluate(symbolTable, currentState.getHeap()));
            try{

                String stringInt = fileDescriptor.readLine();

                if(stringInt != null){
                    symbolTable.defineVariable(variableName,new IntValue(Integer.parseInt(stringInt)));
                }else{
                    symbolTable.defineVariable(variableName,Type.getDefaultValue(Type.Int));
                }

            }catch(IOException e){
                throw new FileError("Error reading variable" + variableName + " from file.\n");
            }
            return null;
        }
    }

    @Override
    public Type typeCheck(TypeTableInterface typeTable) throws TypeException, SymbolException {
        Type.expectType(fileName.typeCheck(typeTable),Type.Str);
        Type.expectType(typeTable.getVariableType(variableName),Type.Int);
        return Type.Void;
    }

    @Override
    public String toString() {
        return "Read( " + fileName + ", " + variableName + " )";
    }
}
