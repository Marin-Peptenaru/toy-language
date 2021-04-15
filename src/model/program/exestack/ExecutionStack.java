package model.program.exestack;

import exception.MyException;
import exception.SymbolException;
import exception.TypeException;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.program.NopStatement;
import model.program.Statement;
import model.program.typecheck.TypeTableInterface;
import model.values.Type;
import java.util.List;
import java.util.*;


public final class ExecutionStack implements ExecutionStackInterface{

    private final ReadOnlyListWrapper<Statement> statementStack;

    public ExecutionStack(Statement... statements) {
        ObservableList<Statement> l = FXCollections.observableArrayList(Arrays.asList(statements));
        Collections.reverse(l);
        statementStack = new ReadOnlyListWrapper<>(l);
    }

    @Override
    public Statement pop() {
        if(statementStack.get().isEmpty())
            throw new EmptyStackException();
        return statementStack.remove(statementStack.size() -1);
    }

    @Override
    public void push(Statement statement) {
        statementStack.add(statement);
    }

    @Override
    public boolean isEmpty() {
        return statementStack.get().isEmpty();
    }

    @Override
    public ReadOnlyListProperty<Statement> statementStackProperty(){
        return statementStack.getReadOnlyProperty();
    }

    @Override
    public String toString(){
        StringBuilder stackString = new StringBuilder();
        ListIterator<Statement> it = statementStack.listIterator(statementStack.size());
        Statement statement;
        while(it.hasPrevious()) {
            statement = it.previous();
            stackString.append(statement).append('\n');
        }
        return new String(stackString);
    }

    @Override
    public Type typeCheck(TypeTableInterface typeTable) throws TypeException, SymbolException {
        ListIterator<Statement> statementIterator = statementStack.listIterator(statementStack.size());
        StringBuilder typecheckingErrors = new StringBuilder();
        Statement currentStatement = new NopStatement(); // initialisation so that java doesn't complain on line 60
        while(statementIterator.hasPrevious()){
            try{
                currentStatement = statementIterator.previous();
                currentStatement.typeCheck(typeTable);
            }
            catch (TypeException e){
                typecheckingErrors.append("Typing error at statement: ").append(currentStatement);
                typecheckingErrors.append("\n").append(e.getMessage()).append("\n\t-----\t\n");
            }
            catch (SymbolException e){
                typecheckingErrors.append("Symbol error at statement: ").append(currentStatement);
                typecheckingErrors.append("\n").append(e.getMessage()).append("\n\t-----\t\n");
            }
        }

        if(typecheckingErrors.length() > 0 )
            throw new TypeException(new String(typecheckingErrors));
        return Type.Void;
    }
}
