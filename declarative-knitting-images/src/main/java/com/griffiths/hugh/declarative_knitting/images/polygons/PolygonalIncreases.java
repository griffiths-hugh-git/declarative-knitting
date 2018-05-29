package com.griffiths.hugh.declarative_knitting.images.polygons;

import com.griffiths.hugh.declarative_knitting.core.model.rows.Row;
import com.griffiths.hugh.declarative_knitting.core.model.rows.Rule;
import com.griffiths.hugh.declarative_knitting.core.model.stitches.TechniqueFactory;

public class PolygonalIncreases implements Rule {
	private final int numSides;
	private int rowNum = 0;

	public PolygonalIncreases(final int numSides) {
		this.numSides = numSides;
	}

	@Override
	public void apply(final Row row) {
		final boolean shouldIncrease =
				(Math.floor((2.0 / numSides) * (rowNum + 1)) !=
						Math.floor((2.0 / numSides) * rowNum));

		if (shouldIncrease) {
			final int panelLength = row.getParentLoops().size() / numSides;
			for (int i = 0; i < row.getParentLoops().size(); i++) {
				final int indexWithinPanel = i % panelLength;
				if (indexWithinPanel == 0 || indexWithinPanel == panelLength - 1) {
					row.replaceStitch(i, TechniqueFactory.kfb());
				}
			}
		}

		rowNum++;
	}
}
