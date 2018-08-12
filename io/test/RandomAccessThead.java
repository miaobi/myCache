package log.io.test;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.concurrent.CountDownLatch;

public class RandomAccessThead implements Runnable{

    public RandomAccessThead(long start,long end,File file,int i,CountDownLatch latch){
        this.start = start;
        this.end = end;
        this.file = file;
        this.i = i;
        this.latch = latch;
    }

    private CountDownLatch latch;
    private long start;
    private long end;
    private File file;
    private int i;

    @Override
    public void run() {
        try {
            int hasRead = 0;
            byte[] b = new byte[1024];
            RandomAccessFile f = new RandomAccessFile(file,"rw");
            f.seek(start);
            for(;;){
                hasRead = f.read(b);
                f.readLine();
                if(start>end || hasRead<0){
                    System.out.println(i+"|"+start);
                    latch.countDown();
                    break;
                }
                start+=hasRead;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
