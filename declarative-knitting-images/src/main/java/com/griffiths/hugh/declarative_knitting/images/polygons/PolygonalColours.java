package com.griffiths.hugh.declarative_knitting.images.polygons;

import com.griffiths.hugh.declarative_knitting.core.model.rows.ColourRule;
import com.griffiths.hugh.declarative_knitting.core.model.rows.Row;
import com.griffiths.hugh.declarative_knitting.core.model.stitches.Stitch;
import java.util.List;

public class PolygonalColours implements ColourRule {
	private final List<int[]> image;
	private final PolygonalCoordinateMapper polygonalCoordinateMapper;
	private int rowNum = 1;

	public PolygonalColours(final List<int[]> image, final int polygonSides, final int numRows) {
		this.image = image;
		this.polygonalCoordinateMapper = new PolygonalCoordinateMapper(image.size(), image.get(0).length,
				polygonSides, numRows + 1);
	}

	@Override
	public void apply(final Row row) {
		final int rowLength = row.getStitches().size();
		for (int stNum = 0; stNum < rowLength; stNum++) {
			final Stitch st = row.getStitches().get(stNum);

			final int[] coordinates = polygonalCoordinateMapper.getCoordinates(rowNum, rowLength, stNum);
			st.setColour(getPixelColourFromImage(coordinates));
		}

		rowNum++;
	}

	private int getPixelColourFromImage(final int[] coordinates) {
		return image.get(coordinates[0])[coordinates[1]];
	}
}
