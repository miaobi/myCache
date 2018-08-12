package myDisruptor;

public class Singleton {
    private static volatile Disruptor disruptor;

    public static Disruptor getDisruptor() {
        if(disruptor==null){
            synchronized (Singleton.class) {
                if (disruptor == null) {
                    disruptor = new Disruptor();
                    disruptor.start();
                }
            }
        }
        return disruptor;
    }

}
