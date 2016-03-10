package ru.fizteh.fivt.students.isalysultan.JUnit.Table;

import ru.fizteh.fivt.storage.strings.TableProviderFactory;
import ru.fizteh.fivt.students.isalysultan.JUnit.Exception.PathException;

import java.nio.file.Path;
import java.nio.file.Paths;

public class MyTableProviderFactory implements TableProviderFactory {

    @Override
    public MyTableProvider create(String dir) throws IllegalArgumentException {
        MyTableProvider direct = new MyTableProvider();
        if (dir == null) {
            throw new PathException("Directory is null,it is uncorrect.");
        }
        direct.currentTablePutNull();
        Path dirPath = Paths.get(dir);
        if (!dirPath.toFile().exists()) {
            dirPath.toFile().mkdir();
        }
        if (!dirPath.toFile().isDirectory()) {
            throw new PathException("Directory it is uncorrect.");
        }
        String[] list = dirPath.toFile().list();
        direct.pathPut(dirPath);
        if (list.length == 0) {
            return direct;
        }
        direct.handleTables(list);
        return direct;
    }
}
