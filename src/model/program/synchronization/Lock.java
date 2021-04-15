package model.program.synchronization;


import java.util.Deque;
import java.util.LinkedList;


public class Lock {

    private Integer owner  = -1;

    public Boolean isHeld(){return owner != -1;}
    public Boolean isHeld(Integer pid){
        return owner.equals(pid);
    }

    public void acquire(Integer pid){
        owner = pid;
    }
    public void release(){
        owner = -1 ;
    }

    @Override
    public String toString() {
        return owner == -1 ? "Free lock" : "Lock owned by Thread " + owner;
    }
}
