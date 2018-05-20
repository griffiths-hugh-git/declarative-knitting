package com.griffiths.hugh.declarative_knitting.core.rendering;

import com.griffiths.hugh.declarative_knitting.core.model.patterns.Pattern;
import com.griffiths.hugh.declarative_knitting.core.model.patterns.PatternSegment;
import java.io.ByteArrayOutputStream;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static com.griffiths.hugh.declarative_knitting.core.rendering.RendererTestsUtil.knitShapes;
import static com.griffiths.hugh.declarative_knitting.core.rules.RuleFactory.bindOff;
import static com.griffiths.hugh.declarative_knitting.core.rules.RuleFactory.ribStitch;
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
	public void testSleevePattern() throws Exception {
		final PatternSegment patternSegment = RendererTestsUtil.knitSleeve();

		textRenderer.render(patternSegment);
		textRenderer.close();
		baos.writeTo(System.out);
	}

	@Test
	public void render() throws Exception {

		final PatternSegment patternSegment = PatternSegment.castOn(6);

		patternSegment.addRule(ribStitch(0))
				.knitRows(2);
		patternSegment.clearRules().addRule(bindOff())
				.knitRow();

		textRenderer.render(patternSegment);
		textRenderer.close();
		final String output = new String(baos.toByteArray());

		final String[] lines = output.split(System.lineSeparator());
		assertArrayEquals(new String[]{"CO 6", "* K, P * 3 times",
				"* P, K * 3 times", "BO 6"}, lines);
		Mockito.verify(baos).close();
	}

	@Test
	public void renderPattern() throws Exception {
		final Pattern p = knitShapes();

		textRenderer.render(p);
		textRenderer.close();
		System.out.println(new String(baos.toByteArray()));
	}
}