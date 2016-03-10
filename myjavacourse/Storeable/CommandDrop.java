package ru.fizteh.fivt.students.isalysultan.Storeable;

public class CommandDrop {
    public static void drop(MyTableProvider direct, String tableName) {
        boolean currTable = false;
        if (direct.getCurrentTable() == null) {
            currTable = true;
        } else if (direct.getCurrentTable().equalityTable(direct.getTableList().get(tableName))) {
            currTable = true;
        }
        MyTable deleteTable = direct.getTableList().get(tableName);
        deleteTable.dropTable();
        direct.getTableList().remove(tableName);
        if (currTable) {
            direct.setCurrentTable(null);
        }
        System.out.println("dropped");
    }
}
