package ru.fizteh.fivt.students.isalysultan.JUnit.Interpreter.Database;

import ru.fizteh.fivt.storage.strings.TableProvider;
import ru.fizteh.fivt.students.isalysultan.JUnit.Exception.WrongArgumentException;
import ru.fizteh.fivt.students.isalysultan.JUnit.Table.MyTableProvider;

public class CommandDrop extends DataBaseCommand {

    public CommandDrop(String args, int numberArguments, TableProvider tableProvider) {
        super(args, numberArguments, tableProvider);
        setName("use");
    }

    @Override
    public void execute(String[] args) {
        String nameTable = args[1];
        if (nameTable == null) {
            throw new WrongArgumentException();
        }
        ((MyTableProvider) myTableProvider).removeTable(nameTable);
        System.out.println("dropped");
    }
}
