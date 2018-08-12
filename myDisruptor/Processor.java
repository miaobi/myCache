package myDisruptor;

import com.lmax.disruptor.Sequence;

import java.time.DayOfWeek;
import java.util.LinkedList;
import java.util.concurrent.*;

public class Processor<T> implements Runnable {
    private final Sequence sequence = new Sequence(-1L);
    private final RingBuffer<T> ringBuffer;
    private final Sequence cursorSequence;
    private final Sequencer sequencer;
    private ThreadPoolExecutor[] execGroup = new ThreadPoolExecutor[7];
    private static final int  THREAD_NUM = 10;
    private final ThreadPerTaskExc buildIndexExec = new ThreadPerTaskExc();

    Processor(final RingBuffer<T> ringBuffer, final Sequencer sequencer, final Sequence cursorSequence) {
        this.ringBuffer = ringBuffer;
        this.sequencer = sequencer;
        this.cursorSequence = cursorSequence;
        for(int i = 0; i < 7; i++){
            execGroup[i] = new ThreadPoolExecutor(1, 1,
                    0L, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<Runnable>());
        }
    }

    @Override
    public void run()
    {
        Event event = null;
        long nextSequence = sequence.get() + 1L;

        while (true)
        {
            final long availableSequence = ringBuffer.waitFor(nextSequence);
            while (nextSequence <= availableSequence)
            {
                Event[] entries = ringBuffer.entries;
                int index = sequencer.calculateIndex(nextSequence);
                event = entries[index];
                int weekIndex = getWeekIndex(event.getLdt().getDayOfWeek());
                String content = event.getValue();
                execGroup[weekIndex-1].execute(new WriteFile(  String.valueOf(weekIndex) + ".log", content));
                nextSequence++;
            }
            sequence.set(availableSequence);
        }
    }

    public int getWeekIndex(DayOfWeek week){
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
        return 0;
    }


}
