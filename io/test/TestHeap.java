package log.io.test;

import java.time.LocalDateTime;

public class TestHeap {
    private Comparable[] arr;

    public TestHeap(){

    }

    public void sort(Comparable[] arr){
        this.arr = arr;
        sort();
    }

    /**
     * 堆排序的主要入口方法，共两步。
     */
    private void sort(){
        /*
         *  第一步：将数组堆化
         *  beginIndex = 第一个非叶子节点。
         *  从第一个非叶子节点开始即可。无需从最后一个叶子节点开始。
         *  叶子节点可以看作已符合堆要求的节点，根节点就是它自己且自己以下值为最大。
         */
        int len = arr.length - 1;
        int beginIndex = (len - 1) >> 1;
        for(int i = beginIndex; i >= 0; i--){
            maxHeapify(i, len);
        }

        /*
         * 第二步：对堆化数据排序
         * 每次都是移出最顶层的根节点A[0]，与最尾部节点位置调换，同时遍历长度 - 1。
         * 然后从新整理被换到根节点的末尾元素，使其符合堆的特性。
         * 直至未排序的堆长度为 0。
         */
        for(int i = len; i > 0; i--){
            swap(0, i);
            maxHeapify(0, i - 1);
        }
    }

    private void swap(int i,int j){
        Comparable temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    /**
     * 调整索引为 index 处的数据，使其符合堆的特性。
     *
     * @param index 需要堆化处理的数据的索引
     * @param len 未排序的堆（数组）的长度
     */
    private void maxHeapify(int index,int len){
        int li = (index << 1) + 1; // 左子节点索引
        int ri = li + 1;           // 右子节点索引
        int cMax = li;             // 子节点值最大索引，默认左子节点。

        if(li > len) return;       // 左子节点索引超出计算范围，直接返回。
        if(ri <= len && isRightLarger(ri ,li)) // 先判断左右子节点，哪个较大。
            cMax = ri;
        if(isSwap(cMax,index)){
            swap(cMax, index);      // 如果父节点被子节点调换，
            maxHeapify(cMax, len);  // 则需要继续判断换下后的父节点是否符合堆的特性。
        }
    }

    //右边更大
    private boolean isRightLarger(int ri ,int li){
        return arr[ri].compareTo(arr[li])>0;
    }

    //右边更大
    private boolean isSwap(int cMax ,int index){
        return arr[cMax].compareTo(arr[index])>0;
    }

    /**
     * 测试用例
     *
     * 输出：
     * [0, 0, 0, 1, 1, 1, 2, 2, 2, 3, 3, 3, 4, 4, 4, 5, 5, 5, 6, 6, 6, 7, 7, 7, 8, 8, 8, 9, 9, 9]
     */
    public static void main(String[] args) {
        String goodFriday = "[22/Oct/2017:00:00:41 +0800]";
        String date2 = "[22/Oct/2017:00:00:42 +0800]";
        LocalDateTime holiday = TestDate.getDate(goodFriday);
        LocalDateTime holiday2 = TestDate.getDate(date2);
        Msg msg1 = new Msg();
        Msg msg2 = new Msg();
        msg1.setLdt(holiday);
        msg2.setLdt(holiday2);
        msg1.setValue("11.135.97.81 - - - [22/Oct/2017:00:00:41 +0800] \"GET /zunyi.1688.com/qiye/-bba7cde2d2b0d3aac2b6d3aacbafb4fc.htm HTTP/1.1\" 302 262 27282 \"-\" \"Sogou web spider/4.0(+http://www.sogou.com/docs/help/webmasters.htm#07)\" - - \"a=-; b=-; c=-\" - 12829 industry-web011183236062.et2\n");
        msg2.setValue("11.128.44.230 - - - [22/Oct/2017:00:00:42 +0800] \"GET /yiwu.1688.com/offer/1281257794.html HTTP/1.1\" 200 268393 468869 \"-\" \"Mozilla/5.0 (compatible; bingbot/2.0; +http://www.bing.com/bingbot.htm)\" - - \"a=-; b=-; c=-\" - 44566 industry-web011183236062.et2\n");
        Msg[] arr = new Msg[]{msg1,msg2};
        new TestHeap().sort(arr);
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i]);
        }
    }
}
