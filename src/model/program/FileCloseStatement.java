package model.program;


import exception.MyException;
import exception.SymbolException;
import exception.TypeException;
import model.expressions.Expression;
import model.program.typecheck.TypeTableInterface;
import model.values.StringValue;
import model.values.Type;

public class FileCloseStatement implements Statement {
    private final Expression fileName;

    public FileCloseStatement(Expression fileName) {
        this.fileName = fileName;
    }

    @Override
    public ProgramState execute(ProgramState currentState) throws MyException {
        var fileTable = currentState.getFileTable();
        synchronized(fileTable){
            fileTable.closeFile((StringValue)fileName.evaluate(currentState.getSymbolTable(), currentState.getHeap()));
            return null;
        }
    }

    @Override
    public Type typeCheck(TypeTableInterface typeTable) throws TypeException, SymbolException {
        Type.expectType(fileName.typeCheck(typeTable),Type.Str);
        return Type.Void;
    }

    @Override
    public String toString() {
        return "Close " + fileName;
    }
}
