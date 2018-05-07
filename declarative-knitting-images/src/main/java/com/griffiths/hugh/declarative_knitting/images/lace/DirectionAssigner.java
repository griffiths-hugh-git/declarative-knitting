package com.griffiths.hugh.declarative_knitting.images.lace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import static com.griffiths.hugh.declarative_knitting.images.lace.ArrayUtils.getSafeValue;

public class DirectionAssigner {
	private static Map<Integer, Character> characterMap = getCharacterMap();

	private static Map<Integer, Character> getCharacterMap() {
		Map<Integer, Character> characterMap = new HashMap<>();
		characterMap.put(-1, '/');
		characterMap.put(0, '|');
		characterMap.put(1, '\\');

		return characterMap;
	}

	public static void printDirectionsArray(int[] directions) {
		for (int i = 0; i < directions.length; i++) {
			char symbol = characterMap.containsKey(directions[i]) ? characterMap.get(directions[i]) : '-';

			System.out.print(symbol);
		}
		System.out.println();
	}

	public List<int[]> assignDirections(List<boolean[]> rows) {
		final Queue<boolean[]> rowsToProcess = new LinkedList<>(rows);
		List<int[]> directionList = new ArrayList<>();
		final int length = rows.get(0).length;

		// First row is skipped - we can't assign directions yet
		boolean[] lastRow = rowsToProcess.poll();

		while (!rowsToProcess.isEmpty()) {
			boolean[] currentRow = rowsToProcess.poll();
			int[] directions = new int[length];
			for (int i = 0; i < length; i++) {
				// Inactive cells are all Integer.MIN_VALUE
				final int direction;
				if (currentRow[i]) {
					if (getSafeValue(lastRow, i - 1))
						direction = 1;
					else if (getSafeValue(lastRow, i + 1))
						direction = -1;
					else
						direction = 0;

				} else {
					direction = Integer.MIN_VALUE;
				}
				directions[i] = direction;

			}

			directionList.add(directions);
			lastRow = currentRow;
		}

		return directionList;
	}
}
