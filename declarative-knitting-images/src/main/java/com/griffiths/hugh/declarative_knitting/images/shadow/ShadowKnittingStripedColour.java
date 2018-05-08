package com.griffiths.hugh.declarative_knitting.images.shadow;

import com.griffiths.hugh.declarative_knitting.core.model.rows.ColourRule;
import com.griffiths.hugh.declarative_knitting.core.model.rows.Row;

/**
 * Extension of the usual stripes rule - this puts a different highlight colour on "P" stitches, to allow the pattern
 * to be visible when viewed as a spreadsheet.
 */
public class ShadowKnittingStripedColour implements ColourRule {
	public static final String STITCH_TO_DEEMPHASISE = "P";
	private final int primaryColour;
	private final int secondaryColour;
	private final int primaryColourFaded;
	private final int secondaryColourFaded;

	private int rowNum = 0;

	public ShadowKnittingStripedColour(int primaryColour, int secondaryColour, int primaryColourFaded, int secondaryColourFaded) {
		this.primaryColour = primaryColour;
		this.secondaryColour = secondaryColour;
		this.primaryColourFaded = primaryColourFaded;
		this.secondaryColourFaded = secondaryColourFaded;
	}

	public static ColourRule shadowKnittingStripes(int primaryColour, int secondaryColour, int primaryColourFaded, int secondaryColourFaded) {
		return new ShadowKnittingStripedColour(primaryColour, secondaryColour, primaryColourFaded, secondaryColourFaded);
	}

	@Override
	public void apply(Row row) {
		final int repeatRowNum = rowNum++ % 4;
		final int mainColour = (repeatRowNum < 2) ? primaryColour : secondaryColour;
		final int fadedColour = (repeatRowNum < 2) ? primaryColourFaded : secondaryColourFaded;

		row.getStitches().stream().forEach(st -> {
			if (STITCH_TO_DEEMPHASISE.equals(st.getType())) {
				st.setColour(fadedColour);
			} else {
				st.setColour(mainColour);
			}
		});
	}
}
