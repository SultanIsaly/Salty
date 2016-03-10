package ru.fizteh.fivt.students.isalysultan.Storeable.test;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import ru.fizteh.fivt.students.IsalySultan.JUnit.MyTableProviderFactory;

import static org.junit.Assert.assertEquals;

public class MyTableProviderFactoryTest {

    public MyTableProviderFactory direct;

    @Rule
    public TemporaryFolder tmpFolder = new TemporaryFolder();

    @Before
    public void init() {
        direct = new MyTableProviderFactory();
    }

    @Test(expected = IllegalArgumentException.class)
    public void getNullTable() {
        assertEquals(false, direct.create(null));
    }

}
