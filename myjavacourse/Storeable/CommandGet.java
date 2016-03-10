package ru.fizteh.fivt.students.isalysultan.Storeable;

import ru.fizteh.fivt.storage.structured.Storeable;

public class CommandGet {
    public static Storeable get(String key, File tables) {
        Storeable result = tables.getForMap(key);
        if (result == null) {
            System.out.println("not found");
            return null;
        } else {
            System.out.println("found");
            return result;
        }
    }
}
