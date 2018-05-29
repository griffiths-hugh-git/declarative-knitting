package com.griffiths.hugh.declarative_knitting.images.polygons;

import com.griffiths.hugh.declarative_knitting.core.rendering.XlsxRenderer;
import java.io.FileOutputStream;
import java.io.IOException;
import org.junit.Test;

public class PolygonalColoursTest {

	public static final int NUM_COLOURS = 4;
	private final PolygonalImagesHelper polygonalImagesHelper = new PolygonalImagesHelper();

	@Test
	public void testHexagonalPattern() throws Exception {
		final String filename = "src/test/resources/circles.png";

		createPolygonalImage(filename, 6, 20, "Hexagonal picture");
	}

	@Test
	public void testSquarePattern() throws Exception {
		final String filename = "src/test/resources/circles.png";

		createPolygonalImage(filename, 4, 20, "Hexagonal picture");
	}

	private void createPolygonalImage(final String filename, final int numSides, final int numRows, final String hexagonalPicture) throws IOException {
		// Create pattern and output as spreadsheet
		try (final FileOutputStream outputStream = new FileOutputStream(getOutputFilename(filename, numSides));
			 final XlsxRenderer xlsxRenderer = new XlsxRenderer(outputStream)) {
			polygonalImagesHelper.createPolygonalImagePattern(filename, xlsxRenderer, numSides, numRows, NUM_COLOURS);
		}
	}

	private String getOutputFilename(final String filename, final int numSides) {
		return filename.replaceAll(".*?(\\w+)\\.(\\w+)", "target/$1_polygon_" + numSides + ".xlsx");
	}
}