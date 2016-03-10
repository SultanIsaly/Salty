package ru.fizteh.fivt.students.isalysultan.JUnit.Table;

import ru.fizteh.fivt.students.isalysultan.JUnit.Exception.OffsetException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

public class File {
    private Path filePath;

    private boolean openRead = false;

    private final Integer SPACE = 4;

    private final String FORMAT = "UTF-8";

    private final String PERMISSIONS = "rwd";

    private final String READ = "r";

    private TreeMap<String, String> storage = new TreeMap<String, String>();

    public String getForMap(String key) {
        return storage.get(key);
    }

    public boolean containsKey(String key) {
        return storage.containsKey(key);
    }

    public String putMap(String key, String value) {
        return storage.put(key, value);
    }

    public String removeMap(String key) {
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

    public File() throws IOException {
        filePath = null;
    }

    public File(Path filesPath, MyTable table) throws IOException {
        filePath = filesPath;
        RandomAccessFile file = new RandomAccessFile(filePath.toString(), READ);
        openRead = true;
        if (file.length() > 0) {
            readFile(file, table);
            file.close();
            return;
        }
        file.close();
    }

    public void readFile(RandomAccessFile file, MyTable table) throws IOException {
        ByteArrayOutputStream buff = new ByteArrayOutputStream();
        ArrayList<String> key = new ArrayList<String>();
        ArrayList<Integer> offset = new ArrayList<Integer>();
        int count = 0;
        int byteReadIndicator = -1;
        byte part;
        boolean dontReadFirstOffset = false;
        while (count < byteReadIndicator || !dontReadFirstOffset) {
            while ((part = file.readByte()) != 0) {
                ++count;
                buff.write(part);
            }
            ++count;
            if (byteReadIndicator == -1) {
                byteReadIndicator = file.readInt();
                offset.add(byteReadIndicator);
                dontReadFirstOffset = true;
            } else {
                offset.add(file.readInt());
            }
            count += SPACE;
            key.add(buff.toString(FORMAT));
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
                throw new OffsetException();
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
            storage.put(itKey.next(), buff.toString(FORMAT));
            buff.reset();
            count = afterCount;
        }
        buff.close();

    }

    public void writeFile() throws IOException {
        RandomAccessFile endFile = new RandomAccessFile(filePath.toString(),
                PERMISSIONS);
        endFile.setLength(0);
        Set<String> keys = storage.keySet();
        ArrayList<Integer> offSetsForKey = new ArrayList<Integer>();
        for (String currentKey : keys) {
            endFile.write((currentKey).getBytes(FORMAT));
            endFile.write('\0');
            offSetsForKey.add((int) endFile.getFilePointer());
            endFile.writeInt(0);
        }
        ArrayList<Integer> offSetsForValue = new ArrayList<Integer>();
        for (String currentKey : keys) {
            offSetsForValue.add((int) endFile.getFilePointer());
            endFile.write(storage.get(currentKey).getBytes(FORMAT));
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
