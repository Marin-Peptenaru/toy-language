package model.program.synchronization;

import exception.LockException;
import javafx.beans.property.ReadOnlyMapProperty;
import javafx.beans.property.ReadOnlyMapWrapper;
import javafx.collections.FXCollections;
import model.values.Value;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class LockTable implements LockTableInterface {

    private final AtomicInteger locationCounter = new AtomicInteger(0);
    private final ReadOnlyMapWrapper<Integer, Lock> locks = new ReadOnlyMapWrapper<>(FXCollections.observableHashMap());;

    @Override
    public synchronized Integer newLock() {
        int lockLocation = locationCounter.incrementAndGet();
        locks.put(lockLocation, new Lock());
        return lockLocation;
    }

    @Override
    public synchronized Lock getLock(Integer location) throws LockException {
        if(locks.containsKey(location))
            return locks.get(location);
       throw new LockException("No Lock object allocated at location " + location + " !");
    }

    public ReadOnlyMapProperty<Integer, Lock> locksProperty() {
        return locks.getReadOnlyProperty();
    }

    @Override
    public String toString() {
        StringBuilder tableString = new StringBuilder("{\n");
        Set<Map.Entry<Integer, Lock>> lockEntries = locks.entrySet();
        Iterator<Map.Entry<Integer,Lock>> it = lockEntries.iterator();
        Map.Entry<Integer, Lock> content;
        while(it.hasNext()){
            content = it.next();

            tableString.append("@").append(content.getKey()); // append var name
            tableString.append(" ---> ").append(content.getValue()); // append var value
            tableString.append("\n");
        }
        tableString.append("}");
        return new String(tableString);
    }
}
