package netty;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TestSokect {
    private static final ExecutorService es = new ThreadPoolExecutor(2,2,2,TimeUnit.SECONDS,new ArrayBlockingQueue(100));
    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(8080);
        System.out.println("-----------------");
        ss.setSoTimeout(5000);
        for(;;){
            Socket s = ss.accept();
            InputStream is = s.getInputStream();
            es.submit(new MyTask(is));

        }
    }
}

class MyTask implements Runnable{
    private InputStream is;
    public MyTask(InputStream is){
        this.is = is;
    }
    @Override
    public void run() {
        for(;;){
            try {
                if(is.read()<-1){
                }
                System.out.println("----------------");
                Thread.sleep(1000);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
