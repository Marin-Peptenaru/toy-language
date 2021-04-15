package view;

import exception.MyException;
import model.program.ProgramState;

public class RunExampleCommand extends Command {

    protected final ProgramState initialProgramState;

    public RunExampleCommand(String key, ProgramState prg){
        super(key, prg.getExecutionStack().toString());
        initialProgramState = prg;
    }



    @Override
    public void execute() throws MyException {

    }

}
