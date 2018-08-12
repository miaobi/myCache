package note;

import java.util.LinkedList;
import java.util.Stack;
import java.util.TreeSet;

/**
 * 倒序打印链表
 */
public class PrintLinked {
    public static void print(LinkedList<Integer> list){
        Stack<Integer> st = new Stack<>();
        for(Integer i :list){
            st.push(i);
        }
        while (!st.empty()){
            System.out.println(st.pop() + " ");
        }
    }

    public static void main(String[] args) {
        LinkedList<Integer> list = new LinkedList<>();
        list.add(5);
        list.add(4);
        list.add(3);
        list.add(2);
        list.add(1);
        print(list);
    }
}
