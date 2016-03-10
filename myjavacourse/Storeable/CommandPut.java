package ru.fizteh.fivt.students.isalysultan.Storeable;

import ru.fizteh.fivt.storage.structured.Storeable;

public class CommandPut {
    public static Storeable put(String key, Storeable value, File tables,
                                MyTable mainTable) {
        boolean newElement = false;
        if (!tables.containsKey(key)) {
            System.out.println("new");
            mainTable.incrementNumberRecords();
            mainTable.incrementChanges();
        }
        if (!tables.containsValue(value)) {
            newElement = true;
        }
        Storeable result = tables.putMap(key, value);
        if (result == null) {
            return result;
        } else if (newElement && !result.equals(value)) {
            System.out.println("overwrite");
            System.out.println(result);
        }
        return result;
    }
}
