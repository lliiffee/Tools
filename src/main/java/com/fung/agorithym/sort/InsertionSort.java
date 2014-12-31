package com.fung.agorithym.sort;

public class InsertionSort {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	/**
	 * 插入排序
	 * 
	 * @paramarr
	 * @return
	 */
	private static int[] insertSort(int[] arr) {
		for (int i = 1; i < arr.length; i++) {
			for (int j = i; j > 0; j--) {
				if (arr[j] < arr[j - 1]) {
					// TODO:
					int temp = arr[j];
					arr[j] = arr[j - 1];
					arr[j - 1] = temp;
				} else {
					// 接下来是无用功
					break;
				}
			}
		}
		return arr;
	}

}
