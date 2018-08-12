package myDisruptor;

import com.lmax.disruptor.Sequence;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Disruptor<T>{
    private final RingBuffer<T> ringBuffer;
    private final Processor<T> processor;
    private final AtomicBoolean started = new AtomicBoolean(false);
    private static ExecutorService exec;
    private static final AtomicInteger threadNumber1=new AtomicInteger(1);

    public Disruptor(){
        Sequencer sequencer = new Sequencer();
        this.ringBuffer = new RingBuffer(sequencer);
        Sequence cursorSequence = new Sequence(-1L);
        this.processor = new Processor(ringBuffer, sequencer, cursorSequence);
    }

    public RingBuffer<T> getRingBuffer()
    {
        return ringBuffer;
    }

    public RingBuffer<T> start()
    {
        checkOnlyStartedOnce();
        exec = Executors.newFixedThreadPool(1, new ThreadFactory(){
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r,"myConsumerThread"+threadNumber1.getAndIncrement());
//                t.setDaemon(true);
                return t;
            }
        });
        exec.execute(processor);
        return ringBuffer;
    }

    private void checkOnlyStartedOnce()
    {
        if (!started.compareAndSet(false, true))
        {
            throw new IllegalStateException("Disruptor.start() must only be called once.");
        }
    }

    public boolean close(){
        exec.shutdown();
        return true;
    }
}
