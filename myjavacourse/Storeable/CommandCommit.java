package ru.fizteh.fivt.students.isalysultan.Storeable;

public class CommandCommit {
    public static int commit(MyTable table) {
        int result = table.getCurrentChanges();
        table.saveTable();
        return result;
    }
}
