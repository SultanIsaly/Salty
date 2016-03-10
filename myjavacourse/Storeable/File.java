package ru.fizteh.fivt.students.isalysultan.Storeable;

import ru.fizteh.fivt.storage.structured.Storeable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.*;

public class File {
    private Path filePath;

    private boolean openRead = false;

    //private TreeMap<String, String> storage = new TreeMap<String, String>();
    private TreeMap<String, Storeable> storage = new TreeMap<String, Storeable>();

    private List<Class<?>> columnTypes;

    private static Set<Class<?>> validTypes;

    private int lenghtDeterminationStoreable;

    private String types;

    public Storeable getForMap(String key) {
        return storage.get(key);
    }

    public void setValidTypes() {
        validTypes = new HashSet<>();
        validTypes.add(Integer.class);
        validTypes.add(Long.class);
        validTypes.add(Byte.class);
        validTypes.add(Double.class);
        validTypes.add(Float.class);
        validTypes.add(Boolean.class);
        validTypes.add(String.class);
    }

    public boolean containsKey(String key) {
        return storage.containsKey(key);
    }

    public boolean containsValue(Storeable value) {
        return storage.containsValue(value);
    }

    public Storeable putMap(String key, Storeable value) {
        return storage.put(key, value);
    }

    public Storeable removeMap(String key) {
        return storage.remove(key);
    }

    public Set<String> keySetMap() {
        return storage.keySet();
    }

    public void setPath(Path newPath) {
        filePath = newPath;
    }

    public Path getPath() {
        return filePath;
    }

    public boolean emptyMap() {
        if (storage.isEmpty()) {
            return true;
        }
        return false;
    }

    public boolean getOpenRead() {
        return openRead;
    }

    public File() throws IOException {
        setValidTypes();
        filePath = null;
    }

    public File(Path filesPath, MyTable table) throws IOException, ParseException {
        filePath = filesPath;
        setValidTypes();
        RandomAccessFile file = new RandomAccessFile(filePath.toString(), "r");
        openRead = true;
        if (file.length() > 0) {
            readFile(file, table);
            file.close();
            return;
        }
        file.close();
    }

    public void readFile(RandomAccessFile file, MyTable table) throws IOException, ParseException {
        ByteArrayOutputStream buff = new ByteArrayOutputStream();
        ArrayList<String> key = new ArrayList<String>();
        ArrayList<Integer> offset = new ArrayList<Integer>();
        int count = 0;
        int camelCase = -1;
        byte part;
        boolean dontReadFirstOffset = false;
        //First 4 byte is lenght type of storeable.
        //Before lenght byte is type of storeable in format Json.
        /*lenghtDeterminationStoreable = file.readInt();
        count+=4;
        int determinate=0;
        while (determinate<lenghtDeterminationStoreable) {
            buff.write(file.readByte());
        }
        types = buff.toString();
        buff.reset();
        count+=lenghtDeterminationStoreable;*/
        while (count < camelCase || !dontReadFirstOffset) {
            while ((part = file.readByte()) != 0) {
                ++count;
                buff.write(part);
            }
            ++count;
            if (camelCase == -1) {
                camelCase = file.readInt();
                offset.add(camelCase);
                dontReadFirstOffset = true;
            } else {
                offset.add(file.readInt());
            }
            count += 4;
            key.add(buff.toString("UTF-8"));
            buff.reset();
        }
        Iterator<String> itKey = key.iterator();
        Iterator<Integer> itForEndOffset = offset.iterator();
        int size = (int) file.length();
        count = itForEndOffset.next();
        int afterCount = count;
        boolean forEnd = true;
        while (forEnd) {
            table.incrementNumberRecords();
            if (size < count) {
                System.err.println("error with offset");
                System.exit(2);
            }
            boolean endFile = false;
            if (!itForEndOffset.hasNext()) {
                while (count < size) {
                    forEnd = false;
                    buff.write(file.readByte());
                    ++count;
                }
                endFile = true;
            }
            if (!endFile) {
                afterCount = itForEndOffset.next();
                while (count < afterCount) {
                    buff.write(file.readByte());
                    ++count;
                }
            }
            Storeable value = table.deserialize(buff.toString());
            storage.put(itKey.next(), value);
            buff.reset();
            count = afterCount;
        }
        buff.close();

    }

    public void writeFile(MyTable table) throws IOException {
        RandomAccessFile endFile = new RandomAccessFile(filePath.toString(),
                "rwd");
        endFile.setLength(0);
        Set<String> keys = storage.keySet();
        ArrayList<Integer> offSetsForKey = new ArrayList<Integer>();
        for (String currentKey : keys) {
            endFile.write((currentKey).getBytes("UTF-8"));
            endFile.write('\0');
            offSetsForKey.add((int) endFile.getFilePointer());
            endFile.writeInt(0);
        }
        ArrayList<Integer> offSetsForValue = new ArrayList<Integer>();
        for (String currentKey : keys) {
            offSetsForValue.add((int) endFile.getFilePointer());
            String writeString = table.serialize(storage.get(currentKey));
            endFile.write(writeString.getBytes("UTF-8"));
        }
        Iterator<Integer> itOffSetKey = offSetsForKey.iterator();
        Iterator<Integer> itOffSetValue = offSetsForValue.iterator();
        while (itOffSetKey.hasNext()) {
            endFile.seek(itOffSetKey.next());
            endFile.writeInt(itOffSetValue.next());
        }
        endFile.close();
    }

    public boolean empty() {
        return filePath == null || storage.isEmpty();
    }

    public boolean needToDeleteFile() {
        return this.empty() && openRead;
    }

    public void deleteFile() {
        filePath.toFile().delete();
    }

    public boolean fileOpenAndNotExist() {
        return openRead && filePath != null;
    }

    public boolean open() {
        return openRead;
    }
}
