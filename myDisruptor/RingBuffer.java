package myDisruptor;

import com.lmax.disruptor.util.Util;
import sun.misc.Unsafe;


import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.util.concurrent.TimeUnit.SECONDS;

abstract class RingBufferPad
{
    protected long p1, p2, p3, p4, p5, p6, p7;
}

abstract class RingBufferFields<E> extends RingBufferPad
{
    private static final int BUFFER_PAD;
    private static final long REF_ARRAY_BASE;
    private static final int REF_ELEMENT_SHIFT;
//    private static final Unsafe UNSAFE;
    private static final Unsafe UNSAFE = Util.getUnsafe();

    static
    {
//        UNSAFE = Unsafe.getUnsafe();
        final int scale = UNSAFE.arrayIndexScale(Object[].class);
        if (4 == scale) {
            REF_ELEMENT_SHIFT = 2;
        } else if (8 == scale) {
            REF_ELEMENT_SHIFT = 3;
        } else {
            throw new IllegalStateException("Unknown pointer size");
        }
        BUFFER_PAD = 128 / scale;
        // Including the buffer pad in the array base offset
        REF_ARRAY_BASE = UNSAFE.arrayBaseOffset(Object[].class) + (BUFFER_PAD << REF_ELEMENT_SHIFT);
    }

    protected final long indexMask;
    protected Event[] entries;
    protected final int bufferSize;
    protected final Sequencer sequencer;

    RingBufferFields(Sequencer sequencer) {
        this.sequencer = sequencer;
        this.bufferSize = sequencer.getBufferSize();
        this.indexMask = bufferSize - 1;
        this.entries = new Event[sequencer.getBufferSize() + 2 * BUFFER_PAD];
        fill();
    }

    private void fill() {
        for (int i = 0; i < bufferSize; i++) {
            entries[BUFFER_PAD + i] = null;
        }
    }

    @SuppressWarnings("unchecked")
    protected final E elementAt(long sequence) {
        return (E) UNSAFE.getObject(entries, REF_ARRAY_BASE + ((sequence & indexMask) << REF_ELEMENT_SHIFT));
    }

    public E get(long sequence) {
        return elementAt(sequence);
    }
}

public class RingBuffer<E> extends RingBufferFields<E>{
    public static final long INITIAL_CURSOR_VALUE = -1L;
    protected long p1, p2, p3, p4, p5, p6, p7;
    private final Lock lock = new ReentrantLock();
    private final Condition processorNotifyCondition = lock.newCondition();

    RingBuffer(Sequencer sequencer)
    {
        super(sequencer);
    }

    public boolean tryPublishEvent(Event event)
    {
        try
        {
            final long sequence = sequencer.tryNext();
            int index = sequencer.calculateIndex(sequence);
            entries[index] = event;
            sequencer.setAvailable(sequence);
            signalAllWhenBlocking();
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public void signalAllWhenBlocking()
    {
        lock.lock();
        try
        {
            processorNotifyCondition.signalAll();
        }
        finally
        {
            lock.unlock();
        }
    }

    public long waitFor(final long sequence) {
        long availableSequence;
        if (sequencer.getCursor() < sequence)
        {
            lock.lock();
            try
            {
                while (sequencer.getCursor() < sequence)
                {
                    processorNotifyCondition.await(1, SECONDS);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
        availableSequence = sequencer.getCursor();
        return sequencer.getHighestPublishedSequence(sequence, availableSequence);
    }
}
