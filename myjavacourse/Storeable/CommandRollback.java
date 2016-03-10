package ru.fizteh.fivt.students.isalysultan.Storeable;

public class CommandRollback {
    public static int rollback(MyTable table) {
        int result = table.getCommitChanges();
        table.filesRollback();
        return result;
    }
}
