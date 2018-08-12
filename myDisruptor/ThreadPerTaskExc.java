package myDisruptor;

import java.util.concurrent.Executor;

public class ThreadPerTaskExc implements Executor {
    @Override
    public void execute(Runnable command) {
        new Thread(command).start();
    }

}