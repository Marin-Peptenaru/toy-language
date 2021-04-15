package model.program.synchronization;

import exception.LockException;
import javafx.beans.property.ReadOnlyMapProperty;

public interface LockTableInterface {
     Integer newLock();
     Lock getLock(Integer location) throws LockException;
     ReadOnlyMapProperty<Integer, Lock> locksProperty();

}
