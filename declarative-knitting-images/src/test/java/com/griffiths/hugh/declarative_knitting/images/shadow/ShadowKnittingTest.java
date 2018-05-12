package com.griffiths.hugh.declarative_knitting.images.shadow;

import com.griffiths.hugh.declarative_knitting.core.rendering.XlsxRenderer;
import java.io.FileOutputStream;
import java.io.IOException;
import org.junit.Test;

public class ShadowKnittingTest {

	@Test
	public void shadowKnit() throws IOException {
		// Load image
		String filename = "src/test/resources/dachshund.jpg";

		// Create pattern and output as spreadsheet
		try (FileOutputStream outputStream = new FileOutputStream(getOutputFilename(filename));
			 XlsxRenderer xlsxRenderer = new XlsxRenderer(outputStream)){
			ShadowKnittingHelper.createShadowKnittingPattern(filename, xlsxRenderer, 100);
		}
	}

	private String getOutputFilename(String filename) {
		return filename.replaceAll(".*?(\\w+)\\.(\\w+)", "target/$1.xlsx");
	}
}