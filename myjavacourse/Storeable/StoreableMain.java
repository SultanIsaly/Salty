package ru.fizteh.fivt.students.isalysultan.Storeable;

import java.io.IOException;
import java.text.ParseException;

public class StoreableMain {
    public static void main(String[] argv) throws IOException, ParseException {
        MyTableProviderFactory factory = new MyTableProviderFactory();
        String newDirect = System.getProperty("fizteh.db.dir");
        MyTableProvider direct = (MyTableProvider) factory.create(newDirect);
        if (argv.length == 0) {
            InteractiveMode.interactive(direct);
        } else {
            BatchMode.batchParser(direct, argv);
        }
    }
}
