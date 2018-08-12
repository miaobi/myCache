package log.io.test.myTest;

import log.io.test.TestDate;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class ReadFile implements Runnable {
    ReentrantLock lock = new ReentrantLock();
    private String logName;

    public ReadFile(String logName) {
        this.logName = logName;
    }

    @Override
    public void run() {
        String line = "";
        try {
            long start = System.currentTimeMillis();
            String date = "";
            int week = 0;
            List list = null;
            long offset = 0;
            long length = 0;
            FileIndex fileIndex = null;
            System.out.println("read " + logName + " stating... "+Thread.currentThread());
            BufferedReader in = Common.buffReadMap.get(logName);
//            RandomAccessFile raf = Common.sourceRafMap.get(logName);
            for (; ; ) {
                line = in.readLine();
                if (line == null) {
                    Common.readCounter.countDown();
                    System.out.println("read " + logName + " cost:" + (System.currentTimeMillis() - start) + " ms");
                    break;
                }
                date = line.split(" ")[4];
                fileIndex = new FileIndex();
                fileIndex.setDate(date);
                fileIndex.setFileName(logName);
//                    fileIndex.setLine(line);
                length = line.length();
                fileIndex.setLength(length);
                fileIndex.setOffset(offset);
                week = Common.getWeekIndex(TestDate.getDate2(date).getDayOfWeek());
//                lock.lock();
//                try {
//                    list = Common.fileIndexMap.get(week);
//                    list.add(fileIndex);
//                } finally {
//                    lock.unlock();
//                }
                Common.fileIndexMap.get(week).add(fileIndex);
                offset += (length + 2);
            }
//                in.close();
        } catch (IOException e) {
            System.out.println(line);
            e.printStackTrace();
        }
    }
}
