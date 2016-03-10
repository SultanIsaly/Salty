package ru.fizteh.fivt.students.isalysultan.JUnit.Interpreter;

public abstract class Command {
    protected String name;

    public String getName() {
        return name;
    }

    public void setName(String nameCommand) {
        name = nameCommand;
    }

    public void run() {

    }

    public void execute(String[] args) {
    }

    ;

}
