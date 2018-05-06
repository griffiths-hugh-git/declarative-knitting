package com.griffiths.hugh.declarative_knitting.core.rendering;

import com.griffiths.hugh.declarative_knitting.core.model.patterns.Pattern;
import com.griffiths.hugh.declarative_knitting.core.model.patterns.PatternSegment;
import com.griffiths.hugh.declarative_knitting.core.rules.BindOff;
import com.griffiths.hugh.declarative_knitting.core.rules.RibStitch;
import com.griffiths.hugh.declarative_knitting.core.rules.StockingStitch;
import com.griffiths.hugh.declarative_knitting.core.rules.modifiers.IncreaseBothEnds;
import java.io.ByteArrayOutputStream;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static com.griffiths.hugh.declarative_knitting.core.rendering.CommonPatternsUtil.knitShapes;
import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Mockito.spy;

public class TextRendererTest {

	private TextRenderer textRenderer;
	private ByteArrayOutputStream baos;

	@Before
	public void setUp() throws Exception {
		baos = spy(new ByteArrayOutputStream());
		textRenderer = new TextRenderer(baos);
	}


	@Test
	public void testSleevePattern() throws Exception{
		PatternSegment patternSegment = PatternSegment.castOn(16);

		patternSegment.addRule(new RibStitch(0))
				.knitRows(6);
		patternSegment.clearRules().addRule(new StockingStitch(0))
				.addRule(new IncreaseBothEnds(0))
				.knitRows(10);
		patternSegment.clearRules().addRule(new BindOff())
				.knitRow();

		textRenderer.render(patternSegment);
		textRenderer.close();
		baos.writeTo(System.out);
	}

	@Test
	public void render() throws Exception {

		PatternSegment patternSegment = PatternSegment.castOn(6);

		patternSegment.addRule(new RibStitch(0))
				.knitRows(2);
		patternSegment.clearRules().addRule(new BindOff())
				.knitRow();

		textRenderer.render(patternSegment);
		textRenderer.close();
		String output = new String(baos.toByteArray());

		String[] lines = output.split(System.lineSeparator());
		assertArrayEquals(new String[] {"CO 6", "* K, P * 3 times", "BO 6"}, lines);
		Mockito.verify(baos).close();
	}

	@Test
	public void renderPattern() throws Exception {
		Pattern p = knitShapes();

		textRenderer.render(p);
		textRenderer.close();
		System.out.println(new String(baos.toByteArray()));
	}
}