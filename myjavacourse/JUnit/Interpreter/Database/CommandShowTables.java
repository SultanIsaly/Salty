package ru.fizteh.fivt.students.isalysultan.JUnit.Interpreter.Database;

import ru.fizteh.fivt.storage.strings.TableProvider;
import ru.fizteh.fivt.students.isalysultan.JUnit.Table.MyTableProvider;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class CommandShowTables extends DataBaseCommand {

    public CommandShowTables(String args, int numberArguments, TableProvider tableProvider) {
        super(args, numberArguments, tableProvider);
        setName("use");
    }

    @Override
    public void execute(String[] args) {
        if (!args[1].equals("tables")) {
            System.out.println("Command is not recognized");
            return;
        }
        try {
            Map<String, Integer> result = ((MyTableProvider) myTableProvider).showTables();
            Set<String> keys = result.keySet();
            for (String key : keys) {
                System.out.println(key + " " + result.get(key));
            }
        } catch (IOException e) {
            System.err.println("Error in tableList.get().");
        }
    }
}
