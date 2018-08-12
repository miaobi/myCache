package log.io.test.myTest;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class MainTest {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        File f = new File(Common.FROM_PATH);
        if(f.isDirectory()){
            String[] logNames = f.list(new FilenameFilter(){
                @Override
                public boolean accept(File dir, String name) {
                    if(name.endsWith(".log")){
                        return true;
                    }
                    return false;
                }
            });
            ThreadPoolExecutor readExecutor = new ThreadPoolExecutor(10, 10, 1, TimeUnit.MINUTES, new ArrayBlockingQueue<Runnable>(1000),
                    new ReadThreadFactory(),
                    new ThreadPoolExecutor.DiscardPolicy());
            initBuffReadMap(logNames);
//            initSourceRafMap(logNames);
            initFileIndexMap(Common.fileIndexMap);
            for (int i = 0; i < logNames.length; i++){
                readExecutor.execute(new ReadFile(logNames[i]));
            }
            try {
                Common.readCounter.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            closeBuffRead(logNames);
            readExecutor.shutdown();
            long readEndTime = System.currentTimeMillis();
            System.out.println("read all file cost " + (readEndTime - startTime) + " ms");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //Test
//            System.out.println(Common.fileIndexMap);
            doWrite(startTime, logNames);
            System.out.println("write all file cost " + (System.currentTimeMillis() - readEndTime) + " ms");
            System.out.println("total cost " + (System.currentTimeMillis() - startTime) + " ms");
        }
    }

    private static void doWrite(long startTime, String[] logNames) {
        ThreadPoolExecutor writeExecutor = new ThreadPoolExecutor(7, 7, 1, TimeUnit.MINUTES, new ArrayBlockingQueue<Runnable>(1000),
                new WriteThreadFactory(),
                new ThreadPoolExecutor.DiscardPolicy());
        initSourceRafMap(logNames);
        initTargetRafMap();
//            initBuffWriteMap();
        for (int i = 0; i < Common.TARGET_FILE_NUM; i++){
            writeExecutor.execute(new WriteFile(i));
        }
        try {
            Common.writeCounter.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        closeSourceRaf(logNames);
        closeTargetRaf();
        writeExecutor.shutdown();
    }

    public static void initFileIndexMap(Map<Integer, List> fileIndexMap){
        for (int i = 0; i < Common.TARGET_FILE_NUM; i++){
            fileIndexMap.put(i+1, Collections.synchronizedList(new ArrayList()));
        }
    }

    public static void initBuffReadMap(String[] logNames){
        for (int i = 0; i < logNames.length; i++){
            File sourceFile = null;
            BufferedReader in = null;
            sourceFile = new File(Common.FROM_PATH + logNames[i]);
            try {
                in = new BufferedReader(new FileReader(sourceFile));
                Common.buffReadMap.put(logNames[i], in);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static void initBuffWriteMap(){
        File targetFile = null;
        BufferedWriter out = null;
        for(int i = 0; i < Common.TARGET_FILE_NUM; i++ ){
            targetFile = new File(Common.TO_PATH + (i+1) + ".log");
            try {
                targetFile.createNewFile();
                out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(targetFile, true)));
                Common.buffWriteMap.put(i+1, out);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void closeBuffWrite(){
        BufferedWriter out = null;
        for(int i = 0; i < Common.TARGET_FILE_NUM; i++ ){
            out = Common.buffWriteMap.get(i+1);
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void closeBuffRead(String[] logNames){
        BufferedReader in = null;
        for(int i = 0; i < logNames.length; i++ ){
            in = Common.buffReadMap.get(logNames[i]);
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void initSourceRafMap(String[] logNames){
        RandomAccessFile raf = null;
        for (int i = 0; i < logNames.length; i++){
            try {
                raf = new RandomAccessFile(Common.FROM_PATH + logNames[i], "rw");
                Common.sourceRafMap.put(logNames[i], raf);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static void initTargetRafMap(){
        String out = Common.TO_PATH;
        File f1 = new File(out);
        String[] fileName = f1.list();
        if(fileName!=null){
            for (int i = 0; i < fileName.length; i++) {
                new File(f1.getAbsolutePath()+"/"+fileName[i]).deleteOnExit();
            }
        }
        RandomAccessFile raf = null;
        for(int i = 0; i < Common.TARGET_FILE_NUM; i++ ){
            try {
                raf = new RandomAccessFile(Common.TO_PATH + (i+1) + ".log", "rw");
                Common.targetRafMap.put(i+1, raf);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static void closeSourceRaf(String[] logNames){
        RandomAccessFile raf = null;
        for(int i = 0; i < logNames.length; i++ ){
            raf = Common.sourceRafMap.get(logNames[i]);
            try {
                raf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void closeTargetRaf(){
        RandomAccessFile raf = null;
        for(int i = 0; i < Common.TARGET_FILE_NUM; i++ ){
            raf = Common.targetRafMap.get(i+1);
            try {
                raf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
