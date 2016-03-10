package ru.fizteh.fivt.students.isalysultan.Storeable;

import ru.fizteh.fivt.storage.structured.ColumnFormatException;
import ru.fizteh.fivt.storage.structured.Storeable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MyStoreable implements Storeable {

    private List<Object> values;
    private List<Class<?>> columnTypes;
    private static Set<Class<?>> validTypes;

    public MyStoreable(MyTable table) {
        setValidTypes();
        columnTypes = new ArrayList<>();
        for (int currentColumns = 0; currentColumns < table.getColumnsCount(); ++currentColumns) {
            Class<?> columnType = table.getColumnType(currentColumns);
            if (!validTypes.contains(columnType)) {
                throw new ColumnFormatException("Invalid column type");
            }
            columnTypes.add(columnType);
        }
        values = new ArrayList<>();
        for (int i = 0; i < columnTypes.size(); ++i) {
            values.add(null);
        }
    }

    public MyStoreable(MyTable table, List<?> values) throws ColumnFormatException {
        this(table);
        if (values.size() != this.values.size()) {
            throw new ColumnFormatException("Size values is uncorrect.");
        } else {
            for (int i = 0; i < values.size(); ++i) {
                this.values.set(i, values.get(i));
            }
        }
    }

    public void setValidTypes() {
        validTypes = new HashSet<>();
        validTypes.add(Integer.class);
        validTypes.add(Long.class);
        validTypes.add(Byte.class);
        validTypes.add(Double.class);
        validTypes.add(Float.class);
        validTypes.add(Boolean.class);
        validTypes.add(String.class);
    }

    public void checkBroad(int columnIndex) {
        if (columnIndex >= columnTypes.size()) {
            throw new ColumnFormatException("ColumnIndex go out broad size columns.");
        }
    }

    public void checkType(int columnIndex, Class<?> type) {
        if (!columnTypes.get(columnIndex).equals(type)) {
            throw new IndexOutOfBoundsException("Invalid column format");
        }
    }

    @Override
    public void setColumnAt(int columnIndex, Object value) throws ColumnFormatException, IndexOutOfBoundsException {
        checkBroad(columnIndex);
        if (value != null) {
            checkType(columnIndex, value.getClass());
        }
        values.set(columnIndex, value);
    }

    @Override
    public Object getColumnAt(int columnIndex) throws IndexOutOfBoundsException {
        checkBroad(columnIndex);
        return values.get(columnIndex);
    }

    @Override
    public Integer getIntAt(int columnIndex) throws ColumnFormatException, IndexOutOfBoundsException {
        checkBroad(columnIndex);
        checkType(columnIndex, Integer.class);
        return (Integer) values.get(columnIndex);
    }

    @Override
    public Long getLongAt(int columnIndex) throws ColumnFormatException, IndexOutOfBoundsException {
        checkBroad(columnIndex);
        checkType(columnIndex, Long.class);
        return (Long) values.get(columnIndex);
    }

    @Override
    public Byte getByteAt(int columnIndex) throws ColumnFormatException, IndexOutOfBoundsException {
        checkBroad(columnIndex);
        checkType(columnIndex, Byte.class);
        return (Byte) values.get(columnIndex);
    }

    @Override
    public Float getFloatAt(int columnIndex) throws ColumnFormatException, IndexOutOfBoundsException {
        checkBroad(columnIndex);
        checkType(columnIndex, Float.class);
        return (Float) values.get(columnIndex);
    }

    @Override
    public Double getDoubleAt(int columnIndex) throws ColumnFormatException, IndexOutOfBoundsException {
        checkBroad(columnIndex);
        checkType(columnIndex, Double.class);
        return (Double) values.get(columnIndex);
    }

    @Override
    public Boolean getBooleanAt(int columnIndex) throws ColumnFormatException, IndexOutOfBoundsException {
        checkBroad(columnIndex);
        checkType(columnIndex, Boolean.class);
        return (Boolean) values.get(columnIndex);
    }

    @Override
    public String getStringAt(int columnIndex) throws ColumnFormatException, IndexOutOfBoundsException {
        checkBroad(columnIndex);
        checkType(columnIndex, String.class);
        return (String) values.get(columnIndex);
    }
}
