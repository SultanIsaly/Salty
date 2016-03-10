package ru.fizteh.fivt.students.isalysultan.JUnit.Interpreter.Database;

import ru.fizteh.fivt.storage.strings.TableProvider;
import ru.fizteh.fivt.students.isalysultan.JUnit.Table.MyTableProvider;

public class CommandRemove extends DataBaseCommand {

    public CommandRemove(String args, int numberArguments, TableProvider tableProvider) {
        super(args, numberArguments, tableProvider);
        setName("use");
    }

    @Override
    public void execute(String[] args) throws IllegalArgumentException {
        if (((MyTableProvider) myTableProvider).getCurrentTable() == null) {
            System.out.println("no table");
            return;
        }
        if (args[1] == null) {
            throw new IllegalArgumentException("uncorrect argument");
        }
        String result = ((MyTableProvider) myTableProvider).getCurrentTable().remove(args[1]);
        if (result == null) {
            System.out.println("not found");
            return;
        } else {
            System.out.println("removed");
        }
    }
}
