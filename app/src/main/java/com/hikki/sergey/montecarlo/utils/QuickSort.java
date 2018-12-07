package com.hikki.sergey.montecarlo.utils;

public class QuickSort {
    private static double[] array;
    private static double[][] array2D;

    public static double[] quickSort(double[] arr){
        array = arr;
        int startIndex = 0;
        int endIndex = arr.length - 1;
        doSort(startIndex, endIndex);
        return array;
    }

    private static void doSort(int start, int end) {
        if (start >= end)
            return;
        int i = start, j = end;
        int cur = i - (i - j) / 2;
        while (i < j) {
            while (i < cur && (array[i] <= array[cur])) {
                i++;
            }
            while (j > cur && (array[cur] <= array[j])) {
                j--;
            }
            if (i < j) {
                double temp = array[i];
                array[i] = array[j];
                array[j] = temp;
                if (i == cur)
                    cur = j;
                else if (j == cur)
                    cur = i;
            }
        }
        doSort(start, cur);
        doSort(cur+1, end);
    }

    public static double[][] quickSort(double[][] arr){
        array2D = arr;
        int startIndex = 0;
        int endIndex = arr.length - 1;
        doSort2D(startIndex, endIndex);
        return array2D;
    }

    private static void doSort2D(int start, int end){
        if (start >= end)
            return;

        int i = start, j = end;
        int cur = i - (i - j) / 2;

        while (i < j) {
            while (i < cur && (array2D[i][0] <= array2D[cur][0])) {
                i++;
            }
            while (j > cur && (array2D[cur][0] <= array2D[j][0])) {
                j--;
            }
            if (i < j) {
                double[] temp = array2D[i];
                array2D[i] = array2D[j];
                array2D[j] = temp;
                if (i == cur)
                    cur = j;
                else if (j == cur)
                    cur = i;
            }
        }
        doSort2D(start, cur);
        doSort2D(cur+1, end);
    }
}
