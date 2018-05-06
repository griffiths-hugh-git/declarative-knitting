package com.griffiths.hugh.declarative_knitting.core.rendering.util;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.StringJoiner;
import org.apache.commons.lang3.StringUtils;

public class RowCompressUtil {
	public static final String DELIMITER = ", ";
	private static final String MULTI_CHAR_REPEAT_FORMAT = "* %s * %d times";
	private static final String SINGLE_CHAR_REPEAT_FORMAT = "%s %d";

	public static String compressLongestBlock(List<String> symbols) {
		// Look for longest repeated block
		int bestReduction = -1;
		int bestBlockStart = 0;
		int bestBlockLength = 0;
		int bestNumRepeats = 1;
		int minimumBestBlockLength = 1;

		for (int blockStart = 0; blockStart < symbols.size(); blockStart++) {
			for (int blockLength = minimumBestBlockLength; blockLength < symbols.size() - blockStart; blockLength++) {
				int numRepeats = 1;
				boolean repeatFailed = false;
				for (; !repeatFailed; ) {
					// Block is too long to have any more repeats
					if (blockStart + (numRepeats + 1) * blockLength > symbols.size())
						break;
					// Check each entry matches the potential block repeat
					for (int k = 0; k < blockLength; k++) {
						if (!Objects.equals(symbols.get(blockStart + k), symbols.get(blockStart + (numRepeats * blockLength) + k))) {
							repeatFailed = true;
							break;
						}
					}
					if (!repeatFailed) {
						numRepeats++;
					}
				}
				int reduction = blockLength * (numRepeats - 1);

				if (reduction > bestReduction) {
					bestBlockStart = blockStart;
					bestBlockLength = blockLength;
					bestNumRepeats = numRepeats;
					bestReduction = reduction;
				}
			}
		}

		// Render as string
		if (bestBlockLength * (bestNumRepeats - 1) >= 2) {
			// The reduction is substantial enough to be worthwhile
			String start = compressLongestBlock(symbols.subList(0, bestBlockStart));

			String repeatFormat = bestBlockLength>1 ? MULTI_CHAR_REPEAT_FORMAT : SINGLE_CHAR_REPEAT_FORMAT;
			String middle = String.format(repeatFormat, join(symbols.subList(bestBlockStart, bestBlockStart + bestBlockLength)), bestNumRepeats);

			String end = compressLongestBlock(symbols.subList(bestBlockStart + bestNumRepeats * bestBlockLength, symbols.size()));

			return joinRenderedStrings(Arrays.asList(start, middle, end));
		} else {
			// Just return the row as a string
			return join(symbols);
		}
	}

	private static String joinRenderedStrings(List<String> elements) {
		StringJoiner sj = new StringJoiner(DELIMITER);
		elements.stream().filter(StringUtils::isNotEmpty).forEach(sj::add);
		return sj.toString();
	}

	private static String join(List<String> elements) {
		StringBuilder sb = new StringBuilder();
		// To add an extra iteration to the loop
		Queue<String> remainingElements = new LinkedList<>(elements);
		remainingElements.add("");

		String lastString = null;
		int lastStringCount = 0;
		String currentString;
		while (!remainingElements.isEmpty()) {
			currentString = remainingElements.poll();
			if (Objects.equals(lastString, currentString)) {
				lastStringCount++;
			} else {
				// Build string
				if (lastStringCount > 1) {
					sb.append(String.format(SINGLE_CHAR_REPEAT_FORMAT, lastString, lastStringCount))
							.append(DELIMITER);
				} else if (lastStringCount == 1) {
					sb.append(lastString).append(DELIMITER);
				}

				// Reset counts
				lastString = currentString;
				lastStringCount = 1;
			}
		}

		String renderedWithTrailingComma = sb.toString();
		if (renderedWithTrailingComma.isEmpty()){
			return "";
		} else {
			// Strip the trailing comma
			return renderedWithTrailingComma.substring(0, renderedWithTrailingComma.length() - DELIMITER.length());
		}
	}

}
