package com.griffiths.hugh.declarative_knitting.images.lace.cellular_automata;

import java.util.ArrayList;
import java.util.List;

import static com.griffiths.hugh.declarative_knitting.images.lace.ArrayUtils.getSafeValue;
import static com.griffiths.hugh.declarative_knitting.images.lace.ArrayUtils.printBooleanArray;

public class CellularAutomataGenerator {

	public List<boolean[]> generateCellularAutomaton(boolean[] rule, int numRows) {
		if (rule.length != 8)
			throw new IllegalArgumentException();
		printBooleanArray(rule);
		System.out.println();

		final int width = 2 * numRows + 1;
		List<boolean[]> rows = new ArrayList<>();

		// Set up the first row
		boolean[] prevRow = new boolean[width];
		prevRow[prevRow.length / 2] = true;
		rows.add(prevRow);

		for (int i = 0; i < numRows; i++) {
//			printBooleanArray(prevRow);
			boolean[] nextRow = new boolean[width];
			for (int j = 0; j < nextRow.length; j++) {
				int ruleNum = getRuleIndex(prevRow, j);
//				System.out.print(ruleNum);
				nextRow[j] = rule[ruleNum];
			}
//			System.out.println();

			rows.add(nextRow);
			prevRow = nextRow;
		}

		return rows;
	}

	private int getRuleIndex(boolean[] prevRow, int position) {
		int ruleToApply = 0;
		if (getSafeValue(prevRow, position - 1))
			ruleToApply += 4;
		if (getSafeValue(prevRow, position))
			ruleToApply += 2;
		if (getSafeValue(prevRow, position + 1))
			ruleToApply += 1;

		return ruleToApply;
	}
}
