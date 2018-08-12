package Test;

import java.util.*;

public class Test {

    public static void main(String[] args) throws InterruptedException {
        long start = System.currentTimeMillis();
        String[] source = new String[]{"fas","eat","tea","tan","ate","nat","bat","ccc","www","vvv","vv","qq","ee","hh"};
        Map<String,List<String>> map = new HashMap();
        for (int i = 0; i < source.length; i++) {
            String e = source[i];
            String key = toKey(e);
            if(map.containsKey(key)){
                map.get(key).add(e);
            }else{
                //map.put(key, Lists.newArrayList(e));
                List<String> keyList = new ArrayList();
                keyList.add(key);
                map.put(key, keyList);
            }
        }
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        for (String key : map.keySet()) {
            sb.append(map.get(key)).append(",");
        }
        sb.deleteCharAt(sb.length()-1);
        sb.append("]");
        System.out.println(sb.toString());
        System.out.println(System.currentTimeMillis()-start + " ms");
        Thread.sleep(60000);
    }

    public static String toKey(String s1){
        char[] b1 = s1.toCharArray();
        //双轴快速排序 时间复杂度O(nlogn)
        //DualPivotQuicksort.sort();
        //https://blog.csdn.net/xjyzxx/article/details/18465661
        //英文书名叫《Hacker's Delight》，中文名叫《算法心得：高效算法的奥秘》
        Arrays.sort(b1);
        return new String(b1);
    }
}
