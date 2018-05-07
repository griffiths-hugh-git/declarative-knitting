package com.griffiths.hugh.declarative_knitting.images;

import com.griffiths.hugh.declarative_knitting.images.lace.EdgeDetector;
import org.junit.Test;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class EdgeDetectorTest {

	@Test
	public void main() throws Exception {
		Mat dachshundEdges = EdgeDetector.segmentImage("src/test/resources/dachshund.jpg", 300, 1.5);
		Imgcodecs.imwrite("target/dachshund.jpg", dachshundEdges);
	}
}