package view;

import exception.FileError;
import exception.MyException;
import javafx.beans.property.ReadOnlyMapProperty;
import javafx.beans.property.ReadOnlyMapWrapper;
import javafx.beans.property.SimpleMapProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Menu {
    private final ReadOnlyMapWrapper<String, Command> commands;


    public Menu() {
        this.commands = new ReadOnlyMapWrapper<>(FXCollections.observableHashMap());
    }

    public void addCommand(Command newCommand) throws MyException {
        String key = newCommand.getKey();
        if(commands.containsKey(key))
            throw new MyException("Command already registered inside the menu!");
        commands.put(key, newCommand);
    }
    public ReadOnlyMapProperty<String, Command> getOptionsProperty(){
        return commands.getReadOnlyProperty();
    }

    public ObservableList<Command> getCommands(){
        return FXCollections.observableArrayList(commands.values());
    }

    private void printMenu(){
       commands.values().forEach(c -> System.out.println("\n"+c.getKey()+"\n"+c.getDescription()));
    }

    public void displayMenu() throws MyException {
        Scanner input = new Scanner(System.in);
        while(true){
            printMenu();
            System.out.println("Input command key and press enter:\n");
            String key = input.nextLine();
            if(!commands.containsKey(key)) {
                System.out.println("There is no command with key "+key+" !");
                continue;
            }
            Command inputCommand = commands.get(key);
            try {
                inputCommand.execute();
            }
            catch(RuntimeException e){
                System.out.println(e.getCause() + ": " +e.getMessage());
            }
            catch(FileError e){
                System.out.println("File error\n" + e.getMessage());
            }
            catch(MyException e)
            {
                System.out.println("Program error\n" + e.getMessage());
            }
        }
    }
}
