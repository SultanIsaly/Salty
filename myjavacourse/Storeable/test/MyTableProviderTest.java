package ru.fizteh.fivt.students.IsalySultan.Storeable.test;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import ru.fizteh.fivt.storage.structured.TableProviderFactory;
import ru.fizteh.fivt.students.IsalySultan.Storeable.MyTableProvider;
import ru.fizteh.fivt.students.IsalySultan.Storeable.MyTableProviderFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class MyTableProviderTest {
    @Rule
    public TemporaryFolder tmpFolder = new TemporaryFolder();

    MyTableProvider direct;

    @Before
    public void initProvider() throws IOException {
        TableProviderFactory factory = new MyTableProviderFactory();
        direct = (MyTableProvider) factory.create(tmpFolder.newFolder("a").getAbsolutePath());
    }

    @Test(expected = IllegalArgumentException.class)
    public void getNullTable() {
        direct.getTable(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createNullTable() throws IOException {
        direct.createTable(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeNullTable() {
        direct.removeTable(null);
    }

    @Test
    public void createAndGetTable() throws IOException {
        List<Class<?>> types = new ArrayList<Class<?>>();
        types.add(Integer.class);
        direct.createTable("newTable", types);
        assertNull(direct.getTable("notExistingTable"));
        assertNotNull(direct.getTable("newTable"));
    }

    @Test(expected = IllegalStateException.class)
    public void removeNotExistingTable() {
        direct.removeTable("notExistingTable");
    }

    @Test
    public void removeTable() throws IOException {
        List<Class<?>> types = new ArrayList<Class<?>>();
        types.add(Integer.class);
        assertNotNull(direct.createTable("Table1", types));
        assertNotNull(direct.getTable("Table1"));
        direct.removeTable("Table1");
        assertNull(direct.getTable("Table1"));
    }

    @Test
    public void doubleTableCreation() throws IOException {
        List<Class<?>> types = new ArrayList<Class<?>>();
        types.add(Integer.class);
        assertNotNull(direct.createTable("Table11", types));
        assertEquals(null, direct.createTable("Table11", types));
        direct.removeTable("Table11");
    }

    @Test
    public void getTableName() throws IOException {
        List<Class<?>> types = new ArrayList<Class<?>>();
        types.add(Integer.class);
        List<String> list = new ArrayList<String>();
        TableProviderFactory factory = new MyTableProviderFactory();
        MyTableProvider newDirect;
        newDirect = (MyTableProvider) factory.create(tmpFolder.newFolder("b").getAbsolutePath());
        newDirect.createTable("table1", types);
        newDirect.createTable("table", types);
        list.add("table1");
        list.add("table");
        assertEquals(list, newDirect.getTableNames());
    }
}
