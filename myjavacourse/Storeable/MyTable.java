package ru.fizteh.fivt.students.isalysultan.Storeable;

import ru.fizteh.fivt.storage.structured.ColumnFormatException;
import ru.fizteh.fivt.storage.structured.Storeable;
import ru.fizteh.fivt.storage.structured.Table;
import ru.fizteh.fivt.storage.structured.TableProvider;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.*;

public class MyTable implements Table {
    private String nameTable;

    private Path tableDirectory;

    private int numberRecords;

    private int currentChanges;

    private int commitChanges;

    private File[][] commitFiles = new File[16][16];

    private File[][] files = new File[16][16];

    private Map<Integer, Path> subDirectsMap = new HashMap<Integer, Path>();

    private final int fileNumber = 16;

    private TableProvider tableProvider;

    private List<Class<?>> columnTypes = new ArrayList<Class<?>>();

    private static String detName = "determinate" + "." + "dat";

    public MyTable() {
        // Disable instantiation to this class.
    }

    public int getCurrentChanges() {
        return currentChanges;
    }

    public int getCommitChanges() {
        return commitChanges;
    }

    public File[][] getFiles() {
        return files;
    }

    public void setFiles(File[][] newFiles) {
        files = newFiles;
    }

    public void nullNumberRecords() {
        numberRecords = 0;
    }

    public void setName(String name) {
        nameTable = name;
    }

    public int getNumberKey() {
        return numberRecords;
    }

    public Path getPath() {
        return tableDirectory;
    }

    public void setPath(Path newPath) {
        tableDirectory = newPath;
    }

    public void saveTable() {
        for (int i = 0; i < 16; ++i) {
            for (int j = 0; j < 16; ++j) {
                commitFiles[i][j] = files[i][j];
            }
        }
        commitChanges = currentChanges;
        currentChanges = 0;
    }

    public void filesRollback() {
        for (int i = 0; i < 16; ++i) {
            for (int j = 0; j < 16; ++j) {
                files[i][j] = commitFiles[i][j];
            }
        }
        currentChanges = commitChanges;
        currentChanges = 0;
    }

    public void incrementChanges() {
        ++currentChanges;
    }

    public void decrementChanges() {
        currentChanges = currentChanges - 1;
    }

    public void read() throws IOException, IllegalArgumentException, ParseException {
        String[] subDirects = tableDirectory.toFile().list();
        for (String nameSubDirect : subDirects) {
            if (nameSubDirect.equals(detName)) {
                Path detPath = tableDirectory.resolve(nameSubDirect);
                RandomAccessFile file = new RandomAccessFile(detPath.toString(), "r");
                ByteArrayOutputStream buff = new ByteArrayOutputStream();
                int fileLength = (int) file.length();
                int count = 0;
                byte part;
                while (count < fileLength) {
                    while (((part = file.readByte()) != 0) && count < fileLength) {
                        buff.write(part);
                        ++count;
                    }
                    ++count;
                    String determinateColumn = buff.toString();
                    buff.reset();
                    Class<?> columnType = determinateType(determinateColumn);
                    columnTypes.add(columnType);
                    if (count >= fileLength) {
                        break;
                    }
                }
                file.close();
            }
        }
        for (String nameSubDirect : subDirects) {
            if (nameSubDirect.equals(detName)) {
                break;
            }
            Path subDirect = tableDirectory.resolve(nameSubDirect);
            int numberDirectory;
            int numberFile;
            if (!subDirect.toFile().isDirectory()
                    || !nameSubDirect.matches("([0-9]|1[0-5])\\.dir")) {
                throw new IllegalArgumentException(
                        " Subdirectory in table is file, but it is wrong.");
            }
            String[] filesList = subDirect.toFile().list();
            if (filesList.length == 0) {
                throw new IllegalArgumentException("Direct not delete.");
            }
            numberDirectory = Integer.parseInt(nameSubDirect.substring(0,
                    nameSubDirect.length() - 4));
            subDirectsMap.put(numberDirectory, subDirect);
            for (String fileName : filesList) {
                Path filePath = subDirect.resolve(fileName);
                if (!filePath.toFile().isFile()
                        || !fileName.matches("([0-9]|1[0-5])\\.dat")) {
                    throw new IllegalArgumentException(
                            "filePath.File() is not file.");
                }
                numberFile = Integer.parseInt(fileName.substring(0,
                        fileName.length() - 4));
                File currentFileTable = new File(filePath, this);
                files[numberDirectory][numberFile] = currentFileTable;
            }
        }

    }

