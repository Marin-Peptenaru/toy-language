package repo;

import exception.MyException;
import model.program.ProgramState;

import java.io.IOException;
import java.util.List;

public interface Repository {
    void logProgramState(ProgramState state) throws MyException;
    List<ProgramState> getProgramList();
    void setProgramList(List<ProgramState> programs);
    String getLogFile();
    void setLogFile(String newFile);
}
