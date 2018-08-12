package Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test7 {
    public  static String getKey(String str){
        int[] counts = new int[26];
        for (int i = 0; i < 26; i++) {
            counts[i] = 0;
        }
        for (int i = 0; i < str.length(); i++) {
            counts[str.charAt(i) - 'a'] ++;
        }
        StringBuffer key = new StringBuffer();
        for (int i = 0; i < 26; i++){
            key.append(counts[i]);
        }
        return key.toString();
    }

    public static void main(String[] args) throws Exception {
        String[] strArray = new String[] { "fas", "eat", "tea", "sfa", "ate", "nat", "bat", "tab", "wwt", "wtw" };
        Map<String, List<String>> map = new HashMap<>();
        for (String str : strArray) {
            String key = getKey(str);
            System.out.println(key);
            if(map.containsKey(key)){
                List<String> list = map.get(key);
                list.add(str);
            }else {
                List<String> list = new ArrayList<>();
                list.add(str);
                map.put(key, list);
            }
        }

        StringBuffer sb = new StringBuffer();
        sb.append("[");
        for (String key : map.keySet()) {
            sb.append("[");
            for (String str : map.get(key)) {
                sb.append("\"").append(str).append("\",");
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append("],");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("]");
        System.out.println(sb.toString());
    }
}
