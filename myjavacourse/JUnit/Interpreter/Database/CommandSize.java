package ru.fizteh.fivt.students.isalysultan.JUnit.Interpreter.Database;

import ru.fizteh.fivt.storage.strings.TableProvider;
import ru.fizteh.fivt.students.isalysultan.JUnit.Table.MyTableProvider;

public class CommandSize extends DataBaseCommand {

    public CommandSize(String args, int numberArguments, TableProvider tableProvider) {
        super(args, numberArguments, tableProvider);
        setName("use");
    }

    @Override
    public void execute(String[] args) {
        if (((MyTableProvider) myTableProvider).getCurrentTable() == null) {
            System.out.println("no table");
            return;
        }
        int result = ((MyTableProvider) myTableProvider).getCurrentTable().size();
        System.out.println(result);
    }
}
