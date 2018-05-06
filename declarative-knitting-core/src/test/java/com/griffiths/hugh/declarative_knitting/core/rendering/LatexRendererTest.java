package com.griffiths.hugh.declarative_knitting.core.rendering;

import com.griffiths.hugh.declarative_knitting.core.model.patterns.Pattern;
import com.griffiths.hugh.declarative_knitting.core.model.patterns.PatternSegment;
import java.io.ByteArrayOutputStream;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static com.griffiths.hugh.declarative_knitting.core.rendering.RendererTestsUtil.knitShapes;
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
		PatternSegment patternSegment = RendererTestsUtil.knitSleeve();

		latexRenderer.render(patternSegment);
		latexRenderer.close();
		Mockito.verify(baos).close();

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