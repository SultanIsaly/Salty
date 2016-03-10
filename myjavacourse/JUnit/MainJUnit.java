package ru.fizteh.fivt.students.isalysultan.JUnit;

import com.sun.javafx.collections.MappingChange;
import ru.fizteh.fivt.students.isalysultan.JUnit.Interpreter.Command;
import ru.fizteh.fivt.students.isalysultan.JUnit.Interpreter.Database.*;
import ru.fizteh.fivt.students.isalysultan.JUnit.Interpreter.Interpreter;
import ru.fizteh.fivt.students.isalysultan.JUnit.Table.MyTableProvider;
import ru.fizteh.fivt.students.isalysultan.JUnit.Table.MyTableProviderFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainJUnit {
    public static void main(String[] argv) throws IOException {
        MyTableProviderFactory factory = new MyTableProviderFactory();
        String newDirect = System.getProperty("fizteh.db.dir");
        MyTableProvider direct = factory.create(newDirect);
        Map<String,Command> commands = new HashMap<>();
        commands.put("use", new CommandUse("use", 2, direct));
        commands.put("size", new CommandSize("size", 1, direct));
        commands.put("show", new CommandShowTables("show", 2, direct));
        commands.put("rollback", new CommandRollback("rollback", 1, direct));
        commands.put("remove", new CommandRemove("remove", 2, direct));
        commands.put("put", new CommandPut("put", 3, direct));
        commands.put("list", new CommandList("list", 1, direct));
        commands.put("get", new CommandGet("get", 2, direct));
        commands.put("exit", new CommandExit("exit", 1, direct));
        commands.put("drop", new CommandDrop("drop", 2, direct));
        commands.put("create", new CommandCreate("create", 2, direct));
        commands.put("commit", new CommandCommit("commit", 1, direct));
        Interpreter interpr = new Interpreter(argv,commands);
    }
}
