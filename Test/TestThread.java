package Test;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TestThread {
    private static Lock lock = new ReentrantLock();
    private static Condition conditionA = lock.newCondition();
    private static Condition conditionB = lock.newCondition();
    private static Condition conditionC = lock.newCondition();
    private static int count = 0;

    public static class PrintA implements Runnable{
        public void run() {
            while (count < 15) {
                lock.lock();
                System.out.print("A");
                try {
                    conditionB.signal();
                    conditionA.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }
    }
    public static class PrintB implements Runnable{
        public void run() {
            while (count < 15) {
                lock.lock();
                System.out.print("B");
                try {
                    conditionC.signal();
                    conditionB.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }
    }
    public static class PrintC implements Runnable{
        public void run() {
            while (count < 15) {
                lock.lock();
                System.out.print("C");
                count++;
                try {
                    conditionA.signal();
                    conditionC.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    public static void main(String[] args) {
        new Thread(new PrintA()).start();
        new Thread(new PrintB()).start();
        new Thread(new PrintC()).start();
    }
}
