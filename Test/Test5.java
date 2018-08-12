package Test;

import java.util.*;
import java.util.stream.Stream;

public class Test5 {
    public static void main(String[] arg) {
        Integer[] a = {1, 2, 3, 4, 5, 6};
        quicksort(a, 0, a.length - 1);
        Stream.of(a).forEach(x -> System.out.print(x + ","));
    }

    public static void swapReferences(Integer[] a, int i, int j) {
        Integer tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
    }

    public static Integer median3(Integer[] a, int left, int right) {
        int center = (left + right) / 2;
        if (a[center] < a[left]) {
            swapReferences(a, left, center);
        }
        if (a[right] < a[left]) {
            swapReferences(a, left, right);
        }
        if (a[right] < a[center]) {
            swapReferences(a, center, right);
        }
        swapReferences(a, center, right-1);
        return a[right-1];
    }

    private static void quicksort(Integer[] a, int left, int right) {
        if(left < right) {
            Integer pivot = median3(a, left, right);
            int i = left;
            int j = right - 1;
            for (; ; ) {
                while (a[i] < pivot) {
                    i++;
                }
                while (a[j] > pivot) {
                    j--;
                }
                if (i < j) {
                    swapReferences(a, i, j);
                } else {
                    break;
                }
            }
            swapReferences(a, i, right - 1);
            quicksort(a, left, i - 1);
            quicksort(a, i + 1, right);
        }
    }
}
