package ru.fizteh.fivt.students.isalysultan.Storeable;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CommandCreate {

    private static Set<Class<?>> validTypes;

    public static void setValidTypes() {
        validTypes = new HashSet<>();
        validTypes.add(Integer.class);
        validTypes.add(Long.class);
        validTypes.add(Byte.class);
        validTypes.add(Double.class);
        validTypes.add(Float.class);
        validTypes.add(Boolean.class);
        validTypes.add(String.class);
    }

    public static MyTable create(MyTableProvider direct, String tableName, List<Class<?>> columnTypes)
            throws IOException, IllegalArgumentException {
        if (direct.checkTableExist(tableName)) {
            System.out.println("tablename exists");
            return null;
        }
        setValidTypes();
        for (Class<?> current : columnTypes) {
            if (!validTypes.contains(current)) {
                throw new IllegalArgumentException("Invalid column type");
            }
        }
        MyTable newTable = new MyTable(direct, tableName, columnTypes);
        direct.tableInitialization(newTable, tableName);
        return newTable;
    }

}
