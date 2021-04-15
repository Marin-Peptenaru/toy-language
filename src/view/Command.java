package view;

import exception.MyException;

public abstract class Command {
    protected final String key;
    protected final String description;

    protected Command(String key, String description) {
        this.key = key;
        this.description = description;
    }

    public abstract void execute() throws MyException;

    public String getKey(){
        return key;
    }

    public String getDescription(){
        return description;
    }

    @Override
    public String toString() {
        return "Program " + key ;
    }
}
