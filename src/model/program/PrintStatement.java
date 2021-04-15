package model.program;


import exception.MyException;
import exception.SymbolException;
import exception.TypeException;
import model.expressions.Expression;
import model.program.output.OutputInterface;
import model.program.symbdict.SymbolTableInterface;
import model.program.typecheck.TypeTableInterface;
import model.values.Type;

public final class PrintStatement implements Statement {
    private final Expression expression;

    public PrintStatement(Expression expression) {
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState currentState) throws MyException {
        OutputInterface output = currentState.getOutput();
        SymbolTableInterface variables = currentState.getSymbolTable();
        synchronized(output){
            output.addOutput(expression.evaluate(variables, currentState.getHeap() ).toString());
            return null;
        }
    }

    @Override
    public String toString() {
        return "Print " + expression;
    }

    @Override
    public Type typeCheck(TypeTableInterface typeTable) throws SymbolException, TypeException {
        expression.typeCheck(typeTable);
        return Type.Void;
    }
}
