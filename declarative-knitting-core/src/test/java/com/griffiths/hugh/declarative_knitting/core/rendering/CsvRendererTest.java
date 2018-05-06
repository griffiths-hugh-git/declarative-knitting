package com.griffiths.hugh.declarative_knitting.core.rendering;

import com.griffiths.hugh.declarative_knitting.core.model.patterns.Pattern;
import com.griffiths.hugh.declarative_knitting.core.model.patterns.PatternSegment;
import com.griffiths.hugh.declarative_knitting.core.rules.BindOff;
import com.griffiths.hugh.declarative_knitting.core.rules.RibStitch;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;

import static com.griffiths.hugh.declarative_knitting.core.rendering.CommonPatternsUtil.knitShapes;
import static org.junit.Assert.*;
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

		PatternSegment patternSegment = PatternSegment.castOn(6);

		patternSegment.addRule(new RibStitch(0))
				.knitRows(2);
		patternSegment.clearRules().addRule(new BindOff())
				.knitRow();

		csvRenderer.render(patternSegment);
		csvRenderer.close();
		String output = new String(baos.toByteArray());

		String[] lines = output.split(System.lineSeparator());
		assertArrayEquals(new String[] {"CO,CO,CO,CO,CO,CO", "K,P,K,P,K,P", "BO,BO,BO,BO,BO,BO"}, lines);
		Mockito.verify(baos).close();
	}

	@Test
	public void renderPattern() throws Exception {
		Pattern p = knitShapes();

		csvRenderer.render(p);
		csvRenderer.close();
		System.out.println(new String(baos.toByteArray()));
	}
}