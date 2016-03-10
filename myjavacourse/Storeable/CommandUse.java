package ru.fizteh.fivt.students.isalysultan.Storeable;

import java.io.IOException;
import java.text.ParseException;

public class CommandUse {
    public static void use(MyTableProvider direct, String tableName,
                           boolean notForShowTables) throws IOException, ParseException {
        MyTable currentTable = direct.getCurrentTable();
        MyTable newCurrentTable = direct.getTable(tableName);
        if (newCurrentTable == null) {
            if (notForShowTables) {
                System.out.println("table not exists");
            }
            return;
        }
        if (currentTable != null) {
            if (notForShowTables && currentTable.getCurrentChanges() != 0) {
                System.err.println(" Don't commit all change and you can't use 'use' .");
                return;
            }
            if (newCurrentTable.getName().equals(currentTable.getName())) {
                return;
            }
            currentTable.write();
        }
        direct.setCurrentTable(newCurrentTable);
        newCurrentTable.read();
        if (notForShowTables) {
            System.out.println("use tablename");
        }
    }
}
