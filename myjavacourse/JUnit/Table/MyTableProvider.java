package ru.fizteh.fivt.students.isalysultan.JUnit.Table;

import ru.fizteh.fivt.storage.strings.TableProvider;
import ru.fizteh.fivt.students.isalysultan.JUnit.Exception.ReadOrWriteException;
import ru.fizteh.fivt.students.isalysultan.JUnit.Exception.WrongArgumentException;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MyTableProvider implements TableProvider {

    private Path directPath;

    // This HashMap save tables.
    private Map<String, MyTable> tableList = new HashMap<String, MyTable>();

    private MyTable currentTable;

    void currentTablePutNull() {
        currentTable = null;
    }

    void pathPut(Path rootPath) {
        directPath = rootPath;
    }

    void handleTables(String[] list) {
        for (String tablename : list) {
            MyTable currentDirTable = new MyTable(this, tablename, true);
            tableList.put(tablename, currentDirTable);
        }
    }

    public Path get() {
        return directPath;
    }

    public Map<String, MyTable> getTableList() {
        return tableList;
    }

    public MyTable getCurrentTable() {
        return currentTable;
    }

    @Override
    public MyTable getTable(String name) throws IllegalArgumentException {
        if (name == null) {
            throw new IllegalArgumentException("name is not correct name");
        }
        if (!tableList.containsKey(name)) {
            return null;
        }
        return tableList.get(name);
    }

    public boolean checkTableExist(String nameTable) {
        if (tableList.containsKey(nameTable)) {
            return true;
        }
        return false;
    }

    public void setCurrentTable(MyTable newCurrentTable) {
        currentTable = newCurrentTable;
    }

    public void tableInitialization(MyTable newTable, String tableName) {
        tableList.put(tableName, newTable);
        newTable.setName(tableName);
    }

    public void use(MyTable newCurrTable) {
        if (currentTable != null) {
            try {
                currentTable.write();
            } catch (IOException e) {
                throw new ReadOrWriteException("Can't write to file.");
            }
        }
        setCurrentTable(newCurrTable);
        newCurrTable.read();
    }
    
    @Override
    public void removeTable(String tableName) throws IllegalArgumentException, IllegalStateException {
        if (tableName == null) {
            throw new IllegalArgumentException("Can't create table with name null.");
        }
        if (!tableList.containsKey(tableName)) {
            throw new IllegalStateException("No exist table.");
        }
        boolean currTableInd = false;
        if (getCurrentTable() == null) {
            currTableInd = true;
        } else if (getCurrentTable().equalityTable(getTableList().get(tableName))) {
            currTableInd = true;
        }
        MyTable deleteTable = getTableList().get(tableName);
        deleteTable.dropTable();
        getTableList().remove(tableName);
        if (currTableInd) {
            setCurrentTable(null);
        }
    }

    public Map<String, Integer> showTables() throws IOException {
        Set<String> keys = getTableList().keySet();
        Map<String, Integer> result = new HashMap<String, Integer>();
        for (String key : keys) {
            if (key.equals(currentTable.getName())) {
                result.put(currentTable.getName(), currentTable.get());
            } else {
                Integer numberKeys = null;
                numberKeys = tableList.get(key).sizeKeys();
                result.put(key, numberKeys);
            }
        }
        return result;
    }

    @Override
    public MyTable createTable(String name) throws IllegalArgumentException {
        if (name == null) {
            throw new WrongArgumentException();
        }
        if (checkTableExist(name)) {
            return null;
        }
        MyTable newTable = new MyTable(this, name);
        this.tableInitialization(newTable, name);
        return newTable;
    }

}
