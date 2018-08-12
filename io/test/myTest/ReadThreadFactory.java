
package log.io.test.myTest;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class ReadThreadFactory implements ThreadFactory {
    private static final AtomicInteger threadNo = new AtomicInteger(1);

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r,"readThread" + threadNo.getAndIncrement());
        return t;
    }

}
