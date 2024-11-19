package com.assignment.assignment.algorithms;

public class MergeSort implements SortingAlgorithm {

    @Override
    public void sort(double[] array) {
        mergeSort(array, 0, array.length - 1);
    }

    @Override
    public String getName() {
        return "Merge Sort";
    }

    private void mergeSort(double[] array, int left, int right) {
        if (left < right) {
            int middle = (left + right) / 2;
            mergeSort(array, left, middle);
            mergeSort(array, middle + 1, right);
            merge(array, left, middle, right);
        }
    }

    private void merge(double[] array, int left, int middle, int right) {
        int n1 = middle - left + 1;
        int n2 = right - middle;

        double[] leftArray = new double[n1];
        double[] rightArray = new double[n2];

        System.arraycopy(array, left, leftArray, 0, n1);
        System.arraycopy(array, middle + 1, rightArray, 0, n2);

        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) {
            if (leftArray[i] <= rightArray[j]) {
                array[k] = leftArray[i];
                i++;
            } else {
                array[k] = rightArray[j];
                j++;
            }
            k++;
        }

        while (i < n1) {
            array[k] = leftArray[i];
            i++;
            k++;
        }

        while (j < n2) {
            array[k] = rightArray[j];
            j++;
            k++;
        }
    }
}
