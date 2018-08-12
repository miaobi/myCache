package log.io.test;

import java.io.File;
import java.io.FilenameFilter;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestMutiFile {
    private static final String path = "/Users/peak/Desktop/d/week_log/test/";
//        private static final String path = "/Users/peak/Desktop/d/week_log/test/in/";
    private static final String outPath = "/Users/peak/Desktop/d/week_log/test/out/";
//    private static final
    public static void main(String[] args) {
        int threadNum = 16;
        CountDownLatch latch = new CountDownLatch(threadNum);
        ExecutorService es = Executors.newFixedThreadPool(threadNum);
        try {
            File f = new File(path);
            String logName = "";
            if (f.isDirectory()) {
                String[] logNames = f.list(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        if (name.matches("1.log")) {
                            return true;
                        }
                        return false;
                    }
                });
                logName = logNames[0];
            }

            File logFile = new File(path+logName);
            long start = System.currentTimeMillis();
            System.out.println(logFile.length());
            long readSize = logFile.length()/threadNum;
            RandomAccessThead[] t = new RandomAccessThead[threadNum];
            for(int i =0 ; i<threadNum ;i++){
                if(i==threadNum-1){
                    t[i] = new RandomAccessThead(i*readSize,logFile.length(),logFile,i,latch);
                }else{
                    t[i] = new RandomAccessThead(i*readSize,(i+1)*readSize,logFile,i,latch);
                }
            }

            for (int i = 0; i < t.length; i++) {
                es.submit(t[i]);
            }

            latch.await();
            System.out.println(System.currentTimeMillis()-start + " ms");

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            es.shutdown();
        }





    }
}




