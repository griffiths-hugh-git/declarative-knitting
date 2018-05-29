package com.griffiths.hugh.declarative_knitting.core.model.patterns;

import java.awt.*;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;

public class Pattern {
	private final Map<String, PatternSegment> segments = new LinkedHashMap<>();
	private final Map<Integer, Color> colours = new HashMap<>();

	public Pattern addColour(final int index, final Color colour) {
		colours.put(index, colour);

		return this;
	}

	public int createColour(final Color colour) {
		final Optional<Integer> max = colours.keySet().stream().max(Comparator.naturalOrder());
		final int nextInt;
		if (max.isPresent()) {
			nextInt = max.get() + 1;
		} else {
			nextInt = 1;
		}

		colours.put(nextInt, colour);
		return nextInt;
	}

	public PatternSegment createSegment(final String title, final int castOnStitchs) {
		final PatternSegment newSegment = PatternSegment.castOn(castOnStitchs);
		segments.put(title, newSegment);

		return newSegment;
	}

	public Map<String, PatternSegment> getSegments() {
		return new LinkedHashMap<>(segments);
	}

	public SortedMap<Integer, Color> getColours() {
		return new TreeMap<>(colours);
	}
}