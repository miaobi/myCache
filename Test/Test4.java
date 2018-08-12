package Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

public class Test4 {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();
        sc.close();
        char strArr[] = s.toCharArray();
        Stack<Character> stack = new Stack<>();
        List<Character> list = new ArrayList<>();
        int i = strArr.length - 1;
        while(i >= 0){
            if((strArr[i]>= 'a' && strArr[i] <= 'z') || (strArr[i]>= 'A' && strArr[i] <= 'Z' )){
                stack.push(strArr[i]); //如果是字母，则入栈。
                i--;
            }
            else{
                //这里判断栈不为空，如果是连续的非字母字符，则进不去循环，从而避免了结果出现连续空格的情况。
                while(!stack.empty()){
                    list.add(stack.pop());
                    if(stack.empty() && i != 0)
//之所以并上i不为0这个条件，是因为如果字符串开头是非字母字符，那么那么没有i ！= 0这个条件的话输出的最后一个单词后面会有一个空格。所以我将最后一个单词放在循环外面输出，也就是下面的while循环。
                        list.add(' ');
                }
                i--;
            }
        }
        while(!stack.empty()){
            list.add(stack.pop());
        }
        for(int j = 0 ; j < list.size() ; j++){
            System.out.print(list.get(j));
        }
    }
}
