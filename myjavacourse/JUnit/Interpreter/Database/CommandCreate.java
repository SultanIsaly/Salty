package ru.fizteh.fivt.students.isalysultan.JUnit.Interpreter.Database;

import ru.fizteh.fivt.storage.strings.TableProvider;
import ru.fizteh.fivt.students.isalysultan.JUnit.Table.MyTable;
import ru.fizteh.fivt.students.isalysultan.JUnit.Table.MyTableProvider;

public class CommandCreate extends DataBaseCommand {

    public CommandCreate(String args, int numberArguments, TableProvider tableProvider) {
        super(args, numberArguments, tableProvider);
        setName("use");
    }

    @Override
    public void execute(String[] args) {
        String nameTable = args[1];
        if (nameTable == null) {
            throw new IllegalArgumentException("Argument not must null.");
        }
        MyTable result = ((MyTableProvider) myTableProvider).createTable(nameTable);
        if (result != null) {
            System.out.println("table create");
        } else {
            System.out.println("tablename exists");
        }
    }
}
