package Test;

import java.util.ArrayList;
import java.util.List;


public class StringOomMock {
    public static void main(String[] args) throws InterruptedException {
        try{
            List<String> list = new ArrayList<>();
            for (int i = 0; ;i++) {
                System.out.println(i);
                list.add(String.valueOf("String" + i++).intern());
                Thread.sleep(5);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
