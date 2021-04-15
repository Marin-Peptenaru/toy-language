package model.program;

import exception.MyException;
import model.program.typecheck.TypeCheckable;

import java.io.IOException;

public interface Statement extends TypeCheckable {
    ProgramState execute(ProgramState currentState) throws MyException;
    String toString();
}
