package com.assignment.assignment.algorithms;

public class QuickSort implements SortingAlgorithm {

    @Override
    public void sort(double[] array) {
        quickSort(array, 0, array.length - 1);
    }

    @Override
    public String getName() {
        return "Quick Sort";
    }

    private void quickSort(double[] array, int low, int high) {
        while (low < high) {
            int pivot = medianOfThree(array, low, high);
            pivot = partition(array, low, high, pivot);

            if (pivot - low < high - pivot) {
                quickSort(array, low, pivot - 1);
                low = pivot + 1;
            } else {
                quickSort(array, pivot + 1, high);
                high = pivot - 1;
            }
        }
    }

    private int medianOfThree(double[] array, int low, int high) {
        int mid = low + (high - low) / 2;

        if (array[low] > array[mid]) {
            swap(array, low, mid);
        }
        if (array[mid] > array[high]) {
            swap(array, mid, high);
        }
        if (array[low] > array[mid]) {
            swap(array, low, mid);
        }

        swap(array, mid, high - 1);
        return high - 1;
    }

    private void swap(double[] array, int i, int j) {
        double temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    private int partition(double[] array, int low, int high, int pivotIndex) {
        double pivotValue = array[pivotIndex];
        swap(array, pivotIndex, high);

        int storeIndex = low;
        for (int i = low; i < high; i++) {
            if (array[i] < pivotValue) {
                swap(array, i, storeIndex);
                storeIndex++;
            }
        }

        swap(array, storeIndex, high);
        return storeIndex;
    }
}