    public Class<?> determinateType(String determinateColomn) throws IllegalStateException {
        if (determinateColomn.equals("class java.lang.Integer")) {
            return Integer.class;
        } else if (determinateColomn.equals("class java.lang.Long")) {
            return Long.class;
        } else if (determinateColomn.equals("class java.lang.Byte")) {
            return Byte.class;
        } else if (determinateColomn.equals("class java.lang.Double")) {
            return Double.class;
        } else if (determinateColomn.equals("class java.lang.Float")) {
            return Float.class;
        } else if (determinateColomn.equals("class java.lang.Boolean")) {
            return Boolean.class;
        } else if (determinateColomn.equals("class java.lang.String")) {
            return String.class;
        } else {
            throw new IllegalStateException("Uncorrect type.");
        }
    }

    public MyTable(MyTableProvider direct, String tableName, boolean dummyArg) {
        tableProvider = direct;
        tableDirectory = direct.get().resolve(tableName);
        numberRecords = 0;
        nameTable = tableName;
    }

    public MyTable(MyTableProvider direct, String tableName, List<Class<?>> inputColumnTypes) throws IOException {
        nameTable = tableName;
        tableDirectory = direct.get().resolve(tableName);
        tableDirectory.toFile().mkdir();
        numberRecords = 0;
        columnTypes = inputColumnTypes;
        tableProvider = direct;
        Set<Integer> numberSubDirect = subDirectsMap.keySet();
        for (Integer key : numberSubDirect) {
            subDirectsMap.put(key, null);
        }
        if (!tableDirectory.toFile().isDirectory()) {
            throw new IllegalArgumentException("Directory doesn't exist.");
        }
        System.out.println("created");
    }

    public int get() {
        return numberRecords;
    }

    public boolean equalityTable(MyTable argv) {
        if (tableDirectory.equals(argv.tableDirectory)) {
            return true;
        }
        return false;
    }

    public void dropTable() {
        String[] subDirects = tableDirectory.toFile().list();
        if (subDirects.length == 0) {
            tableDirectory.toFile().delete();
            return;
        }
        for (String subDirect : subDirects) {
            if (subDirect.equals(detName)) {
                Path determinatePath = tableDirectory.resolve(detName);
                determinatePath.toFile().delete();
                break;
            }
            Path subDirectPath = tableDirectory.resolve(subDirect);
            String[] fileList = subDirectPath.toFile().list();
            if (fileList.length == 0) {
                subDirectPath.toFile().delete();
            } else {
                for (String fileString : fileList) {
                    Path filePath = subDirectPath.resolve(fileString);
                    filePath.toFile().delete();
                }
                subDirectPath.toFile().delete();
            }
        }
        tableDirectory.toFile().delete();
    }

    public void incrementNumberRecords() {
        ++numberRecords;
    }

    public void dicrementNumberRecords() {
        --numberRecords;
    }

    @Override
    public Storeable put(String key, Storeable value) throws ColumnFormatException, IOException {
        if (key == null || value == null) {
            throw new IllegalArgumentException("uncorrect arguments");
        }
        byte externalKey = key.getBytes("UTF-8")[0];
        int ndirectory = (externalKey % fileNumber);
        if (ndirectory < 0) {
            ndirectory = -ndirectory;
        }
        int nfile = (externalKey / fileNumber) % fileNumber;
        if (nfile < 0) {
            nfile = -nfile;
        }
        if (files[ndirectory][nfile] == null) {
            files[ndirectory][nfile] = new File();
        }
        File currTable = files[ndirectory][nfile];
        Path file = tableDirectory.resolve(Integer.toString(ndirectory) + "."
                + "dir");
        file = file.resolve(Integer.toString(ndirectory) + "." + "dat");
        currTable.setPath(file);
        Storeable result = CommandPut.put(key, value, currTable, this);
        return result;
    }

