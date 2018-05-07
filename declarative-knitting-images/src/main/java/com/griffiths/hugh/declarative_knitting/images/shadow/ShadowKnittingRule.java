package com.griffiths.hugh.declarative_knitting.images.shadow;

import com.griffiths.hugh.declarative_knitting.core.model.rows.Row;
import com.griffiths.hugh.declarative_knitting.core.model.rows.Rule;
import com.griffiths.hugh.declarative_knitting.core.model.stitches.Technique;
import java.util.List;

import static com.griffiths.hugh.declarative_knitting.core.model.stitches.TechniqueFactory.knit;
import static com.griffiths.hugh.declarative_knitting.core.model.stitches.TechniqueFactory.purl;

public class ShadowKnittingRule implements Rule {
	private final List<boolean[]> image;
	private int rowNum = 0;

	private ShadowKnittingRule(List<boolean[]> image) {
		this.image = image;
	}

	public static Rule getInstance(List<boolean[]> image) {
		return new ShadowKnittingRule(image);
	}

	@Override
	public void apply(Row row) {
		if (rowNum >= image.size() * 4)
			return;

		// Each "pixel" is 2 stitches wide and 4 deep.
		boolean[] imageRow = image.get(rowNum / 4);
		int pixelRow = rowNum % 4;

		for (int i = 0; i < Math.min(imageRow.length, row.getParentLoops().size()); i++) {
			// Whether we knit or purl depends on which row we're on, and whether this pixel is active
			final Technique technique = getTechnique(imageRow[i], pixelRow);
			row.replaceStitch(2 * i, technique);
			row.replaceStitch(2 * i + 1, technique);
		}

		rowNum++;
	}

	private Technique getTechnique(boolean pixelState, int pixelRow) {
		if (pixelState) {
			switch (pixelRow) {
				/*
				 * Ridge on the second stripe:
				 * kk
				 * kk
				 * kk
				 * pp
				 */
				case 0:
				case 1:
				case 2:
					return knit();
				case 3:
					return purl();
			}
		} else {
			switch (pixelRow) {
				/*
				 * Ridge on the first stripe:
				 * kk
				 * pp
				 * kk
				 * kk
				 */
				case 0:
				case 2:
				case 3:
					return knit();
				case 1:
					return purl();
			}
		}

		throw new IndexOutOfBoundsException("Shadow knitting pixel row out of bounds : "+pixelRow);
	}
}
