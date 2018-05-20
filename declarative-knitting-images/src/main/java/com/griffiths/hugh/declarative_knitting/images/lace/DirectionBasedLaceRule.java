package com.griffiths.hugh.declarative_knitting.images.lace;

import com.griffiths.hugh.declarative_knitting.core.model.rows.Row;
import com.griffiths.hugh.declarative_knitting.core.model.rows.Rule;
import java.util.Iterator;
import java.util.List;

public class DirectionBasedLaceRule implements Rule {
	private final Iterator<int[]> directionsIterator;

	public DirectionBasedLaceRule(List<int[]> directions) {
		this.directionsIterator = directions.iterator();
	}

	@Override
	public void apply(Row row) {
		int[] directions = directionsIterator.next();
		// TODO: This makes it easier to not worry about edge conditions, but it needs thinking about properly.
		// Also, how to do triangular rows?
		if (row.getParentLoops().size()<2*directions.length)
			throw new IllegalArgumentException("BasicRow length too short");

		for (int i=0; i<directions.length-1; i++){
			int firstDirection=directions[i];
			int secondDirection=directions[i+1];


		}
	}
}
