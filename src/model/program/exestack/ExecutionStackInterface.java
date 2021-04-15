package model.program.exestack;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import model.program.Statement;
import model.program.typecheck.TypeCheckable;

import java.util.Deque;

public interface ExecutionStackInterface extends TypeCheckable {
    Statement pop();
    void push(Statement statement);
    boolean isEmpty();
    public String toString();
    public ReadOnlyListProperty<Statement> statementStackProperty();

}
