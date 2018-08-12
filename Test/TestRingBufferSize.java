package Test;

/**
 *  环形数组的实现方法（有序）
 */
public class TestRingBufferSize {
    public static void main(String[] args) {
        int x = 10;
        int i = 1 << (32 - Integer.numberOfLeadingZeros(x - 1));//把数字变成2的n次方
        System.out.println(i);
        int[] segments = new int[i];
        int segment = i;//segment 要是2的n次方 下面2个等式相等
        int c = 7;
//        System.out.println(c & (segment-1));
//        System.out.println(c % segment);
        int index = 0;
        for (int j = 0; j < 100; j++) {
            index = j & (segments.length-1);
            System.out.println("j="+j+",index:"+index);
        }

    }
}
