package model.program.heap;

import exception.MemoryException;
import javafx.beans.property.ReadOnlyMapProperty;
import model.expressions.Expression;
import model.values.ReferenceValue;
import model.values.Type;
import model.values.Value;

import java.util.Set;

public interface HeapInterface {
    ReferenceValue allocateMemory(Type addressType);
    void writeAtLocation(int address, Value<?> value) throws MemoryException;
    Value<?> readFromLocation(int address) throws MemoryException;
    void collectGarbage(Set<Integer> symbolTableAddresses);
    void clear();
    ReadOnlyMapProperty<Integer, Value<?>> memoryProperty();
}
