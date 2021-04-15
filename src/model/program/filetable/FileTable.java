package model.program.filetable;

import exception.FileError;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyMapProperty;
import javafx.beans.property.ReadOnlyMapWrapper;
import javafx.beans.property.SimpleMapProperty;
import javafx.collections.FXCollections;
import model.values.StringValue;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FileTable implements FileTableInterface{

    private final ReadOnlyMapWrapper<StringValue,BufferedReader> files;

    public FileTable() {
        this.files = new ReadOnlyMapWrapper<>(FXCollections.observableHashMap());
    }

    @Override
    public void openFile(StringValue fileName) throws FileError {
        if(files.containsKey(fileName))
            throw new FileError("File " + fileName + "is already open!");
        try{
            files.put(fileName,new BufferedReader(new FileReader(fileName.getValue())));
        }catch(FileNotFoundException e){
            throw new FileError("File " + fileName + "not found!");
        }
    }

    @Override
    public BufferedReader getFileDescriptor(StringValue fileName) throws FileError {
        if(!files.containsKey(fileName))
            throw new FileError("File " + fileName + "is not open!");
        return files.get(fileName);
    }

    @Override
    public void closeFile(StringValue fileName) throws FileError {
        if(!files.containsKey(fileName))
            throw new FileError("File " + fileName + "is not open!");
        try {
            files.get(fileName).close();
        }catch(IOException e){
            throw new FileError("Error closing file " + fileName  + "!");
        }
        files.remove(fileName);
    }

    @Override
    public ReadOnlyMapProperty<StringValue,BufferedReader> filesProperty(){
        return files.getReadOnlyProperty();
    }

    @Override
    public String toString() {
        StringBuilder filesString = new StringBuilder();
        for(StringValue fileName: files.keySet())
            filesString.append(fileName.getValue()).append('\n');
        return new String(filesString);
    }
}
