package model.program.filetable;

import exception.FileError;
import javafx.beans.property.ReadOnlyMapProperty;
import model.values.StringValue;

import java.io.BufferedReader;
import java.io.IOException;

public interface FileTableInterface {
    void openFile(StringValue fileName) throws FileError;
    BufferedReader getFileDescriptor(StringValue fileName) throws FileError;
    void closeFile(StringValue fileName) throws FileError;
    ReadOnlyMapProperty<StringValue,BufferedReader> filesProperty();
}
