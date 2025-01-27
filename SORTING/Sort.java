package SORTING;

import java.util.Arrays;

public class Sort {
    public static void main(String[] args) {
        int data[] = {9, 2, 4, 5, 17, 1, 3};

        //insertionSort(data);
        //bobleSort(data);
        fletteSort(data, data[0], data[data.length-1]);
        for (int element : data) {
            System.out.print(element + " ");
        }


    }

    public static void insertionSort(int[] t){
        for(int j= 1; j < t.length; j++){ //starter på 2 element i array siden den første har ikke noe til venstre
            int temp = t[j];
            int i = j -1;
            while(i >= 0 && t[i] > temp){
                t[i+1] = t[i];
                i--;
            }
            t[i+1] = temp;
            }
        }


    public static void bobleSort(int[] t){
        for(int i= t.length-1; i> 0; i--){
            for(int j=0; j < i; j++){
                if(t[j]> t[j+1]){
                    bytt(t,j, j+1);
                }
            }
        }
    }

    public static void bytt(int[] t, int i, int j){
        int k  = t[j];
        t[j] = t[i];
        t[i] = k;
    }


    public static void velgesort(int []t) {
        for (int i = t.length - 1; i > 0; --i) {
            int max = 0;
            for (int j = 1; j < i; ++j) {
                if (t[j] > t[max]) max = j;
            }
            if (max != i) bytt(t, i, max);

        }
    }

    public static void fletteSort(int []t, int v, int h){
        if( v <h){
            int m = (v+h) / 2;
            fletteSort(t, v, m);
            fletteSort(t, m+1, h);
            flett(t, v, m, h);
        }
    }

    public static void flett( int[]t, int v, int m, int h) {
        int []ht = new int[h-v+1];
        int i = 0, j= v, k = m + 1;

        while (j <= m && k <= h) {
            ht[i++] = (t[j] <= t[k])?
            t[j++] : t[k++];
        }
        while (j <= m) ht[i++] = t[j++];
        for(i=v; i<k; ++i) t[i] = ht[i-v];
    }



}

