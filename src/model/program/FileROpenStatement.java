package model.program;

import exception.MyException;
import exception.SymbolException;
import exception.TypeException;
import model.expressions.Expression;
import model.program.filetable.FileTableInterface;
import model.program.typecheck.TypeTableInterface;
import model.values.StringValue;
import model.values.Type;

public class FileROpenStatement implements Statement{
    private final Expression fileName;

    public FileROpenStatement(Expression fileName) {
        this.fileName = fileName;
    }

    @Override
    public ProgramState execute(ProgramState currentState) throws MyException {
        FileTableInterface files = currentState.getFileTable();
        synchronized(files){
            files.openFile((StringValue)fileName.evaluate(currentState.getSymbolTable(), currentState.getHeap()));
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
        return "Open " + fileName;
    }
}
