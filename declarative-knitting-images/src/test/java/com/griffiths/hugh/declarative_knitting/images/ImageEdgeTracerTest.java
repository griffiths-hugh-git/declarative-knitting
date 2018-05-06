package com.griffiths.hugh.declarative_knitting.images;

import java.util.List;
import org.junit.Test;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class ImageEdgeTracerTest {
	@Test
	public void main() {
		Mat imgEdges = EdgeDetector.segmentImage("src/main/resources/bike-silhouette.jpg", 300, 1.0);
		Imgcodecs.imwrite("target/bike-silhouette.jpg", imgEdges);

		List<int[]> directions = new ImageEdgeTracer().traceEdgeDirections(imgEdges);

		directions.stream().forEach(DirectionAssigner::printDirectionsArray);
//		DirectionAssigner.printDirectionsArray(resultList.get(i), directionsList.get(i));
	}
}