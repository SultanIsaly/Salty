package ru.fizteh.fivt.students.IsalySultan.JUnit.Interpreter;

import ru.fizteh.fivt.students.IsalySultan.JUnit.Exception.WrongArgumentException;

import java.util.Map;
import java.util.Scanner;

public class Interpreter {
    public Map<String, Command> commands;

    private final String PROMPT = "$ ";
    private final String EMPTY = " ";
    private final String COLON = ";";

    public Interpreter(String[] argv, Map<String, Command> mapCommands) {
        commands = mapCommands;
        run(argv);
    }

    public void run(String[] argv) {
        if (argv.length == 0) {
            runInteractiveMode();
        } else {
            runBatchMode(argv);
        }
    }

    public void runInteractiveMode() {
        Scanner in = new Scanner(System.in);
        String[] parserCommand;
        while (true) {
            String command;
            System.out.print(PROMPT);
            command = in.nextLine();
            parserCommand = command.split(EMPTY);
            if (commands.get(parserCommand[0]) == null) {
                throw new WrongArgumentException();
            }
            commands.get(parserCommand[0]).execute(parserCommand);
        }
    }

    public void runBatchMode(String[] argv) {
        Scanner in = new Scanner(System.in);
        StringBuilder allStringBuild = new StringBuilder();
        for (String argument : argv) {
            if (allStringBuild.length() != 0) {
                allStringBuild.append(' ');
            }
            allStringBuild.append(argument);
        }
        String allString = allStringBuild.toString();
        String[] commandsArg = allString.split(COLON);
        int i = 0;
        while (i < commandsArg.length) {
            String[] command = commandsArg[i].trim().split(EMPTY);
            if (commands.get(command[0]) == null) {
                throw new WrongArgumentException();
            }
            commands.get(command[0]).execute(command);
            ++i;
        }
        System.exit(0);
    }

}
