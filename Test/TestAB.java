package Test;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TestAB {
    Map map = new HashMap();
    static List<Integer> count =  new ArrayList();
    public static void main(String[] args) throws InterruptedException {
        int powerOf = 2;
        int segment = 1 << powerOf ;
        System.out.println("segment:"+segment);
        int segmentShift = 32 - powerOf;
        List<String> list = Lists.newArrayList();
        int loop = 100;
        for (int i = 0; i < 1000000; i++) {
            list.add(i+"01");
        }
        long start = System.currentTimeMillis();
        //hash code 算法分析 https://blog.csdn.net/sinat_31011315/article/details/78699655
        for (String key: list) {
//            int i =  (rehash(key.hashCode())>>>segmentShift)&(segment-1);//guava cache hash算法
//            int i =  Math.abs(key.hashCode()%segment);//string类默认hash算法。优点：分布较均匀；缺点：hash冲突严重，例如字符串”gdejicbegh”与字符串”hgebcijedg”具有相同的hashCode()返回值-801038016，并且它们具有reverse的关系。
            int i = (key.hashCode() ^ (key.hashCode() >>> 16))&(segment-1); //hashmap hash算法
            count.add(i);
        }
        System.out.println(System.currentTimeMillis()-start+" ms");


        Map<Object, List<Integer>> collect = count.stream().collect(
                Collectors.groupingBy(o -> o));
        List<String[]> collect2 = collect
                .entrySet()
                .stream()
                .map(e -> new String[] { String.valueOf(e.getKey()),
                        String.valueOf(e.getValue().size()) })
                .collect(Collectors.toList());

        collect2.forEach(o -> System.out.println("segment"+o[0] + " >> " + o[1]));

    }


    static int rehash(int h) {
        // Spread bits to regularize both segment and index locations,
        // using variant of single-word Wang/Jenkins hash.
        // TODO(kevinb): use Hashing/move this to Hashing?
        h += (h << 15) ^ 0xffffcd7d;
        h ^= (h >>> 10);
        h += (h << 3);
        h ^= (h >>> 6);
        h += (h << 2) + (h << 14);
        return h ^ (h >>> 16);
    }
}
