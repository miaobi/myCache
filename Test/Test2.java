package Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test2 {

    public static String sort(String str) {
        //冒泡排序、时间复杂度O(n^2)
        // 利用toCharArray可将字符串转换为char型的数组
        char[] s1 = str.toCharArray();
        for (int i = 0; i < s1.length; i++) {
            for (int j = 0; j < i; j++) {
                if (s1[i] < s1[j]) {
                    char temp = s1[i];
                    s1[i] = s1[j];
                    s1[j] = temp;
                }
            }
        }
        // 再次将字符数组转换为字符串，也可以直接利用String.valueOf(s1)转换
        String st = new String(s1);
        return st;
    }

    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();

        String[] keys = new String[] { "fas", "eat", "tea", "tan", "ate", "nat", "bat", "ccc", "www", "vvv", "vv", "qq",
                "ee", "hh","ccc" };

        Map<String, List<String>> map = new HashMap<String, List<String>>();
        for (String key : keys) {
            String sortKey = sort(key);
            if (map.containsKey(sortKey)) {
                List<String> keyList = map.get(sortKey);
                keyList.add(key);
            } else {
                List<String> keyList = new ArrayList<String>();
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