package ru.fizteh.fivt.students.isalysultan.Storeable;

import ru.fizteh.fivt.storage.structured.ColumnFormatException;
import ru.fizteh.fivt.storage.structured.Storeable;
import ru.fizteh.fivt.storage.structured.Table;
import ru.fizteh.fivt.storage.structured.TableProvider;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class MyTableProvider implements TableProvider {
    private Path directPath;

    // This HashMap save tables.
    private HashMap<String, MyTable> tableList = new HashMap<String, MyTable>();

    private MyTable currentTable;

    private static final String SPLIT_JSON = ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)";

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

    public HashMap<String, MyTable> getTableList() {
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

    public void executeExit() throws IOException {
        Set<String> keys = tableList.keySet();
        for (String key : keys) {
            tableList.get(key).write();
        }
    }

    public void setCurrentTable(MyTable newCurrentTable) {
        currentTable = newCurrentTable;
    }

    public void tableInitialization(MyTable newTable, String tableName)
            throws IOException {
        tableList.put(tableName, newTable);
        newTable.setName(tableName);
    }

    @Override
    public Storeable deserialize(Table table, String value) throws ParseException {
        if (!value.startsWith("[")) {
            throw new ParseException("Deserialize" + value + "argument doesn't start with [.", 0);
        }
        if (!value.endsWith("]")) {
            throw new ParseException("Deserialize" + value + "argument doesn't start with ].", 0);
        }
        value = value.substring(1, value.length() - 1);
        String[] parsedValue = value.split(SPLIT_JSON);
        Storeable result = new MyStoreable((MyTable) table);
        for (int currentSubString = 0; currentSubString < parsedValue.length; ++currentSubString) {
            result.setColumnAt(currentSubString, columnValue(parsedValue[currentSubString], table.getColumnType(currentSubString), table));
        }
        return result;
    }

    public Object columnValue(String string, Class<?> type, Table table) {
        string = string.trim();
        if (string == null) {
            return null;
        }
        if (type == Integer.class) {
            return Integer.parseInt(string);
        } else if (type == Long.class) {
            return Long.parseLong(string);
        } else if (type == Byte.class) {
            return Byte.parseByte(string);
        } else if (type == Float.class) {
            return Float.parseFloat(string);
        } else if (type == Double.class) {
            return Double.parseDouble(string);
        } else if (type == Boolean.class) {
            if (!"true".equals(string) && !"false".equals(string)) {
                System.err.println("Error in boolean value");
            }
            return Boolean.parseBoolean(string);
        } else {
            return string;
        }
    }

    @Override
    public String serialize(Table table, Storeable value) throws ColumnFormatException {
        StringBuilder result = new StringBuilder("[");
        for (int currentIndex = 0; currentIndex < table.getColumnsCount(); ++currentIndex) {
            result.append(castToString(table, value, currentIndex));
            result.append(", ");
        }
        result.deleteCharAt(result.length() - 1);
        result.deleteCharAt(result.length() - 1);
        result.append("]");
        return result.toString();
    }

    public String castToString(Table table, Storeable value, int index) {
        Object answer = null;
        Class<?> currentClass = table.getColumnType(index);
        if (currentClass == Integer.class) {
            answer = value.getIntAt(index);
        }
        if (currentClass == Long.class) {
            answer = value.getLongAt(index);
        }
        if (currentClass == Byte.class) {
            answer = value.getByteAt(index);
        }
        if (currentClass == Float.class) {
            answer = value.getFloatAt(index);
        }
        if (currentClass == Double.class) {
            answer = value.getDoubleAt(index);
        }
        if (currentClass == Boolean.class) {
            answer = value.getBooleanAt(index);
        }
        if (currentClass == String.class) {
            answer = value.getStringAt(index);
        }
        if (answer != null) {
            return answer.toString();
        } else {
            return null;
        }
    }

    @Override
    public Storeable createFor(Table table) {
        Storeable result = new MyStoreable((MyTable) table);
        return result;
    }

    @Override
    public Storeable createFor(Table table, List<?> values) throws ColumnFormatException, IndexOutOfBoundsException {
        Storeable result = new MyStoreable((MyTable) table, values);
        return result;
    }

    public void use(String tableName, boolean ind) throws IOException, ParseException {
        CommandUse.use(this, tableName, ind);
    }

    @Override
    public void removeTable(String tableName) throws IllegalArgumentException, IllegalStateException {
        if (tableName == null) {
            throw new IllegalArgumentException("Uncorrect arguments.");
        }
        if (!tableList.containsKey(tableName)) {
            throw new IllegalStateException("No exist table.");
        }
        CommandDrop.drop(this, tableName);
    }

    public Storeable executePut(String[] command) throws IOException, ParseException {
        String key = command[1];
        String value = new String();
        for (int i = 2; i < command.length; ++i) {
            value = value + command[i];
        }
        value.trim();
        Storeable arg = deserialize(currentTable, value);
        Storeable result = currentTable.put(key, arg);
        return result;
    }

    public Storeable executeGet(String key) throws UnsupportedEncodingException {
        if (currentTable != null) {
            Storeable find = currentTable.get(key);
            if (find == null) {
                System.out.println("Not has this key.");
            }
            return find;
        }
        System.out.println("no table");
        return null;
    }

    public Storeable executeRemove(String key) throws UnsupportedEncodingException {
        if (currentTable != null) {
            Storeable find = currentTable.remove(key);
            return find;
        }
        System.out.println("no table");
        return null;
    }

    public List<String> executeList() {
        if (currentTable != null) {
            List<String> keys = currentTable.list();
            return keys;
        }
        System.out.println("no table");
        return null;
    }

    @Override
    public Table createTable(String name, List<Class<?>> columnTypes) throws IOException, IllegalArgumentException {
        if (name == null) {
            throw new IllegalArgumentException("Argument not must null.");
        }
        MyTable result = CommandCreate.create(this, name, columnTypes);
        return result;
    }

    public int size() throws IOException {
        int result = currentTable.size();
        System.out.println(result);
        return result;
    }

    public int commit() {
        return currentTable.commit();
    }

    public int rollback() {
        return currentTable.rollback();
    }

    @Override
    public List<String> getTableNames() {
        List<String> result = new ArrayList<String>();
        Set<String> keys = tableList.keySet();
        for (String name : keys) {
            result.add(name);
        }
        return result;
    }
}
