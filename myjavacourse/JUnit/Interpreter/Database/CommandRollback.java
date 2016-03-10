package ru.fizteh.fivt.students.isalysultan.JUnit.Interpreter.Database;

import ru.fizteh.fivt.storage.strings.TableProvider;
import ru.fizteh.fivt.students.isalysultan.JUnit.Table.MyTableProvider;

public class CommandRollback extends DataBaseCommand {

    public CommandRollback(String args, int numberArguments, TableProvider tableProvider) {
        super(args, numberArguments, tableProvider);
        setName("use");
    }

    @Override
    public void execute(String[] args) {
        if (((MyTableProvider) myTableProvider).getCurrentTable() == null) {
            System.out.println("no table");
            return;
        }
        Integer result = ((MyTableProvider) myTableProvider).getCurrentTable().rollback();
        System.out.println(result);
    }

}
