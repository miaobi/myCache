package Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test3 {
    private static final int[] count = new int[26];
    public static String sort(String str) {
        //小写字母只有26位
        for (int i = 0; i < 26; i++) {
            count[i] = 0;
        }
        //http://www.runoob.com/java/java-string-getbytes.html
        //https://blog.csdn.net/changlei_shennan/article/details/68059904
        //https://blog.csdn.net/swingseagull/article/details/6456523
        //时间复杂度O(n)
        for (int asc : str.getBytes()) {
            //asc-97 (97就是小写a的ascii码十进制的值) https://baike.baidu.com/item/ASCII/309296?fr=aladdin
            count[asc-97]++;
        }
        StringBuffer sb = new StringBuffer();
        //时间复杂度是常量26
        for (int c:count) {
            sb.append(c);
        }
        return sb.toString();
    }

    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();

        String[] keys = new String[] { "fas", "eat", "tea", "tan", "ate", "nat", "bat", "ccc", "www", "vvv", "vv", "qq",
                "ee", "hh","ccc","eee" };

        Map<String, List<String>> map = new HashMap<String, List<String>>();
        for (String key : keys) {
            String sortKey = sort(key);
            if (map.containsKey(sortKey)) {
                List<String> keyList = map.get(sortKey);
                keyList.add(key);
            } else {
                List<String> keyList = new ArrayList<>();
                keyList.add(key);
                map.put(sortKey, keyList);
            }
        }

        StringBuffer sb = new StringBuffer();
        sb.append("[");
        for (String sortKey : map.keySet()) {
            sb.append("[");
            for (String key : map.get(sortKey)) {
                sb.append("\"").append(key).append("\",");
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append("],");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("]");
        System.out.println(sb.toString());

        System.out.println(System.currentTimeMillis() - start + " ms");
    }
}