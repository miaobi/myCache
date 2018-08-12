package Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Test6 {

    public static void main(String[] arg){
        Integer[] a ={5,2,3,4,6,8,7,1};
        Stream.of(a).forEach(x -> System.out.print(x+","));
        System.out.println();
        int j;
        for(int p=1; p<a.length; p++) {
            System.out.println("----------"+p+"--------");
            Integer tmp = a[p];
            System.out.println("tmp="+tmp);
            System.out.println("*******for************");
            for (j = p; j > 0 && tmp.compareTo(a[j - 1]) < 0; j--) {
                a[j] = a[j - 1];
                Stream.of(a).forEach(x -> System.out.print(x+","));
                System.out.println();
            }
            System.out.println("********for************");
            a[j] = tmp;
            Stream.of(a).forEach(x -> System.out.print(x+","));
            System.out.println();
            System.out.println("----------"+p+"--------");
        }
        Stream.of(a).forEach(x -> System.out.print(x+","));
    }
}
