package ru.fizteh.fivt.students.isalysultan.JUnit.Interpreter.Database;

import ru.fizteh.fivt.storage.strings.TableProvider;
import ru.fizteh.fivt.students.isalysultan.JUnit.Table.MyTableProvider;

import java.util.List;

public class CommandList extends DataBaseCommand {

    public CommandList(String args, int numberArguments, TableProvider tableProvider) {
        super(args, numberArguments, tableProvider);
        setName("use");
    }

    @Override
    public void execute(String[] args) {
        if (((MyTableProvider) myTableProvider).getCurrentTable() == null) {
            System.out.println("no table");
            return;
        }
        List<String> keys = ((MyTableProvider) myTableProvider).getCurrentTable().list();
        for (String key : keys) {
            System.out.println("key");
        }
    }
}
