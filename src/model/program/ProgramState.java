package model.program;
import exception.MyException;
import model.program.exestack.ExecutionStackInterface;
import model.program.filetable.FileTableInterface;
import model.program.heap.HeapInterface;
import model.program.output.OutputInterface;
import model.program.symbdict.SymbolTableInterface;
import model.program.synchronization.LockTableInterface;

import java.util.concurrent.atomic.AtomicInteger;


public final class ProgramState {
    private final Integer id ;
    private final Integer pid;
    private final static AtomicInteger idCounter = new AtomicInteger(1);
    private final ExecutionStackInterface executionStack;
    private final OutputInterface output;
    private final SymbolTableInterface symbolTable;
    private final FileTableInterface fileTable;
    private final HeapInterface heap;
    private final LockTableInterface locks;

    public ProgramState(ExecutionStackInterface executionStack, OutputInterface output, SymbolTableInterface symbolTable, FileTableInterface fileTable, HeapInterface heap, LockTableInterface locks) {
        this.pid = 0;
        this.executionStack = executionStack;
        this.output = output;
        this.symbolTable = symbolTable;
        this.fileTable = fileTable;
        this.heap = heap;
        this.id = idCounter.getAndIncrement();
        this.locks = locks;
    }

    public ProgramState(ExecutionStackInterface executionStack, OutputInterface output, SymbolTableInterface symbolTable, FileTableInterface fileTable, HeapInterface heap, LockTableInterface locks, Integer pid) {
        this.pid = pid;
        this.executionStack = executionStack;
        this.output = output;
        this.symbolTable = symbolTable;
        this.fileTable = fileTable;
        this.heap = heap;
        this.id = idCounter.getAndIncrement();
        this.locks = locks;
    }



    public Boolean isFinished() {
        return executionStack.isEmpty();
    }

    public ExecutionStackInterface getExecutionStack(){
        return executionStack;
    }

    public OutputInterface getOutput(){
        return output;
    }

    public SymbolTableInterface getSymbolTable(){
        return symbolTable;
    }

    public FileTableInterface getFileTable() { return fileTable;}

    public HeapInterface getHeap() { return heap;}

    public LockTableInterface getLocks() { return locks;}

    public ProgramState executeStatement() throws MyException {
        Statement statement = executionStack.pop();
        return statement.execute(this);
    }

    public int getId(){
        return id;
    }

    public Integer getPid() {
        return pid;
    }

    @Override
    public String toString() {
        return "Thread - " + id;
    }

    public String stringState(){
        String programString = "State ID: " + id + "\nVariables\n" + symbolTable + "\n";
        programString += "\nHeap\n" + heap + "\n";
        programString += "\nExecution Stack\n" + executionStack + "\n";
        programString  += "\nOutput\n" + output + "\n";
        programString += "\nFiles\n" + fileTable + "\n";
        programString += "\nLocks\n" + locks + "\n";
        programString += "\n------------------------\n";
        return programString;
    }
}
