package myDisruptor;
import log.io.test.TestDate;

import java.io.*;
import java.util.concurrent.CountDownLatch;

public class ReadFile {
    private static final String FROMPATH = "D:/workspace_intellij/MessageSystem/week_log/from/";
    private static Disruptor disruptor = Singleton.getDisruptor();
    private static CountDownLatch lock = new CountDownLatch(10);

    public static void main(String[] args) {
        ThreadPerTaskExc exec = new ThreadPerTaskExc();
        File f = new File(FROMPATH);
//        System.out.println("start time:" + System.currentTimeMillis());
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
            for (int i = 0; i < logNames.length; i++){
                exec.execute(new Task(logNames[i]));
            }
            try {
                lock.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Read finished");
            disruptor.close();
        }
    }

    public static class Task implements Runnable {
        private String logName;

        public Task(String logName){
             this.logName = logName;
        }

        @Override
        public void run(){
            try{
                File logFile = new File(FROMPATH + logName);
                long start = System.currentTimeMillis();
                String line="";
                try {
                    BufferedReader in = new BufferedReader(new FileReader(logFile));
                    for(;;) {
                        line = in.readLine();
                        if( line == null){
                            System.out.println("read " + logName + " cost:" + (System.currentTimeMillis()-start)+" ms");
                            lock.countDown();
                            break;
                        }
                        Event event = new Event();
                        event.setValue(line);
                        event.setLdt(TestDate.getDate2(line.split(" ")[4]));
                        disruptor.getRingBuffer().tryPublishEvent(event);
                    }
                    in.close();
                } catch (IOException e) {
                    System.out.println(line);
                    e.printStackTrace();
                }

//                barrier.await();
            }catch (Exception e){
                e.printStackTrace();
            }

        }

    }

}


