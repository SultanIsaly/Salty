package ru.fizteh.fivt.students.isalysultan.Storeable;

import java.util.Set;

public class CommandList {
    public static Set<String> list(File tables) {
        if (tables != null) {
            Set<String> result = tables.keySetMap();
            String answer = String.join(", ", result);
            System.out.println(answer);
            return result;
        } else {
            return null;
        }
    }
}
