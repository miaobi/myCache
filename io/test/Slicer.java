package log.io.test;


import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Slicer implements Runnable{
    private int defaultSliceLength = 250 * 1024 ;
    private int padding = 500;
    byte[] buf;
    private int fileLength;
    int nxtOffset=0;
    IoBuffer ioBuffer;
    public Slicer(File file,CountDownLatch latch){
        this.file = file;
        this.fileLength = (int)file.length();
        this.buf = new byte[defaultSliceLength+padding];
        this.ioBuffer = new IoBuffer(defaultSliceLength+padding);
        this.latch = latch;
//        this.disruptor = new TestDisruptor2(list,defaultSliceLength+padding);
    }
    private File file;

    private CountDownLatch latch;
//    private TestDisruptor2 disruptor;


    @Override
    public void run() {
        try {
//            disruptor.start();
//            int hasRead = 0;
            RandomAccessFile f = new RandomAccessFile(file, "rw");
//            StringBuffer sb = new StringBuffer(defaultSliceLength);
//            for (; ; ) {
//                f.seek(offset);
//                if (offset >= fileLength) {
//                    System.out.println(i + "|" + offset);
//                    latch.countDown();
//                    break;
//                }
//                int length = defaultLength + padding;
//                if (fileLength - offset < length) {
//                    length = fileLength - offset;
//                }
//                byte[] b = new byte[length];
//                f.readFully(b, 0, length);
//                IndexMsg msg = new IndexMsg();
//                msg.setB(b);
//                msg.setLines(new String(b));
//                msg.setFileName(file.getName());
//                msg.setLength(length);
//                msg.setOffset(offset);
//                disruptor.tryPublishEvent(msg);
    //                disruptor.tryPublishEvent();
    //                for (int i = 0; i < length; i++) {
    //                    int x = b[i];
    //                    if(x != '\n'){
    //                        sb.append((char)x);
    //                    }else{
    //                        sb.append("\n");
    //                        sb.delete(0, sb.length());
    //                    }
    //                }
//                offset += defaultLength;
//            }
            for(;;){
                Slice slice = this.getSlice();
                if (slice == null) {
//                    Collections.sort(this.list);
//                    disruptor.shutdown();
                    latch.countDown();
                    break;
                }
                try {
                    f.seek(slice.offset);
                    int length = (int) slice.length;
                    if (slice.addPadding) {
                        length += padding;
                    }
//                    buf = new byte[defaultSliceLength+padding];
                    f.readFully(ioBuffer.buf, 0, length);
                } catch (IOException e) {
                }
//                IndexMsg msg = new IndexMsg();
//                msg.setB(buf);
//                msg.setLines(new String(buf));
//                msg.setSlice(slice);
//                msg.setFileLength(this.fileLength);
//                disruptor.tryPublishEvent(msg);
                ioBuffer.slice = slice;
                ioBuffer.prepare();
                while (ioBuffer.hasNextItem()){
                    FileIndex1 index = new FileIndex1();
                    index.setOffset(ioBuffer.slice.offset+ioBuffer.position);
                    String line = ioBuffer.getNextLine();
                    if(line.equals("")){
                        break;
                    }
                    index.setLength(ioBuffer.length);
                    index.setFileName(slice.fileName);
                    String date = line.split(" ")[4];
                    index.setDayOfMonth(date.substring(1, 3));
                    index.setDate(TestDate.getTime3(date.substring(13)));
                    int x = Integer.parseInt(index.getDayOfMonth()) & 7;
                    map.get(x).add(index);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {

        }
    }

    public Slice getSlice() {
        synchronized (this) {
            Slice slice = null;
            while (true) {
                if (nxtOffset != fileLength) {
                    boolean skipFirstRecord = true;
                    boolean addPadding = true;

                    if (nxtOffset == 0) {
                        skipFirstRecord = false;
                    }

                    long length = defaultSliceLength;

                    long left = fileLength - nxtOffset;
                    if (left <= defaultSliceLength) {
                        addPadding = false;
                        length = left;
                    }

                    slice = new Slice(this.file.getName(),nxtOffset, (int)length,
                            skipFirstRecord, addPadding);
                    nxtOffset += length;
                }

                return slice;
            }
        }
    }

    static Map<Integer,List<FileIndex1>> map = new ConcurrentHashMap<>();
    static Map<Integer,RandomAccessFile> writeFileMap = new ConcurrentHashMap<>();
//static Map<Integer,FileOutputStream> writeFileMap = new ConcurrentHashMap<>();


    public static void main(String[] args) throws InterruptedException, IOException {
        String out = "D:/workspace_intellij/MessageSystem/week_log/to/";
        File f1 = new File(out);
        String[] fileName = f1.list();
        if(fileName!=null){
            for (int i = 0; i < fileName.length; i++) {
                new File(f1.getAbsolutePath()+"/"+fileName[i]).deleteOnExit();
            }
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < 10; i++) {
            map.put(i, Collections.synchronizedList(new ArrayList<>()));
            File file = new File(out+"/"+i+".log");
            try {
                RandomAccessFile raf = new RandomAccessFile(file, "rw");
                writeFileMap.put(i, raf);
//                FileOutputStream write = new FileOutputStream(file);
//                writeFileMap.put(i, write);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        Thread.sleep(2000);
        long start = System.currentTimeMillis();
//        String test = "/Users/peak/Desktop/d/week_log/test/";
        String product = "D:/workspace_intellij/MessageSystem/week_log/from/";
//        File f = new File(test);
        File f = new File(product);
        String[] fileNames = f.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".log");
            }
        });
        ExecutorService es = Executors.newFixedThreadPool(fileNames.length);
        CountDownLatch count = new CountDownLatch(fileNames.length);
        File[] files = new File[fileNames.length];
        Slicer[] slicers = new Slicer[fileNames.length];
        for(int i = 0 ; i<files.length ;i++){
            files[i] = new File(f.getAbsolutePath()+"/"+fileNames[i]);
            System.out.println(i+"="+files[i].length());
            slicers[i] = new Slicer(files[i],count);
        }

        Map<String,RandomAccessFile> readFileMap = new ConcurrentHashMap<>();
        for(int i = 0 ; i<files.length ;i++){
            File file = files[i];
            readFileMap.put(file.getName(), new RandomAccessFile(file,"r"));
        }

        for(int i = 0 ; i<files.length ;i++){
            es.submit(slicers[i]);
        }
        count.await();
        es.shutdown();
        long cost = System.currentTimeMillis() - start;
        System.out.println("read:"+cost+" ms");
        for(Map.Entry<Integer, List<FileIndex1>> entry : map.entrySet()){
            List<FileIndex1> list = entry.getValue();
            if(list.size()==0){
                continue;
            }
//            System.out.println(entry.getKey()+"="+list.size());
            Collections.sort(list);
        }
        cost = System.currentTimeMillis() - start;
        System.out.println("sort:"+cost+" ms");
        Thread.sleep(100);

        CountDownLatch writeCount = new CountDownLatch(7);
        ExecutorService writeEs = Executors.newFixedThreadPool(7);
        for(Map.Entry<Integer, List<FileIndex1>> entry : map.entrySet()){
            Integer key = entry.getKey();
            RandomAccessFile writer = writeFileMap.get(key);
//                FileOutputStream writer = writeFileMap.get(key);
            List<FileIndex1> list = entry.getValue();
            if(list.size()==0){
                continue;
            }
//                Writer3 writer1 = new Writer3(key,list,writer,readFileMap,writeCount);
            Writer4 writer1 = new Writer4(key,list,writer,readFileMap,writeCount);
            writeEs.submit(writer1);
        }
        writeCount.await();
        writeEs.shutdown();

        for(Map.Entry<Integer,RandomAccessFile> entry:writeFileMap.entrySet()){
            RandomAccessFile raf = entry.getValue();
            raf.close();
        }
        cost = System.currentTimeMillis() - start;
        System.out.println("write cost:"+cost+" ms");
    }

    public static void printS(Slice s) throws IOException {
        File file = new File("/Users/peak/Desktop/d/week_log/test/in/"+s.fileName);
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        raf.seek(s.offset);
        byte[] b = new byte[(int)s.length];
        raf.readFully(b, 0, s.length);
        System.out.println(s);
        System.out.println(new String(b));
        System.out.println("=====================");
    }

}




class Slice {
    public Slice(String fileName,int offset, int length,boolean skipFirstRecord,boolean addPadding){
        this.fileName = fileName;
        this.offset = offset;
        this.length = length;
        this.skipFirstRecord = skipFirstRecord;
        this.addPadding = addPadding;
    }
    public int offset;
    public int length;
    public String fileName;
    public boolean addPadding;
    public boolean skipFirstRecord;

    @Override
    public String toString() {
        return "Slice{" +
                "offset=" + offset +
                ", length=" + length +
                ", fileName='" + fileName + '\'' +
                ", addPadding=" + addPadding +
                ", skipFirstRecord=" + skipFirstRecord +
                '}';
    }
}
