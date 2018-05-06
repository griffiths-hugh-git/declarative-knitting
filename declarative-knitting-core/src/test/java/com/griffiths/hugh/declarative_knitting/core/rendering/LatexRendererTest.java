package com.griffiths.hugh.declarative_knitting.core.rendering;

import com.griffiths.hugh.declarative_knitting.core.model.patterns.Pattern;
import com.griffiths.hugh.declarative_knitting.core.model.patterns.PatternSegment;
import com.griffiths.hugh.declarative_knitting.core.rules.BindOff;
import com.griffiths.hugh.declarative_knitting.core.rules.RibStitch;
import com.griffiths.hugh.declarative_knitting.core.rules.StockingStitch;
import com.griffiths.hugh.declarative_knitting.core.rules.modifiers.IncreaseBothEnds;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;

import static com.griffiths.hugh.declarative_knitting.core.rendering.CommonPatternsUtil.knitShapes;
import static org.mockito.Mockito.spy;

public class LatexRendererTest {
	private LatexRenderer latexRenderer;
	private ByteArrayOutputStream baos;

	@Before
	public void setUp() throws Exception {
		baos = spy(new ByteArrayOutputStream());
		latexRenderer = new LatexRenderer(baos);
	}

	@Test
	public void testSleevePattern() throws Exception {
		PatternSegment patternSegment = PatternSegment.castOn(16);

		patternSegment.addRule(new RibStitch(0))
				.knitRows(6);
		patternSegment.clearRules().addRule(new StockingStitch(0))
				.addRule(new IncreaseBothEnds(0))
				.knitRows(10);
		patternSegment.clearRules().addRule(new BindOff())
				.knitRow();

		latexRenderer.render(patternSegment);
		latexRenderer.close();
		baos.writeTo(System.out);
	}

	@Test
	public void renderPattern() throws Exception {
		Pattern p = knitShapes();

		latexRenderer.render(p);
		latexRenderer.close();
		System.out.println(new String(baos.toByteArray()));
	}
}