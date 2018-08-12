package Test;

import javax.annotation.concurrent.GuardedBy;
import java.util.concurrent.Semaphore;

public class BoundedBuffer<E> {
    private final Semaphore availableItems, availableSpaces;
    @GuardedBy("this") private final E[] items;
    @GuardedBy("this") private int putPosition = 0, takePosition = 0;

    public BoundedBuffer(int capacity){
        availableItems = new Semaphore(0);
        availableSpaces = new Semaphore(capacity);
        items = (E[])new Object[capacity];
    }

    public boolean isEmpty(){
        return availableItems.availablePermits() == 0;
    }

    public boolean isFull(){
        return availableSpaces.availablePermits() == 0;
    }

    public void put(E x) throws InterruptedException{
        availableSpaces.acquire();
        doInsert(x);
        availableItems.release();
    }

    public E take() throws InterruptedException{
        availableItems.acquire();
        E item = doExtract();
        availableSpaces.release();
        return item;
    }

    private synchronized void doInsert(E x){
        int i = putPosition;
        items[i] = x;
        putPosition = (++i == items.length)? 0 : i;
    }

    private synchronized E doExtract(){
        int i = takePosition;
        E x = items[i];
        items[i] = null;
        takePosition = (++i == items.length)? 0 : i;
        return x;
    }

    public static void main(String[] args) {
        final BoundedBuffer<Integer> bb = new BoundedBuffer<Integer>(10);
        Thread taker = new Thread(){
            public void run(){
                try {
                    int unsed = bb.take();
                    System.out.println("fail");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        try{
            taker.start();
            Thread.sleep(1000);
            taker.interrupt();
            taker.join();
            if (taker.isAlive()){
                System.out.println("taker is alive");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
