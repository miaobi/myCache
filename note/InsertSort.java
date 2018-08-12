package note;

public class InsertSort {
    public void sort(int[] array){
        for(int i = 1; i < array.length; i++){
            int temp = array[i];
            int j = i-1;
            for(;(j >= 0) && (array[j] > temp); j--){
                array[j+1] = array[j];
            }
            array[j+1] = temp;
        }
    }

    public static void main(String[] args) {
        InsertSort test = new InsertSort();
        int[] array = {2,4,3,1,5,6,3,4};
        test.sort(array);
        for (int i : array){
            System.out.println(i);
        }
    }
}
