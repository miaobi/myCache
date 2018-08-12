package log.io.test;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import org.jetbrains.annotations.NotNull;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class TestDisruptor {
    private  static List<Msg> list = new ArrayList<>();
    private  Disruptor disruptor = new Disruptor(new WriteEventFactory(), 256*1024, new ThreadFactory(){
        @Override
        public Thread newThread(@NotNull Runnable r) {
            return new Thread(r);
        }
    }, ProducerType.MULTI, new TimeoutBlockingWaitStrategy(1000, TimeUnit.MILLISECONDS));

    public static void main(String[] args) {


        TestDisruptor td = new TestDisruptor();
        td.start();
        for (int i = 0; i < 10; i++) {
            td.tryPublishEvent("msg - - - [22/Oct/2017:00:00:42 +0800] cc"+i);
        }
        td.shutdown();

    }

    public void tryPublishEvent(String line){
        disruptor.getRingBuffer().tryPublishEvent(new WriteEventTranslator(line));
    }

    public void start(){
        disruptor.handleEventsWith(new WriteEventHandler());
        disruptor.start();

    }
    public void shutdown(){
        disruptor.shutdown();

    }


    static class WriteEventFactory implements EventFactory<Msg>{

        @Override
        public Msg newInstance() {
            return new Msg();
        }
    }


    static class WriteEventHandler implements SequenceReportingEventHandler<Msg>, LifecycleAware {


        @Override
        public void onEvent(Msg event, long sequence, boolean endOfBatch) throws Exception {
            //写文件
            list.add(event);
        }

        @Override
        public void onStart() {

        }

        @Override
        public void onShutdown() {

        }

        @Override
        public void setSequenceCallback(Sequence sequenceCallback) {

        }
    }

    static class WriteEventTranslator implements
            EventTranslator<Msg> {
        String msg;
        public WriteEventTranslator(String msg){
            this.msg = msg;
        }
        @Override
        public void translateTo(Msg msg, long sequence) {
            msg.setValue(this.msg);
            msg.setLdt(TestDate.getDate2(msg.getValue().split(" ")[4]));
        }
    }



}
