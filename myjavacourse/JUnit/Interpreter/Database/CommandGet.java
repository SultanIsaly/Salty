package ru.fizteh.fivt.students.isalysultan.JUnit.Interpreter.Database;

import ru.fizteh.fivt.storage.strings.TableProvider;
import ru.fizteh.fivt.students.isalysultan.JUnit.Table.MyTableProvider;

public class CommandGet extends DataBaseCommand {

    public CommandGet(String args, int numberArguments, TableProvider tableProvider) {
        super(args, numberArguments, tableProvider);
        setName("use");
    }

    @Override
    public void execute(String[] args) {
        if (((MyTableProvider) myTableProvider).getCurrentTable() == null) {
            System.out.println("no table");
        }
        String key = args[1];
        String find = ((MyTableProvider) myTableProvider).getCurrentTable().get(key);
        if (find == null) {
            System.out.println("not found");
        } else {
            System.out.println("found");
            System.out.println(find);
        }
    }
}
