package model.program;

import exception.MyException;
import exception.SymbolException;
import exception.TypeException;
import model.expressions.Expression;
import model.program.typecheck.TypeCheckable;
import model.program.typecheck.TypeTableInterface;
import model.values.Type;

public class ForStatement implements Statement, TypeCheckable {

    private Boolean firstStep = true;
    private Expression condition;
    private Statement initialStep;
    private Statement nextStep;
    private Statement body;

    public ForStatement(Expression condition, Statement initialStep, Statement nextStep, Statement body) {
        this.condition = condition;
        this.initialStep = initialStep;
        this.nextStep = nextStep;
        this.body = body;
    }

    @Override
    public ProgramState execute(ProgramState currentState) throws MyException {
        var stack = currentState.getExecutionStack();
        if(firstStep){
            firstStep = false;
            stack.push(this);
            stack.push(initialStep);
        } else if((Boolean)condition.evaluate(currentState.getSymbolTable(), currentState.getHeap()).getValue()){
            stack.push(this);
            stack.push(nextStep);
            stack.push(body);
        }
        return null;
    }

    @Override
    public Type typeCheck(TypeTableInterface typeTable) throws TypeException, SymbolException {
        initialStep.typeCheck(typeTable);
        Type.expectType(condition.typeCheck(typeTable),Type.Bool);
        nextStep.typeCheck(typeTable);
        return Type.Void;
    }

    @Override
    public String toString() {
        return "For " + initialStep + ", " + condition + ", " + nextStep + ":\n\t" + body;
    }
}
