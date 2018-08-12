package myDisruptor;

import com.lmax.disruptor.Sequence;
import com.lmax.disruptor.util.Util;
import sun.misc.Unsafe;

public class Sequencer {
    private static final Unsafe UNSAFE = Util.getUnsafe();
    private static final long BASE = UNSAFE.arrayBaseOffset(int[].class);
    private static final long SCALE = UNSAFE.arrayIndexScale(int[].class);
    private static long INITIAL_CURSOR_VALUE = -1L;

    private final Sequence gatingSequenceCache = new Sequence(INITIAL_CURSOR_VALUE);
    protected final Sequence cursor = new Sequence(INITIAL_CURSOR_VALUE);
    protected volatile Sequence[] gatingSequences = new Sequence[0];

    protected final int bufferSize;
    private final int[] availableBuffer;
    private final int indexMask;
    private final int indexShift;

    public final long getCursor()
    {
        return cursor.get();
    }

    public final int getBufferSize()
    {
        return bufferSize;
    }

    public Sequencer()
    {
        this.bufferSize = 4096;
        availableBuffer = new int[bufferSize];
        indexMask = bufferSize - 1;
        indexShift = log2(bufferSize);
        initialiseAvailableBuffer();
    }

    public static int log2(int i)
    {
        int r = 0;
        while ((i >>= 1) != 0)
        {
            ++r;
        }
        return r;
    }

    private void initialiseAvailableBuffer()
    {
        for (int i = availableBuffer.length - 1; i != 0; i--)
        {
            setAvailableBufferValue(i, -1);
        }

        setAvailableBufferValue(0, -1);
    }

    private void setAvailableBufferValue(int index, int flag)
    {
        long bufferAddress = (index * SCALE) + BASE;
        UNSAFE.putOrderedInt(availableBuffer, bufferAddress, flag);
    }

    public long getHighestPublishedSequence(long lowerBound, long availableSequence)
    {
        for (long sequence = lowerBound; sequence <= availableSequence; sequence++)
        {
            if (!isAvailable(sequence))
            {
                return sequence - 1;
            }
        }

        return availableSequence;
    }

    public boolean isAvailable(long sequence)
    {
        int index = calculateIndex(sequence);
        int flag = calculateAvailabilityFlag(sequence);
        long bufferAddress = (index * SCALE) + BASE;
        return UNSAFE.getIntVolatile(availableBuffer, bufferAddress) == flag;
    }

    public int calculateAvailabilityFlag(final long sequence)
    {
        return (int) (sequence >>> indexShift);
    }

    public int calculateIndex(final long sequence)
    {
        return ((int) sequence) & indexMask;
    }

    public long tryNext(){
        long current;
        long next;
        do {
            current = cursor.get();
            next = current + 1;
            if (!hasAvailableCapacity(gatingSequences, 1, current)) {
                throw new IllegalArgumentException("hasAvailableCapacity fail");
            }
        } while(!cursor.compareAndSet(current, next));
        return next;
    }

    private boolean hasAvailableCapacity(Sequence[] gatingSequences, final int requiredCapacity, long cursorValue)
    {
        long wrapPoint = (cursorValue + requiredCapacity) - bufferSize;
        long cachedGatingSequence = gatingSequenceCache.get();

        if (wrapPoint > cachedGatingSequence || cachedGatingSequence > cursorValue)
        {
            long minSequence = getMinimumSequence(gatingSequences, cursorValue);
            gatingSequenceCache.set(minSequence);
            if (wrapPoint > minSequence)
            {
                return false;
            }
        }
        return true;
    }

    public static long getMinimumSequence(final Sequence[] sequences, long minimum)
    {
        for (int i = 0, n = sequences.length; i < n; i++)
        {
            long value = sequences[i].get();
            minimum = Math.min(minimum, value);
        }
        return minimum;
    }

    public void setAvailable(final long sequence)
    {
        setAvailableBufferValue(calculateIndex(sequence), calculateAvailabilityFlag(sequence));
    }




}
