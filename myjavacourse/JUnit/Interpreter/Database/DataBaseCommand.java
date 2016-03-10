package ru.fizteh.fivt.students.isalysultan.JUnit.Interpreter.Database;

import ru.fizteh.fivt.storage.strings.TableProvider;
import ru.fizteh.fivt.students.isalysultan.JUnit.Exception.WrongArgumentException;
import ru.fizteh.fivt.students.isalysultan.JUnit.Interpreter.Command;

public class DataBaseCommand extends Command {
    protected int numberArguments;
    protected TableProvider myTableProvider;

    public DataBaseCommand(String args, int numArg, TableProvider tableProvider) {
        numberArguments = numArg;
        myTableProvider = tableProvider;
        setName(args);
    }

    public void run(String[] args, int numberArg) {
        if (numberArg != numberArguments) {
            throw new WrongArgumentException();
        }
        execute(args);
    }

    @Override
    public void execute(String[] args) {

    }

}
