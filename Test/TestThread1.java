package Test;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TestThread1{
    private static Lock lock = new ReentrantLock();
    private static Condition conditionSub = lock.newCondition();
    private static Condition conditionMain = lock.newCondition();

    public static class SubThread implements Runnable{
        private static int i = 0;
        private static int j = 0;
        public void run(){
            while (j < 4 ){
                lock.lock();
                System.out.println("sub");
                conditionMain.signal();
                try {
                    conditionSub.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    lock.unlock();
                }
                j++;
            }
        }
    }

    public static class MainThread implements Runnable{
        private static int i = 0;
        private static int j = 0;
        public void run(){
            while (j < 4 ){
                lock.lock();
                System.out.println("main");
                conditionSub.signal();
                try {
                    conditionMain.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    lock.unlock();
                }
                j++;
            }
        }
    }


    public static void main(String[] args) {
        Thread subThread = new Thread(new SubThread());
        subThread.start();
        Thread mainThread = new Thread(new MainThread());
        mainThread.start();
    }
}
