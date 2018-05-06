package com.griffiths.hugh.declarative_knitting.core.rendering;

import com.griffiths.hugh.declarative_knitting.core.model.patterns.Pattern;
import com.griffiths.hugh.declarative_knitting.core.model.patterns.PatternSegment;
import com.griffiths.hugh.declarative_knitting.core.rules.BindOff;
import com.griffiths.hugh.declarative_knitting.core.rules.RibStitch;
import com.griffiths.hugh.declarative_knitting.core.rules.StockingStitch;
import com.griffiths.hugh.declarative_knitting.core.rules.colour.StripedColour;
import com.griffiths.hugh.declarative_knitting.core.rules.modifiers.IncreaseBothEnds;
import java.awt.*;
import java.io.FileOutputStream;
import org.junit.Test;

import static com.griffiths.hugh.declarative_knitting.core.rendering.CommonPatternsUtil.knitShapes;

public class XlsxRendererTest {

	@Test
	public void testSleevePattern() throws Exception {
		XlsxRenderer xlsxRenderer = new XlsxRenderer(new FileOutputStream("target/XlsxRendererTest.xlsx"));

		PatternSegment patternSegment = PatternSegment.castOn(16);

		knitSleeve(patternSegment);

		xlsxRenderer.render(patternSegment);
		xlsxRenderer.close();
	}

	@Test
	public void testSleevePattern_withColours() throws Exception {
		XlsxRenderer xlsxRenderer = new XlsxRenderer(new FileOutputStream("target/XlsxRendererTest_colours.xlsx"));
		xlsxRenderer.addColour(0, Color.BLUE);
		xlsxRenderer.addColour(1, Color.GREEN);

		PatternSegment patternSegment = PatternSegment.castOn(16);

		knitSleeve(patternSegment);

		xlsxRenderer.render(patternSegment);
		xlsxRenderer.close();
	}

	private void knitSleeve(PatternSegment patternSegment) {
		StripedColour stripedColour = new StripedColour(1, 1, 0, 1);
		patternSegment.addRule(new RibStitch(0)).addColourRule(stripedColour)
				.knitRows(6);
		patternSegment.clearRules().addRule(new StockingStitch(0))
				.addRule(new IncreaseBothEnds(0)).addColourRule(stripedColour)
				.knitRows(10);
		patternSegment.clearRules().addRule(new BindOff()).addColourRule(stripedColour)
				.knitRow();
	}


	@Test
	public void renderPattern() throws Exception {
		Pattern p = knitShapes();
		p.addColour(1, Color.GREEN).addColour(2, Color.CYAN).addColour(3, Color.PINK);

		XlsxRenderer xlsxRenderer = new XlsxRenderer(new FileOutputStream("target/XlsxRendererTest_pattern.xlsx"));
		xlsxRenderer.render(p);
		xlsxRenderer.close();
	}
}