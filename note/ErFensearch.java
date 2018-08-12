package note;

public class ErFensearch {
    public int search(int[] array, int low, int hight, int target){
        int mid = (int)(low + hight)/2;
        if(low < hight){
            if(target < array[mid]){
                System.out.println("search between " + array[low] +" and " + array[mid-1]);
                return search(array, low, mid-1, target);
            }
            if(target > array[mid]){
                System.out.println("search between " + array[mid+1] +" and " + array[hight]);
                return search(array, mid+1, hight, target);
            }
            System.out.println("target = " + array[mid]);
            return array[mid];
        }else {
            System.out.println("No find " + target);
            return -1;
        }
    }

    public static void main(String[] args) {
        int[] array = {3,14,25,37,38,39,40,42,45,49,50,54,58,59,60,65};
        ErFensearch erFensearch = new ErFensearch();
        int i = erFensearch.search(array, 0, array.length-1, 31);
    }
}
