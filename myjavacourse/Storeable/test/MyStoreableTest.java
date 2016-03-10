package ru.fizteh.fivt.students.IsalySultan.Storeable.test;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import ru.fizteh.fivt.storage.structured.ColumnFormatException;
import ru.fizteh.fivt.storage.structured.Storeable;
import ru.fizteh.fivt.storage.structured.TableProviderFactory;
import ru.fizteh.fivt.students.IsalySultan.Storeable.MyStoreable;
import ru.fizteh.fivt.students.IsalySultan.Storeable.MyTable;
import ru.fizteh.fivt.students.IsalySultan.Storeable.MyTableProvider;
import ru.fizteh.fivt.students.IsalySultan.Storeable.MyTableProviderFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class MyStoreableTest {

    @Rule
    public TemporaryFolder tmpFolder = new TemporaryFolder();
    MyTableProvider provider;
    private final Path testDir = Paths.get(System.getProperty("java.io.tmpdir"), "DataBaseTestDirectory");
    private List<Class<?>> listWhichHoldsAllTypes = new ArrayList<>();
    List<Object> listWhichHoldsAllValues = new ArrayList<>();
    private Storeable megaStoreable;

    @Before
    public void initProvider() throws IOException {
        TableProviderFactory factory = new MyTableProviderFactory();
        provider = (MyTableProvider) factory.create(tmpFolder.newFolder("a").getAbsolutePath());
        listWhichHoldsAllTypes.add(Integer.class);
        listWhichHoldsAllTypes.add(Float.class);
        listWhichHoldsAllTypes.add(String.class);
        listWhichHoldsAllTypes.add(Double.class);
        listWhichHoldsAllTypes.add(Long.class);
        listWhichHoldsAllTypes.add(Byte.class);
        listWhichHoldsAllTypes.add(Boolean.class);
        MyTable table = new MyTable(provider, "table name", listWhichHoldsAllTypes);
        listWhichHoldsAllValues.add(1);
        listWhichHoldsAllValues.add(7.62f);
        listWhichHoldsAllValues.add("string");
        listWhichHoldsAllValues.add((double) 3.14);
        listWhichHoldsAllValues.add((long) 2);
        listWhichHoldsAllValues.add((byte) 3);
        listWhichHoldsAllValues.add(true);
        megaStoreable = new MyStoreable(table, listWhichHoldsAllValues);
    }

    @Test
    public void testGetColumnAt() throws Exception {
        assertEquals(megaStoreable.getColumnAt(0), 1);
    }

    @Test
    public void testSetColumnAt() throws Exception {
        megaStoreable.setColumnAt(0, 2);
        assertEquals(megaStoreable.getColumnAt(0), 2);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testThrowColumnFormatExceptionWhileGetting() throws Exception {
        megaStoreable.getIntAt(3);
    }

    @Test(expected = ColumnFormatException.class)
    public void testThrowIndexOutOfBoundExceptionWhileGetting() throws Exception {
        megaStoreable.getIntAt(42);
    }

    @Test
    public void testGetIntAt() throws Exception {
        assertEquals(megaStoreable.getIntAt(0), (Integer) (int) 1);
    }

    @Test
    public void testGetLongAt() throws Exception {
        assertEquals(megaStoreable.getLongAt(4), (Long) (long) 2);
    }

    @Test
    public void testGetByteAt() throws Exception {
        assertEquals(megaStoreable.getByteAt(5), (Byte) (byte) 3);
    }

    @Test
    public void testGetFloatAt() throws Exception {
        assertEquals(megaStoreable.getFloatAt(1), (Float) 7.62f);
    }

    @Test
    public void testGetDoubleAt() throws Exception {
        assertEquals(megaStoreable.getDoubleAt(3), (Double) 3.14);
    }

    @Test
    public void testGetBooleanAt() throws Exception {
        assertEquals(megaStoreable.getBooleanAt(6), true);
    }

    @Test
    public void testGetStringAt() throws Exception {
        assertEquals(megaStoreable.getStringAt(2), "string");
    }
}
