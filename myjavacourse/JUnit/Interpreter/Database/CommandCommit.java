package ru.fizteh.fivt.students.isalysultan.JUnit.Interpreter.Database;

import ru.fizteh.fivt.storage.strings.TableProvider;
import ru.fizteh.fivt.students.isalysultan.JUnit.Table.MyTableProvider;

public class CommandCommit extends DataBaseCommand {

    public CommandCommit(String args, int numberArguments, TableProvider tableProvider) {
        super(args, numberArguments, tableProvider);
        setName("use");
    }

    @Override
    public void execute(String[] args) {
        if (((MyTableProvider) myTableProvider).getCurrentTable() == null) {
            System.out.println("no table");
        }
        int result = ((MyTableProvider) myTableProvider).getCurrentTable().commit();
        System.out.println(result);
    }
}
