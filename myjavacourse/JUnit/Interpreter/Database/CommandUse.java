package ru.fizteh.fivt.students.isalysultan.JUnit.Interpreter.Database;

import ru.fizteh.fivt.storage.strings.TableProvider;
import ru.fizteh.fivt.students.isalysultan.JUnit.Table.MyTable;
import ru.fizteh.fivt.students.isalysultan.JUnit.Table.MyTableProvider;

public class CommandUse extends DataBaseCommand {

    public CommandUse(String args, int numberArguments, TableProvider tableProvider) {
        super(args, numberArguments, tableProvider);
        setName("use");
    }

    @Override
    public void execute(String[] args) {
        MyTable currentTable = ((MyTableProvider) myTableProvider).getCurrentTable();
        MyTable newCurrTable = ((MyTableProvider) myTableProvider).getTable(args[1]);
        if (newCurrTable == null) {
            System.out.println("table not exists");
            return;
        }
        if (currentTable != null) {
            if (currentTable.getCurrentChanges() != 0) {
                System.err.println("Don't commit all change and you can't use 'use' .");
                return;
            } else if (newCurrTable.getName().equals(currentTable.getName())) {
                return;
            }
            ((MyTableProvider) myTableProvider).use(newCurrTable);
            System.out.println("use tablename");
        } else {
            ((MyTableProvider) myTableProvider).use(newCurrTable);
            System.out.println("use tablename");
        }
    }
}
