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

	public static FlattenedImage createShadowKnittingPattern(String filename, XlsxRenderer renderer, int widthStitches) throws IOException {
		// Flatten
		FlattenedImage flattenedImage = new FlattenedImage(filename, widthStitches/2, 0.67);

		// Create pattern
		Pattern pattern = new Pattern();
		Rule imageRule = ShadowKnittingRule.getInstance(flattenedImage.getPixels());
		int patternWidth = flattenedImage.getPixels().get(0).length * 2;
		PatternSegment patternSegment = pattern.createSegment(filename.replaceAll(".*[/\\\\]", ""), patternWidth);
		int numRows = flattenedImage.getPixels().size() * 4;
		// Set colours
		pattern.addColour(1, getColour(flattenedImage.getForeground()));
		pattern.addColour(2, getColour(flattenedImage.getBackground()));
		pattern.addColour(3, getFadedColour(flattenedImage.getForeground()));
		pattern.addColour(4, getFadedColour(flattenedImage.getBackground()));

		// Knit
		// NB - the primary colour is the second stripe, which will be raised on active pixels
		patternSegment.addRule(stockingStitch(0)).addRule(imageRule)
				.addColourRule(shadowKnittingStripes(1, 2, 3, 4))
				.knitRows(numRows);
		patternSegment.clearRules().addRule(bindOff()).knitRow();

		// Render
		renderer.render(pattern);

		// Return the image, so any further inspections can be carrier out
		return flattenedImage;
	}

	private static Color getColour(double[] bgr) {
		return new Color((int) bgr[2], (int) bgr[1], (int) bgr[0]);
	}

	private static Color getFadedColour(double[] bgr) {
		return new Color((int) (bgr[2]* FADE_PROPORTION), (int) (bgr[1]* FADE_PROPORTION), (int) (bgr[0]* FADE_PROPORTION));
	}
}
