package log.io.test.myTest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.RandomAccessFile;
import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

public class Common {
//    protected static final String FROM_PATH = "D:/workspace_intellij/MessageSystem/week_log/from/";
//    protected static final String TO_PATH = "D:/workspace_intellij/MessageSystem/week_log/to/";
    protected static final String FROM_PATH = "/mnt/d/workspace_intellij/MessageSystem/week_log/from/";
    protected static final String TO_PATH = "/mnt/d/workspace_intellij/MessageSystem/week_log/to/";
    protected static final int SOURCE_FILE_NUM = 10;
    protected static final int TARGET_FILE_NUM = 7;

    protected static CountDownLatch readCounter = new CountDownLatch(Common.SOURCE_FILE_NUM);
    protected static CountDownLatch writeCounter = new CountDownLatch(Common.TARGET_FILE_NUM);

    protected static Map<Integer, List> fileIndexMap = new ConcurrentHashMap<>();

    protected static Map<String, BufferedReader> buffReadMap = new HashMap<>();
    protected static Map<Integer, BufferedWriter> buffWriteMap = new HashMap<>();
    protected static Map<String, RandomAccessFile> sourceRafMap = new HashMap<>();
    protected static Map<Integer, RandomAccessFile> targetRafMap = new HashMap<>();

    public static int getWeekIndex(DayOfWeek week){
        switch (week){
            case MONDAY:
                return 1;
            case TUESDAY:
                return 2;
            case WEDNESDAY:
                return 3;
            case THURSDAY:
                return 4;
            case FRIDAY:
                return 5;
            case SATURDAY:
                return 6;
            case SUNDAY:
                return 7;
        }
        return -1;
    }
}
