package model.program.output;

import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedDeque;

public final class Output implements OutputInterface {

    private final ReadOnlyListWrapper<String> outputBuffer = new ReadOnlyListWrapper<>(FXCollections.observableArrayList());

    @Override
    public void addOutput(String... outputString) {
        outputBuffer.addAll(Arrays.asList(outputString));
    }

    @Override
    public ReadOnlyListProperty<String> outputProperty() {
        return outputBuffer.getReadOnlyProperty();
    }

    public String toString(){
        StringBuilder outputString = new StringBuilder();
        for (String s : outputBuffer) {
            outputString.append(s).append("\n");
        }
        return new String(outputString);
    }
}
