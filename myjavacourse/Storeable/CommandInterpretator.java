package ru.fizteh.fivt.students.isalysultan.Storeable;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class CommandInterpretator {
    public void executeCommand(MyTableProvider direct, String[] command)
            throws IOException, ParseException {
        if (command[0].equals("create")) {
            List<Class<?>> types = new ArrayList<Class<?>>();
            int count = 2;
            while (count < command.length) {
                types.add(determinateType(command[count]));
                ++count;
            }
            direct.createTable(command[1], types);
        } else if (command[0].equals("use")) {
            if (command.length < 2 || command.length > 2) {
                System.err.println("Wrong arguments");
            }
            direct.use(command[1], true);
        } else if (command[0].equals("drop")) {
            if (command.length < 2 || command.length > 2) {
                System.err.println("Wrong arguments");
            }
            direct.removeTable(command[1]);
        } else if (command[0].equals("put")) {
            direct.executePut(command);
        } else if (command[0].equals("get")) {
            if (command.length < 2 || command.length > 2) {
                System.err.println("Wrong arguments");
            }
            direct.executeGet(command[1]);
        } else if (command[0].equals("remove")) {
            if (command.length < 2 || command.length > 2) {
                System.err.println("Wrong arguments");
            }
            direct.executeRemove(command[1]);
        } else if (command[0].equals("list")) {
            direct.executeList();
        } else if (command[0].equals("size")) {
            if (command.length > 1 || command.length < 1) {
                System.err.println("Wrong arguments");
            }
            direct.size();
        } else if (command[0].equals("commit")) {
            direct.commit();
        } else if (command[0].equals("rollback")) {
            direct.rollback();
        } else if (command[0].equals("exit")) {
            direct.executeExit();
            System.exit(0);
        } else {
            System.out.println("Command is not recognized.");
        }
    }

    public Class<?> determinateType(String determinateColomn) throws IllegalStateException {
        if (determinateColomn.equals("Integer")) {
            return Integer.class;
        } else if (determinateColomn.equals("Long")) {
            return Long.class;
        } else if (determinateColomn.equals("Byte")) {
            return Byte.class;
        } else if (determinateColomn.equals("Double")) {
            return Double.class;
        } else if (determinateColomn.equals("Float")) {
            return Float.class;
        } else if (determinateColomn.equals("Boolean")) {
            return Boolean.class;
        } else if (determinateColomn.equals("String")) {
            return String.class;
        } else {
            throw new IllegalStateException("Uncorrect type.");
        }
    }
}
