package ru.fizteh.fivt.students.isalysultan.JUnit.Interpreter.Database;

import ru.fizteh.fivt.storage.strings.TableProvider;
import ru.fizteh.fivt.students.isalysultan.JUnit.Table.MyTableProvider;

public class CommandPut extends DataBaseCommand {

    public CommandPut(String args, int numberArguments, TableProvider tableProvider) {
        super(args, numberArguments, tableProvider);
        setName("use");
    }

    @Override
    public void execute(String[] args) {
        if ((((MyTableProvider) myTableProvider).getCurrentTable() == null)) {
            System.out.println("no table");
            return;
        }
        String result = (((MyTableProvider) myTableProvider).getCurrentTable().put(args[1], args[2]));
        if (result != null) {
            System.out.println("overwrite");
            System.out.println(result);
        } else {
            System.out.println("new");
        }
    }

}
