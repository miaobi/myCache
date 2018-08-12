package log.io.test;


import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class TestFile {
    private static final String path = "D:/workspace_intellij/MessageSystem/week_log2/";
//    private static final String path = "/Users/peak/Desktop/d/week_log/test/in/";
    private static final String outPath = "D:/workspace_intellij/MessageSystem/week_log2/out/";
    private static final StringBuffer sb = new StringBuffer(1024);
    private static int total = 0;

    public static void main(String[] args) {
        try{
        File f = new File(path);
        String logName = "";
        if(f.isDirectory()){
            String[] logNames = f.list(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    if(name.endsWith(".log")){
                        return true;
                    }
                    return false;
                }
            });
            logName = logNames[0];
        }

        File logFile = new File(path+logName);
        long start = System.currentTimeMillis();
//            write();
//        readFileByMap(logFile);
        read(logFile);
//        readByAsync(logFile);
//        readFile(logFile);
//        readFileRandom(logFile);
//        readByFileChannel(logFile);
//        System.out.println(sb.toString());
        System.out.println("cost:"+(System.currentTimeMillis()-start)+" ms");
        td.shutdown();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void write() throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter(new File(outPath+"1.log")));
        for(int i=0;i<2050;i++){
            out.write(String.valueOf(i));
            out.newLine();
        }
        out.flush();
        out.close();
    }


    private static Pattern pattern = Pattern.compile(" (\\[.*\\]) ");
    //3418ms  17s
    public static void read(File logFile) {
         String line="";
         try {
             BufferedReader in=new BufferedReader(new FileReader(logFile));
             for(;;) {
//                 sb.append(line).append("\n");
                 line=in.readLine();
                 if(line==null){
                     break;
                 }
//                 Matcher matcher = pattern.matcher(line);
                 //1-2s
                 Msg msg = new Msg();
                 msg.setValue(line);
//                 System.out.println(line);
                 //10s
                 msg.setLdt(TestDate.getDate2(line.split(" ")[4]));
//                 if(matcher.find()){
//                     msg.setLdt(TestDate.getDate(matcher.group().trim()));
//                 }
                 list.add(msg);
             }
             Collections.sort(list);
             for (Msg msg: list) {
                 System.out.println(msg);
             }
             in.close();
         } catch (IOException e) {
             System.out.println(line);
             e.printStackTrace();
         }
    }

    static TestDisruptor td = new TestDisruptor();
    //3418  6s  23-25s
    public static void readByAsync(File logFile) {
        String line="";
        try {
            td.start();
            BufferedReader in=new BufferedReader(new FileReader(logFile));
            for(;;) {
                line=in.readLine();
                if(line==null){
                    break;
                }
                td.tryPublishEvent(line);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private static List<Msg> list = new ArrayList<>();

    //1200
    private static void readFileByMap(File logFile) throws IOException {
        RandomAccessFile raf = new RandomAccessFile(logFile,"rw");
        MappedByteBuffer mappedBuffer = raf.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, raf.length());
        mappedBuffer.load();
        int i = 0;
        StringBuffer dateStr = new StringBuffer();
        boolean isAppend = false;
        boolean isNewLine = true;
        for(;;){
            byte b= mappedBuffer.get(i++);
            sb.append((char)b);
            if(b==91 & isNewLine){
                isAppend = true;
            }
            if(isAppend){
                dateStr.append((char)b);
            }
            if(b==93){
                isAppend = false;
                isNewLine = false;
            }
            if(b==10){
//                Msg msg = new Msg();
//                msg.setValue(sb.toString());
//                msg.setLdt(TestDate.getDate(dateStr.toString()));
                sb.delete(0,sb.length());
//                dateStr.delete(0,dateStr.length());
//                list.add(msg);
                isNewLine = true;
            }
            if(i>=mappedBuffer.capacity()){
                break;
            }
        }
        long start = System.currentTimeMillis();
//        Msg[] msgs = new Msg[list.size()];
//        list.toArray(msgs);
//        new TestHeap().sort(msgs);
//        Collections.sort(list);
//        Arrays.parallelSort(msgs);
        System.out.println(System.currentTimeMillis() - start + " ms");
    }
    //3323
    private static void readFile(File logFile) throws IOException {
        FileInputStream fs = new FileInputStream(logFile);
        FileChannel fileChannel = fs.getChannel();
        ByteBuffer bb = ByteBuffer.allocate(1024);
        long size = fileChannel.size();
        long position = 0;
        for(;;) {
            bb.clear();
            fileChannel.read(bb);
            int i = 0;
            for(;;){
                byte b = bb.get(i++);
//                sb.append((char)b);
                if(i>=bb.position()){
                    break;
                }
            }
            position += 1024;
            if(position>=size){
                break;
            }
        }
        fileChannel.close();
        fs.close();
    }

    //读不出来
    private static void readFileRandom(File logFile) throws IOException {
        RandomAccessFile rf = new RandomAccessFile(logFile, "rw");
        for(;;){
            String line = rf.readLine();
            if(line==null){
//                System.out.println(sb.toString());
                break;
            }
//            sb.append(line).append("\n");
        }
    }

    //3393
    public static void readByFileChannel(File logFile) throws IOException {
        FileChannel channel = FileChannel.open(Paths.get(logFile.getAbsolutePath()), StandardOpenOption.WRITE, StandardOpenOption.READ);
        ByteBuffer bb = ByteBuffer.allocateDirect(1024);
        long size = channel.size();
        long position = 0;
        for(;;){
            bb.clear();
            channel.read(bb);
            int i = 0 ;
            for(;;){
                byte b = bb.get(i++);
//                sb.append((char)b);
                if(i>=bb.position()){
                    break;
                }
            }
            position += 1024;
            if(position>=size){
                break;
            }
        }

    }

}
