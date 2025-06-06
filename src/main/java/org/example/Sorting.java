package org.example;

import java.util.Random;
import java.util.Arrays;

public class Sorting {
    private int size;
    private SortingType sortingType;
    private final Random random = new Random();
    private int[] array;

    public void generateArray(int size) {
        this.size = size;
        array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(1000);
        }
    }

    public int[] sort() {
        if (sortingType == null) {
            throw new IllegalStateException("Sorting type must be set before sorting.");
        }

        if (array == null) {
            throw new IllegalStateException("Generate an array before sorting.");
        }

        switch (sortingType) {
            case MERGE -> mergeSort(array, 0, size - 1);
            case BUBBLE -> bubbleSort(array);
            case SELECTION -> selectionSort(array);
            case INSERTION -> insertionSort(array);
            case QUICK -> quickSort(array, 0, size - 1);
            default -> throw new IllegalArgumentException("Unknown sorting type: " + sortingType);
        }
        return array;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setSortingType(SortingType sortingType) {
        this.sortingType = sortingType;
    }

    public enum SortingType {
        BUBBLE, SELECTION, INSERTION, MERGE, QUICK
    }

    // ======================
    // Sorting Implementations
    // ======================

    private void bubbleSort(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = 0; j < arr.length - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    swap(arr, j, j + 1);
                }
            }
        }
    }

    private void selectionSort(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[j] < arr[minIdx]) {
                    minIdx = j;
                }
            }
            swap(arr, i, minIdx);
        }
    }

    private void insertionSort(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            int key = arr[i];
            int j = i - 1;
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
    }

    private void mergeSort(int[] arr, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSort(arr, left, mid);
            mergeSort(arr, mid + 1, right);
            merge(arr, left, mid, right);
        }
    }

    private void merge(int[] arr, int left, int mid, int right) {
        int[] temp = new int[right - left + 1];
        int i = left, j = mid + 1, k = 0;

        while (i <= mid && j <= right) {
            temp[k++] = (arr[i] <= arr[j]) ? arr[i++] : arr[j++];
        }
        while (i <= mid) temp[k++] = arr[i++];
        while (j <= right) temp[k++] = arr[j++];

        System.arraycopy(temp, 0, arr, left, temp.length);
    }

    private void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            int pi = partition(arr, low, high);
            quickSort(arr, low, pi - 1);
            quickSort(arr, pi + 1, high);
        }
    }

    private int partition(int[] arr, int low, int high) {
        int pivot = arr[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (arr[j] <= pivot) {
                i++;
                swap(arr, i, j);
            }
        }
        swap(arr, i + 1, high);
        return i + 1;
    }

    private void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    private Sorting() {} // private constructor

    public static class Builder {
        private int size;
        private SortingType sortingType;

        public Builder setSize(int size) {
            this.size = size;
            return this;
        }

        public Builder setSortingType(SortingType sortingType) {
            this.sortingType = sortingType;
            return this;
        }

        public Sorting build() {
            Sorting s = new Sorting();
            s.size = this.size;
            s.sortingType = this.sortingType;
            s.generateArray(s.size);
            return s;
        }
    }
}
