package com.griffiths.hugh.declarative_knitting.images.polygons;

public class PolygonalCoordinateMapper {
	private final int[] centre;
	private final int polygonSides;
	private final double stitchHeightPx;

	public PolygonalCoordinateMapper(final int width, final int height, final int polygonSides, final double numRows) {
		this.centre = new int[]{width / 2, height / 2};
		this.polygonSides = polygonSides;
		this.stitchHeightPx = Math.min(height, width) / (2.0 * numRows);
	}

	public int[] getCoordinates(final int rowNum, final int rowLength, final int stitchNum) {
		final double[] cartesianCoords = convertPolygonalToCartesian(rowNum, rowLength, stitchNum);

		final int[] coords = new int[cartesianCoords.length];
		for (int i = 0; i < cartesianCoords.length; i++) {
			coords[i] = (int) (centre[i] + stitchHeightPx * cartesianCoords[i]);
		}
		return coords;
	}

	private double[] convertPolygonalToCartesian(final int rowNum, final int rowLength, final int stitchNum) {
		final int sideLength = rowLength / polygonSides;
		final int sideNum = stitchNum / sideLength;

		final double[] sideStartCoords = getPolarCoordinate(rowNum, sideNum);
		final double[] sideEndCoords = getPolarCoordinate(rowNum, sideNum + 1);

		final int stitchSideNum = stitchNum % sideLength;
		final double[] stitchCoords = interpolatePoints(sideStartCoords, sideEndCoords, stitchSideNum, sideLength);

		return stitchCoords;
	}

	private double[] getPolarCoordinate(final int rowNum, final int sideNum) {
		return new double[]{rowNum * Math.cos(getAngularCoordinate(sideNum)), rowNum * Math.sin(getAngularCoordinate(sideNum))};
	}

	private double getAngularCoordinate(final int sideNum) {
		return 2 * Math.PI / polygonSides * sideNum;
	}

	private double[] interpolatePoints(final double[] start, final double[] end, final int step, final int length) {
		if (start.length != end.length) {
			throw new IllegalArgumentException("Coordinate array lengths do not match");
		}

		final double[] interpolatedPoint = new double[start.length];
		for (int i = 0; i < start.length; i++) {
			interpolatedPoint[i] = interpolateCoordinate(start[i], end[i], step, length);
		}

		return interpolatedPoint;
	}

	private double interpolateCoordinate(final double start, final double end, final int step, final int length) {
		return (end * step / length) + (start * (length - step) / length);
	}
}
