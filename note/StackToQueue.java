package note;

import java.util.Stack;

/**
 * 两个栈实现队列
 */
public class StackToQueue {
    private Stack<Integer> stack1;
    private Stack<Integer> stack2;

    public StackToQueue(){
        this.stack1 = new Stack<>();
        this.stack2 = new Stack<>();
    }

    public void add(Integer i){
        stack1.push(i);
    }

    public void remove(){
        while (!stack1.empty()){
            stack2.push(stack1.pop());
        }
        stack2.pop();
        while (!stack2.empty()){
            stack1.push(stack2.pop());
        }
    }

    public static void main(String[] args) {
        StackToQueue test = new StackToQueue();
        test.add(1);
        test.add(2);
        test.add(3);
        test.add(4);
        test.add(5);
        test.remove();
    }

}
