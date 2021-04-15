package model.program.output;

import javafx.beans.property.ReadOnlyListProperty;

public interface OutputInterface {
    void addOutput(String... outputString);
    ReadOnlyListProperty<String> outputProperty();
    public String toString();
}
