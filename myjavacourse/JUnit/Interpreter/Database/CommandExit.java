package ru.fizteh.fivt.students.isalysultan.JUnit.Interpreter.Database;

import ru.fizteh.fivt.storage.strings.TableProvider;
import ru.fizteh.fivt.students.isalysultan.JUnit.Table.MyTableProvider;

import java.io.IOException;

public class CommandExit extends DataBaseCommand {
    public CommandExit(String args, int numberArguments, TableProvider tableProvider) {
        super(args, numberArguments, tableProvider);
        setName("use");
    }

    @Override
    public void execute(String[] args) {
        if (((MyTableProvider) myTableProvider).getCurrentTable() != null) {
            try {
                ((MyTableProvider) myTableProvider).getCurrentTable().write();
            } catch (IOException e) {
                System.err.println("Error in write table.");
            }
        }
        System.exit(0);
    }
}
