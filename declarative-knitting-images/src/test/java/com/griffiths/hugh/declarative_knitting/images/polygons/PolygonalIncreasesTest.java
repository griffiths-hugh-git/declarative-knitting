package com.griffiths.hugh.declarative_knitting.images.polygons;

import com.griffiths.hugh.declarative_knitting.core.model.patterns.Pattern;
import com.griffiths.hugh.declarative_knitting.core.model.patterns.PatternSegment;
import com.griffiths.hugh.declarative_knitting.core.rendering.LatexRenderer;
import com.griffiths.hugh.declarative_knitting.core.rendering.Renderer;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import org.junit.Test;

import static com.griffiths.hugh.declarative_knitting.core.rules.RuleFactory.bindOff;
import static com.griffiths.hugh.declarative_knitting.core.rules.RuleFactory.stockingStitchITR;

public class PolygonalIncreasesTest {

	@Test
	public void testHexagonalPattern() throws Exception {
		final Pattern pattern = new Pattern();
		knitPolygon(pattern, "Triangle", 3);
		knitPolygon(pattern, "Square", 4);
		knitPolygon(pattern, "Hexagon", 6);
		knitPolygon(pattern, "Dodecagon", 12);

		final OutputStream outputStream = new FileOutputStream(new File("Target", "polygons.tex"));
		try (final Renderer rendererRenderer = new LatexRenderer(outputStream)) {
			rendererRenderer.render(pattern);
		}

		System.out.println(outputStream);
	}

	private Pattern knitPolygon(final Pattern pattern, final String title, final int numSides) {
		final PatternSegment patternSegment = pattern.createSegment(title, numSides * 2);

		patternSegment.clearRules().addRule(stockingStitchITR())
				.addRule(new PolygonalIncreases(numSides))
				.knitRows(10);
		patternSegment.clearRules().addRule(bindOff())
				.knitRow();

		return pattern;
	}
}