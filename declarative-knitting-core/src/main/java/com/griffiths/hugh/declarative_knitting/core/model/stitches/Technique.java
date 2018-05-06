package com.griffiths.hugh.declarative_knitting.core.model.stitches;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class Technique implements Function<Iterator<Loop>, Stitch> {
	private final String symbol;
	private int colour;

	public Technique(String symbol) {
		this.symbol = symbol;
	}

	public int getColour() {
		return colour;
	}

	public void setColour(int colour) {
		this.colour = colour;
	}

	@Override
	public String toString() {
		return symbol;
	}

	static Technique createTechnique(int numParents, String symbol, int numChildren) {
		// TODO: Can we express that as a lambda?
		return new Technique(symbol) {
			@Override
			public Stitch apply(Iterator<Loop> loops) {
				return new Stitch(consumeLoops(loops, numParents), symbol, createLoops(numChildren, getColour()));
			}
		};
	}

	static List<Loop> consumeLoops(Iterator<Loop> loops, int num) {
		return IntStream.range(0, num).mapToObj(i -> loops.next()).collect(Collectors.toList());
	}

	static List<Loop> createLoops(int num, int colour) {
		return IntStream.range(0, num).mapToObj(i -> new Loop(colour)).collect(Collectors.toList());
	}
}
