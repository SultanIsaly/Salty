package ru.fizteh.fivt.students.isalysultan.JUnit.test;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import ru.fizteh.fivt.students.IsalySultan.JUnit.Table.MyTable;
import ru.fizteh.fivt.students.IsalySultan.JUnit.Table.MyTableProvider;
import ru.fizteh.fivt.students.IsalySultan.JUnit.Table.MyTableProviderFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MyTableTest {
    @Rule
    public TemporaryFolder tmpFolder = new TemporaryFolder();

    public MyTable table;
    public String rootPath;

    @Before
    public void initTable() throws IOException {
        MyTableProviderFactory factory = new MyTableProviderFactory();
        rootPath = tmpFolder.newFolder("a").getAbsolutePath();
        MyTableProvider provider = factory.create(rootPath);
        table = provider.createTable("table");
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
        assertNull(table.put("1", "7"));
        assertEquals("7", table.get("1"));
        assertEquals("7", table.put("1", "3"));
        assertEquals("3", table.get("1"));
        assertNull(table.get("c"));
    }

    @Test
    public void testPutAndRemove() throws IOException {
        assertNull(table.put("1", "2"));
        assertNull(table.remove("2"));
        assertEquals("2", table.remove("1"));
        assertNull(table.remove("1"));
        assertNull(table.get("1"));
    }

    @Test
    public void testSize() throws IOException {
        assertEquals(0, table.size());
        table.put("1", "2");
        assertEquals(1, table.size());
        table.put("3", "4");
        assertEquals(2, table.size());
        table.put("3", "5");
        assertEquals(2, table.size());
        table.remove("1");
        assertEquals(1, table.size());
        table.remove("1");
        assertEquals(1, table.size());
        table.remove("3");
        assertEquals(0, table.size());
    }

    @Test
    public void testList() throws IOException {
        assertEquals(0, table.list().size());
        table.put("1", "2");
        table.put("3", "4");
        table.put("3", "5");
        table.remove("1");
        table.put("6", "7");
        table.put("key", "value");
        assertEquals(3, table.list().size());
    }

    @Test
    public void testRollBack() throws IOException {
        assertEquals(0, table.rollback());
        table.put("1", "2");
        table.put("2", "3");
        table.put("3", "4");
        table.remove("1");
        table.put("1", "5");
        assertEquals(3, table.size());
        assertEquals(5, table.commit());
        assertEquals(0, table.rollback());
        assertEquals(3, table.size());
    }

    @Test
    public void testCommit() throws IOException {
        assertEquals(0, table.commit());
        table.put("1", "2");
        table.put("2", "3");
        table.put("3", "4");
        table.remove("3");
        assertEquals(4, table.commit());
        assertEquals(2, table.size());
    }

    @Test
    public void testCommitAndRallback() throws IOException {
        assertEquals(0, table.commit());
        table.put("1", "2");
        table.put("2", "3");
        assertEquals(2, table.commit());
        assertEquals(0, table.commit());
        table.put("4", "5");
        assertEquals("5", table.get("4"));
        assertEquals(1, table.rollback());
        assertNull(table.get("4"));
        assertEquals("3", table.get("2"));
        assertEquals(0, table.rollback());
        assertEquals(2, table.size());
        table.put("6", "7");
        table.remove("1");
        assertEquals(2, table.commit());
        assertEquals(2, table.size());
        table.put("1", "2");
        table.put("8", "9");
        table.remove("6");
        assertEquals(3, table.size());
        assertEquals(3, table.rollback());
        assertEquals(2, table.size());
    }

}

 
