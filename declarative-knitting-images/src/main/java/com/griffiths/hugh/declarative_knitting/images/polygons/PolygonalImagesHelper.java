package com.griffiths.hugh.declarative_knitting.images.polygons;

import com.griffiths.hugh.declarative_knitting.core.model.patterns.Pattern;
import com.griffiths.hugh.declarative_knitting.core.model.patterns.PatternSegment;
import com.griffiths.hugh.declarative_knitting.core.rendering.XlsxRenderer;
import com.griffiths.hugh.declarative_knitting.images.shadow.FlattenedImage;
import java.awt.*;

import static com.griffiths.hugh.declarative_knitting.core.rules.RuleFactory.bindOff;
import static com.griffiths.hugh.declarative_knitting.core.rules.RuleFactory.stockingStitchITR;

public class PolygonalImagesHelper {

	public static final String SHEET_TITLE = "Polygonal Image";

	public FlattenedImage createPolygonalImagePattern(final String filename, final XlsxRenderer renderer, final int numSides, final int numRows, final int numColours) {
		// Load image
		final FlattenedImage flattenedImage = new FlattenedImage(filename, 100, 1, numColours);

		// Create pattern and output as spreadsheet
		final Pattern pattern = new Pattern();
		final PatternSegment patternSegment = pattern.createSegment(SHEET_TITLE, 2 * numSides);
		patternSegment.addRule(stockingStitchITR())
				.addRule(new PolygonalIncreases(numSides))
				.addColourRule(new PolygonalColours(flattenedImage.getPixels(), numSides, numRows))
				.knitRows(numRows);
		patternSegment.clearRules().addRule(bindOff()).knitRow();

		for (final Integer label : flattenedImage.getColours().keySet()) {
			final double[] colourComponents = flattenedImage.getColours().get(label);
			pattern.addColour(label, getColour(colourComponents));
		}

		renderer.render(pattern);

		return flattenedImage;
	}

	private Color getColour(final double[] colourComponents) {
		return new Color(toInt(colourComponents[2]), toInt(colourComponents[1]), toInt(colourComponents[0]));
	}

	private int toInt(final double colourComponent) {
		return (int) colourComponent;
	}
}