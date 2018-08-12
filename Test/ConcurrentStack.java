package Test;

import java.util.concurrent.atomic.AtomicReference;

public class ConcurrentStack<E> {
    //栈顶元素定义为原子变量
    AtomicReference<Node<E>> top = new AtomicReference<Node<E>>();

    //创建新节点，使新节点的next域指向当前的栈顶，用CAS把新节点放入栈顶
    public void push(E item){
        Node<E> newHead = new Node<E>(item);
        Node<E> oldHead;
        do{
            oldHead = top.get();
            newHead.next = oldHead;
        }while (!top.compareAndSet(oldHead, newHead));
    }

    //将栈顶节点取出，用CAS将栈顶节点的下一个节点设为新栈顶
    public E pop(){
        Node<E> oldNode;
        Node<E> newNode;
        do{
            oldNode = top.get();
            if(oldNode == null){
                return null;
            }
            newNode = oldNode.next;
        }while (!top.compareAndSet(oldNode, newNode));
        return oldNode.item;
    }

    private static class Node<E>{
        public final E item;
        public Node<E> next;

        public Node(E item){
            this.item = item;
        }
    }
}
