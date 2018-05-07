package com.griffiths.hugh.declarative_knitting.images;

import com.griffiths.hugh.declarative_knitting.images.lace.DirectionAssigner;
import com.griffiths.hugh.declarative_knitting.images.lace.EdgeDetector;
import com.griffiths.hugh.declarative_knitting.images.lace.ImageEdgeTracer;
import java.util.List;
import org.junit.Test;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class ImageEdgeTracerTest {
	@Test
	public void main() {
		Mat imgEdges = EdgeDetector.segmentImage("src/test/resources/bike-silhouette.jpg", 300, 1.0);
		Imgcodecs.imwrite("target/bike-silhouette.jpg", imgEdges);

		List<int[]> directions = new ImageEdgeTracer().traceEdgeDirections(imgEdges);

		directions.stream().forEach(DirectionAssigner::printDirectionsArray);
//		DirectionAssigner.printDirectionsArray(resultList.get(i), directionsList.get(i));
	}
}