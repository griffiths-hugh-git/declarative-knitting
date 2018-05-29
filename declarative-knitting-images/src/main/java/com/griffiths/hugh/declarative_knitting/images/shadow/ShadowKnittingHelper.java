package com.griffiths.hugh.declarative_knitting.images.shadow;

import com.griffiths.hugh.declarative_knitting.core.model.patterns.Pattern;
import com.griffiths.hugh.declarative_knitting.core.model.patterns.PatternSegment;
import com.griffiths.hugh.declarative_knitting.core.model.rows.Rule;
import com.griffiths.hugh.declarative_knitting.core.rendering.XlsxRenderer;
import java.awt.*;
import java.io.IOException;

import static com.griffiths.hugh.declarative_knitting.core.rules.RuleFactory.bindOff;
import static com.griffiths.hugh.declarative_knitting.core.rules.RuleFactory.stockingStitch;
import static com.griffiths.hugh.declarative_knitting.images.shadow.ShadowKnittingStripedColour.shadowKnittingStripes;

public class ShadowKnittingHelper {

	public static final double FADE_PROPORTION = 0.7;

	public static FlattenedImage createShadowKnittingPattern(final String filename, final XlsxRenderer renderer, final int widthStitches) throws IOException {
		// Flatten
		final FlattenedImage flattenedImage = new FlattenedImage(filename, widthStitches / 2, 0.67, 2);

		// Create pattern
		final Pattern pattern = new Pattern();
		final Rule imageRule = ShadowKnittingRule.getInstance(flattenedImage.getPixels());
		final int patternWidth = flattenedImage.getPixels().get(0).length * 2;
		final PatternSegment patternSegment = pattern.createSegment(filename.replaceAll(".*[/\\\\]", ""), patternWidth);
		final int numRows = flattenedImage.getPixels().size() * 4;
		// Set colours
		pattern.addColour(1, getColour(flattenedImage.getColours().get(0)));
		pattern.addColour(2, getColour(flattenedImage.getColours().get(1)));
		pattern.addColour(3, getFadedColour(flattenedImage.getColours().get(0)));
		pattern.addColour(4, getFadedColour(flattenedImage.getColours().get(1)));

		// Knit
		// NB - the primary colour is the second stripe, which will be raised on active pixels
		patternSegment.addRule(stockingStitch(0)).addRule(imageRule)
				.addColourRule(shadowKnittingStripes(1, 2, 3, 4))
				.knitRows(numRows);
		patternSegment.clearRules().addRule(bindOff()).knitRow();

		// Render
		renderer.render(pattern);

		// Return the image, so any further inspections can be carried out
		return flattenedImage;
	}

	private static Color getColour(final double[] bgr) {
		return new Color((int) bgr[2], (int) bgr[1], (int) bgr[0]);
	}

	private static Color getFadedColour(final double[] bgr) {
		return new Color((int) (bgr[2] * FADE_PROPORTION), (int) (bgr[1] * FADE_PROPORTION), (int) (bgr[0] * FADE_PROPORTION));
	}
}
