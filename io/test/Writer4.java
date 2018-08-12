package log.io.test;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class Writer4 implements Runnable{
    private List<FileIndex1> list;
    private RandomAccessFile writer;
    private Map<String,RandomAccessFile> readFileMap;
    private CountDownLatch writeCount;
    private int i;
    private FileWriteCache2 writeCache;

    public Writer4(int i, List<FileIndex1> list, RandomAccessFile writer, Map<String,RandomAccessFile> readFileMap, CountDownLatch writeCount) throws IOException {
        this.list = list;
        this.writer = writer;
        this.readFileMap = readFileMap;
        this.writeCount = writeCount;
        this.i = i;
        this.writeCache = new FileWriteCache2(writer);
    }

    @Override
    public void run() {
        long start = System.currentTimeMillis();
        FileIndex1 bak = null;
        RandomAccessFile reader = null;
        try {
            for (FileIndex1 index:list) {
                bak = index;
                reader = readFileMap.get(index.getFileName());
                reader.seek(index.getOffset());
                byte[] b = new byte[(int)index.getLength()];
                reader.read(b,0,(int)index.getLength());
                writeCache.write(b);
                writeCache.write("\n".getBytes());
            }
            System.out.println(i+" is down! cost="+(System.currentTimeMillis()-start));
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(i+"|"+bak+" is fail! cost="+(System.currentTimeMillis()-start));
        }finally {
            writeCache.flush();
            writeCache.close();
            writeCount.countDown();
        }
    }
}
