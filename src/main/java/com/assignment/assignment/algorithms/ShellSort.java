package com.assignment.assignment.algorithms;

public class ShellSort implements SortingAlgorithm {
    @Override
    public void sort(double[] array) {
        int n = array.length;
        for (int gap = n / 2; gap > 0; gap /= 2) {
            for (int i = gap; i < n; i++) {
                double temp = array[i];
                int j = i;
                while (j >= gap && array[j - gap] > temp) {
                    array[j] = array[j - gap];
                    j -= gap;
                }
                array[j] = temp;
            }
        }
    }

    @Override
    public String getName() {
        return "Shell Sort";
    }
}