package com.griffiths.hugh.declarative_knitting.images;

public class ArrayUtils {
	public static boolean getSafeValue(boolean[] row, int position) {
		if (position < 0 || position >= row.length)
			return false;
		else
			return row[position];
	}

	public static void printBooleanArray(boolean[] row) {
		for (int i = 0; i < row.length; i++) {
			System.out.print(row[i] ? 1 : "-");
		}
		System.out.println();
	}
}
