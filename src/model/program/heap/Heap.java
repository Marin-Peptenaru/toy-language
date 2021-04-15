package model.program.heap;

import exception.MemoryException;
import javafx.beans.property.ReadOnlyMapProperty;
import javafx.beans.property.ReadOnlyMapWrapper;
import javafx.beans.property.SimpleMapProperty;
import javafx.collections.FXCollections;
import model.values.ReferenceValue;
import model.values.Type;
import model.values.Value;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Heap implements HeapInterface {
    private final ReadOnlyMapWrapper<Integer, Value<?>> memory = new ReadOnlyMapWrapper<>(FXCollections.observableHashMap());
    private final AtomicInteger addressCount = new AtomicInteger(1);

    public Heap(){ }

    @Override
    public ReferenceValue allocateMemory(Type addressType) {
        int address = addressCount.getAndIncrement();
        memory.put(address,Type.getDefaultValue(addressType));
        return new ReferenceValue(address, addressType);
    }

    @Override
    public void writeAtLocation(int address, Value<?> value) throws MemoryException {
        if(!memory.containsKey(address))
            throw new MemoryException("Address " + address + " is not accessible!");
        memory.put(address,value);
    }

    @Override
    public Value<?> readFromLocation(int address) throws MemoryException {
        if(address == 0)
            throw new MemoryException("Invalid address 0!");
        if(!memory.containsKey(address))
            throw new MemoryException("Memory not allocated at address " + address + "!");
        return memory.get(address);
    }

    @Override
    public void collectGarbage(Set<Integer> symbolTableAddresses) {
        Set<Integer> heapAddresses = memory.values().stream().filter(v -> v instanceof ReferenceValue)
                .map(v -> ((ReferenceValue)v).getAddress()).collect(Collectors.toSet());
        memory.keySet().removeIf(address -> !(symbolTableAddresses.contains(address) || heapAddresses.contains(address)));
    }

    @Override
    public ReadOnlyMapProperty<Integer, Value<?>> memoryProperty(){
        return memory.getReadOnlyProperty();
    }

    @Override
    public void clear() {
        memory.clear();
    }

    @Override
    public String toString() {
        StringBuilder tableString = new StringBuilder("{\n");
        Set<Map.Entry<Integer,Value<?>>> memoryAddresses = memory.entrySet();
        Iterator<Map.Entry<Integer,Value<?>>> it = memoryAddresses.iterator();
        Map.Entry<Integer,Value<?>> content;
        while(it.hasNext()){
            content = it.next();

            tableString.append("@").append(content.getKey()); // append var name
            tableString.append(" ").append(content.getValue().getType()); // append var type
            tableString.append(" ---> ").append(content.getValue()); // append var value
            tableString.append("\n");
        }
        tableString.append("}");
        return new String(tableString);
    }
}