    @Override
    public Storeable remove(String key) throws UnsupportedEncodingException {
        if (key == null) {
            throw new IllegalArgumentException("uncorrect arguments");
        }
        byte externalKey = key.getBytes("UTF-8")[0];
        int ndirectory = externalKey % fileNumber;
        int nfile = (externalKey / fileNumber) % fileNumber;
        File currTable = files[ndirectory][nfile];
        if (currTable == null) {
            System.out.println("not found");
            return null;
        }
        Storeable result = CommandRemove.remove(key, currTable, this);
        return result;
    }

    @Override
    public int size() throws IOException {
        return numberRecords;
    }

    @Override
    public List<String> list() {
        // Derive all the keys of the table.
        List<String> listKey = new ArrayList<String>();
        for (int i = 0; i < fileNumber; ++i) {
            for (int j = 0; j < fileNumber; ++j) {
                if (files[i][j] != null) {
                    Set<String> setKey = CommandList.list(files[i][j]);
                    for (String key : setKey) {
                        listKey.add(key);
                    }
                }
            }
        }
        return listKey;
    }

    @Override
    public int commit() {
        int result = CommandCommit.commit(this);
        System.out.println(result);
        return result;
    }

    @Override
    public int rollback() {
        int result = CommandRollback.rollback(this);
        System.out.println(result);
        return result;
    }

    public void write() throws IOException {
        Path determinateDirect = tableDirectory;
        determinateDirect = determinateDirect.resolve("determinate" + "." + "dat");
        determinateDirect.toFile().createNewFile();
        RandomAccessFile determinateFile = new RandomAccessFile(determinateDirect.toString(),
                "rwd");
        for (Class<?> currentTypes : columnTypes) {
            determinateFile.write(currentTypes.toString().getBytes("UTF-8"));
            determinateFile.write('\0');
        }
        determinateFile.close();
        for (int i = 0; i < fileNumber; ++i) {
            Path subDirect = tableDirectory;
            subDirect = subDirect.resolve((Integer.toString(i) + "." + "dir"));
            boolean directExist = false;
            for (int j = 0; j < fileNumber; ++j) {
                if (files[i][j] == null) {
                    continue;
                } else if (files[i][j].needToDeleteFile()) {
                    files[i][j].deleteFile();
                } else if (files[i][j].fileOpenAndNotExist()) {
                    directExist = true;
                    files[i][j].writeFile(this);
                } else if (!files[i][j].open() && !files[i][j].empty()) {
                    if (!directExist) {
                        subDirect.toFile().mkdir();
                        subDirectsMap.put(i, subDirect);
                        directExist = true;
                        Path filePath = subDirect.resolve((Integer.toString(j)
                                + "." + "dat"));
                        files[i][j].setPath(filePath);
                        filePath.toFile().createNewFile();
                        files[i][j].writeFile(this);
                    } else {
                        Path filePath = subDirectsMap.get(i).resolve(
                                (Integer.toString(j) + "." + "dat"));
                        files[i][j].setPath(filePath);
                        filePath.toFile().createNewFile();
                        files[i][j].writeFile(this);
                    }
                }
            }
            if (!directExist && subDirect.toFile().exists()) {
                subDirectsMap.get(i).toFile().delete();
            }
        }
    }

    @Override
    public int getNumberOfUncommittedChanges() {
        return currentChanges;
    }

    @Override
    public int getColumnsCount() {
        return columnTypes.size();
    }

    @Override
    public Class<?> getColumnType(int columnIndex) throws IndexOutOfBoundsException {
        return columnTypes.get(columnIndex);
    }

    @Override
    public String getName() {
        return nameTable;
    }

    @Override
    public Storeable get(String key) throws UnsupportedEncodingException {
        if (key == null) {
            throw new IllegalArgumentException("Uncorrect arguments.");
        }
        byte externalKey = key.getBytes("UTF-8")[0];
        int ndirectory = externalKey % fileNumber;
        int nfile = (externalKey / fileNumber) % fileNumber;
        File currTable = files[ndirectory][nfile];
        if (currTable == null) {
            System.out.println("Not found.");
            return null;
        }
        Storeable oldkey = CommandGet.get(key, currTable);
        String result = new String();
        if (oldkey != null) {
            result = serialize(oldkey);
        }
        System.out.println(result);
        return oldkey;
    }

    public Storeable deserialize(String string) throws ParseException {
        return tableProvider.deserialize(this, string);
    }

    public String serialize(Storeable storeable) {
        return tableProvider.serialize(this, storeable);
    }
}
