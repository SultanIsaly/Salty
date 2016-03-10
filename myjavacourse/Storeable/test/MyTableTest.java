package ru.fizteh.fivt.students.isalysultan.Storeable.test;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import ru.fizteh.fivt.storage.structured.Storeable;
import ru.fizteh.fivt.storage.structured.Table;
import ru.fizteh.fivt.students.IsalySultan.Storeable.MyTable;
import ru.fizteh.fivt.students.IsalySultan.Storeable.MyTableProvider;
import ru.fizteh.fivt.students.IsalySultan.Storeable.MyTableProviderFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.testng.AssertJUnit.assertTrue;

public class MyTableTest {
    @Rule
    public TemporaryFolder tmpFolder = new TemporaryFolder();

    private MyTableProvider provider;
    private Table table;
    private String rootPath;
    private List<Class<?>> types;

    @Before
    public void initTable() throws IOException {
        MyTableProviderFactory factory = new MyTableProviderFactory();
        rootPath = tmpFolder.newFolder("a").getAbsolutePath();
        provider = factory.create(rootPath);
        Class<?>[] arrayTypes = {Integer.class, Long.class, Float.class, Double.class,
                Boolean.class, String.class, Byte.class};
        types = Arrays.asList(arrayTypes);
        table = provider.createTable("table", types);
    }

    @Test
    public void testGetName() {
        assertEquals("table", table.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void putNull() throws IOException {
        table.put(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getNull() throws UnsupportedEncodingException {
        table.get(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeNull() throws UnsupportedEncodingException {
        table.remove(null);
    }

    @Test
    public void testPutAndGet() throws IOException {
        Object[] values = {5, 6L, 5.2f, 5.4, true, null, (Byte) (byte) 3};
        Storeable value = provider.createFor(table, Arrays.asList(values));
        assertNull(table.put("key", value));
        Storeable ret = table.get("key");
        assertEquals(ret.getByteAt(6), (Byte) (byte) 3);
        assertEquals(ret.getStringAt(5), null);
        assertEquals(ret.getColumnAt(1), 6L);
        value.setColumnAt(0, 2);
        Storeable old = table.put("key", value);
        assertEquals(table.get("key").getColumnAt(0), 2);
    }

    @Test
    public void testPutAndRemove() throws IOException {
        Object[] values = {5, 6L, 5.2f, 5.4, true, null, (Byte) (byte) 3};
        Storeable value = provider.createFor(table, Arrays.asList(values));
        assertNull(table.put("key", value));
        Storeable old = table.remove("key");
        assertEquals(old.getColumnAt(0), 5);
        assertEquals(old.getColumnAt(1), 6L);
        assertEquals(old.getColumnAt(6), (byte) 3);
        assertNull(table.remove("key"));
    }

    @Test
    public void testSize() throws IOException {
        Storeable value = provider.createFor(table);
        assertEquals(0, table.size());
        table.put("1", value);
        assertEquals(1, table.size());
        table.put("2", value);
        table.put("3", value);
        table.put("2", value);
        assertEquals(3, table.size());
    }

    @Test
    public void testColumnsNumber() {
        assertEquals(7, table.getColumnsCount());
    }

    @Test
    public void testGetColumnType() {
        for (int i = 0; i < types.size(); i++) {
            assertEquals(types.get(i), table.getColumnType(i));
        }
    }

    @Test
    public void testRollBack() throws IOException {
        Storeable value = provider.createFor(table);
        assertEquals(0, table.rollback());
        table.put("1", value);
        table.put("2", value);
        table.put("3", value);
        table.remove("1");
        table.put("1", value);
        assertEquals(3, table.size());
        assertEquals(0, table.rollback());
    }

    @Test
    public void testCommit() throws IOException {
        Storeable value = provider.createFor(table);
        assertEquals(0, table.commit());
        table.put("1", value);
        table.put("2", value);
        table.put("3", value);
        table.remove("3");
        assertEquals(2, table.commit());
        assertEquals(2, table.size());
    }

    @Test
    public void testList() throws IOException {
        Storeable value = provider.createFor(table);
        table.put("1", value);
        table.put("2", value);
        table.put("3", value);
        assertEquals(3, ((MyTable) table).list().size());
        assertTrue(((MyTable) table).list().containsAll(new LinkedList<>(Arrays.asList("1", "2", "3"))));
    }
}
