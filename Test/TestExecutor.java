package Test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.*;

public class TestExecutor {
    private static Logger logger = LogManager.getLogger(TestExecutor.class.getName());
    static class MyTask implements Runnable {
        private int taskNum;

        public MyTask(int num) {
            this.taskNum = num;
        }

        @Override
        public void run() {
            System.out.println("RuningTask "+taskNum);
            try {
                Thread.currentThread().sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("task "+taskNum+" Completed");
        }
    }

    public static void main(String[] args) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, 200, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(5));
        for (int i = 0; i < 15; i++){
            executor.execute(new MyTask(i));
            System.out.println("PoolSize: "+ executor.getPoolSize()+", QueueSize: "+
                    executor.getQueue().size()+", CompletedTaskCount: "+executor.getCompletedTaskCount());
        }
        executor.shutdown();
        logger.error("executor shutdown");
    }




}
