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

public class CsvRendererTest {

	private CsvRenderer csvRenderer;
	private ByteArrayOutputStream baos;

	@Before
	public void setUp() throws Exception {
		baos = spy(new ByteArrayOutputStream());
		csvRenderer = new CsvRenderer(baos);
	}

	@Test
	public void render() throws Exception {

		final PatternSegment patternSegment = PatternSegment.castOn(6);

		patternSegment.addRule(ribStitch(0)).knitRows(2);
		patternSegment.clearRules().addRule(bindOff()).knitRow();

		csvRenderer.render(patternSegment);
		csvRenderer.close();
		final String output = new String(baos.toByteArray());

		final String[] lines = output.split(System.lineSeparator());
		assertArrayEquals(new String[]{"CO,CO,CO,CO,CO,CO", "K,P,K,P,K,P",
				"P,K,P,K,P,K", "BO,BO,BO,BO,BO,BO"}, lines);
		Mockito.verify(baos).close();
	}

	@Test
	public void renderPattern() throws Exception {
		final Pattern p = knitShapes();

		csvRenderer.render(p);
		csvRenderer.close();
		System.out.println(new String(baos.toByteArray()));
	}
}