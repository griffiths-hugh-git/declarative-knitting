package com.griffiths.hugh.declarative_knitting.core.rules.colour;

import com.griffiths.hugh.declarative_knitting.core.model.rows.ColourRule;
import com.griffiths.hugh.declarative_knitting.core.model.rows.Row;

public class StripedColour implements ColourRule {
	private final int primaryStripeWidth;
	private final int secondaryStripeWidth;
	private final int primaryColour;
	private final int secondaryColour;

	private int rowNum=0;

	public StripedColour(int primaryStripeWidth, int secondaryStripeWidth, int primaryColour, int secondaryColour) {
		this.primaryStripeWidth = primaryStripeWidth;
		this.secondaryStripeWidth = secondaryStripeWidth;
		this.primaryColour = primaryColour;
		this.secondaryColour = secondaryColour;
	}

	@Override
	public void apply(Row row) {
		final int repeatRowNum = rowNum++ %(primaryStripeWidth + secondaryStripeWidth);
		final int colour = (repeatRowNum < primaryStripeWidth) ? primaryColour : secondaryColour;

		row.getStitches().stream().forEach(st -> st.setColour(colour));
	}
}
