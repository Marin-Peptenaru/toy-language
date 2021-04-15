package repo;

import exception.FileError;
import exception.MyException;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import model.program.ProgramState;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class ProgramRepository implements Repository{
    private final SimpleListProperty<ProgramState> programs = new SimpleListProperty<>(FXCollections.observableArrayList());
    private String logFile;
    private boolean clearFile;

    public ProgramRepository(ProgramState initialState){
        programs.add(initialState);
        this.logFile = logFile;
        clearFile = true;
    }


    public ProgramRepository(ProgramState initialState, String logFile) {
        programs.add(initialState);
        this.logFile = logFile;
        clearFile = true;
    }

    @Override
    public String getLogFile() {
        return logFile;
    }

    @Override
    public void setLogFile(String newFile){
        this.logFile = newFile;
        this.clearFile = true;
    }

    @Override
    public void logProgramState(ProgramState state) throws MyException {
        try{
            if(clearFile){
                var fileCleaner = new PrintWriter(new BufferedWriter(new FileWriter(logFile)));
                fileCleaner.write("");
                fileCleaner.close();
                clearFile = false;
            }
            var logWriter = new PrintWriter(new BufferedWriter(new FileWriter(logFile,true)));
            logWriter.write("\n" +state.stringState()+ "\n");
            logWriter.close();
        }catch(IOException e){
            throw new FileError("Could not log program state to file");
        }
    }

    @Override
    public List<ProgramState> getProgramList() {
        return programs;
    }

    public ReadOnlyListProperty<ProgramState> programListProperty(){
        return programs;
    }

    @Override
    public void setProgramList(List<ProgramState> newPrograms) {
            programs.setValue(FXCollections.observableArrayList(newPrograms));
    }
}
