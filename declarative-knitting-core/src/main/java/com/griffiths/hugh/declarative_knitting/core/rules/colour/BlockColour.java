package com.griffiths.hugh.declarative_knitting.core.rules.colour;

import com.griffiths.hugh.declarative_knitting.core.model.rows.ColourRule;
import com.griffiths.hugh.declarative_knitting.core.model.rows.Row;

public class BlockColour implements ColourRule {
	private final int colour;

	public BlockColour(int colour) {
		this.colour = colour;
	}

	@Override
	public void apply(Row row) {
		row.getStitches().stream().forEach(st -> st.setColour(colour));
	}
}
