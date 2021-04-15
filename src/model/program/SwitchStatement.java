package model.program;

import exception.MyException;
import exception.SymbolException;
import exception.TypeException;
import model.expressions.ConstantExpression;
import model.expressions.Expression;
import model.program.typecheck.TypeCheckable;
import model.program.typecheck.TypeTableInterface;
import model.values.Type;
import model.values.Value;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class SwitchStatement implements Statement, TypeCheckable {

    public static class Case{
        private final ConstantExpression value;
        private final Statement branch;

        public Case(ConstantExpression value, Statement branch) {
            this.value = value;
            this.branch = branch;
        }

        public Expression getValue() {
            return value;
        }

        public Statement getBranch() {
            return branch;
        }
    }

    private Expression value;
    private List<Case> cases = new ArrayList<>();
    private Statement defaultCase = null;

    public SwitchStatement(Expression value, Case... cases){
        this.value = value;
        this.cases.addAll(Arrays.asList(cases));
    }


    public SwitchStatement(Expression value, Statement defaultCase, Case... cases) {
        this.value = value;
        this.cases.addAll(Arrays.asList(cases));
        this.defaultCase = defaultCase;
    }

    @Override
    public ProgramState execute(ProgramState currentState) throws MyException {

        boolean matched = false;
        var stack = currentState.getExecutionStack();
        var variables = currentState.getSymbolTable();
        var heap = currentState.getHeap();
            Value<?> v = value.evaluate(variables,heap);
            for (Case branch : cases) {
                if (branch.getValue().evaluate(variables, heap).equals(v)) {
                    matched = true;
                    stack.push(branch.getBranch());
                    break;
                }
            }

        if(!matched && defaultCase != null)
            stack.push(defaultCase);
        return null;
    }

    @Override
    public Type typeCheck(TypeTableInterface typeTable) throws TypeException, SymbolException {
        Type valueType = value.typeCheck(typeTable);
        for(Case branch: cases){
            Type.expectType(branch.getValue().typeCheck(typeTable),valueType);
            branch.getBranch().typeCheck(typeTable);
        }
        if(defaultCase != null){
            defaultCase.typeCheck(typeTable);
        }
        return Type.Void;
    }

    @Override
    public String toString() {
        StringBuilder switchStr = new StringBuilder("Switch " + value +":\n");
        for(Case branch: cases){
            switchStr.append("Case ").append(branch.getValue()).append(":\n\t").append(branch.getBranch()).append("\n");
        }
        if(defaultCase != null)
            switchStr.append("Default:\n\t ").append(defaultCase).append("\n");
        return switchStr.toString();
    }
}
