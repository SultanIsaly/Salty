package ru.fizteh.fivt.students.isalysultan.Storeable;

import ru.fizteh.fivt.storage.structured.Storeable;

public class CommandRemove {
    public static Storeable remove(String key, File tables, MyTable data) {
        Storeable result = tables.removeMap(key);
        if (result == null) {
            System.out.println("not found");
            return null;
        } else {
            System.out.println("removed");
            data.decrementChanges();
            data.dicrementNumberRecords();
            return result;
        }
    }
}
