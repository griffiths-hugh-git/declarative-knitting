package com.griffiths.hugh.declarative_knitting.core.rendering;

import com.griffiths.hugh.declarative_knitting.core.model.patterns.Pattern;
import com.griffiths.hugh.declarative_knitting.core.model.patterns.PatternSegment;
import java.awt.*;
import java.io.FileOutputStream;
import org.junit.Test;

import static com.griffiths.hugh.declarative_knitting.core.rendering.RendererTestsUtil.knitShapes;

public class XlsxRendererTest {

	@Test
	public void testSleevePattern() throws Exception {
		XlsxRenderer xlsxRenderer = new XlsxRenderer(new FileOutputStream("target/XlsxRendererTest.xlsx"));

		PatternSegment patternSegment = RendererTestsUtil.knitSleeve();

		xlsxRenderer.render(patternSegment);
		xlsxRenderer.close();
	}

	@Test
	public void testSleevePattern_withColours() throws Exception {
		XlsxRenderer xlsxRenderer = new XlsxRenderer(new FileOutputStream("target/XlsxRendererTest_colours.xlsx"));
		xlsxRenderer.addColour(1, Color.BLUE);
		xlsxRenderer.addColour(2, Color.GREEN);

		PatternSegment patternSegment = RendererTestsUtil.knitSleeve();

		xlsxRenderer.render(patternSegment);
		xlsxRenderer.close();
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