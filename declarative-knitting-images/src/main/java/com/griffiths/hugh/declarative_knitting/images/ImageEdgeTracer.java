package com.griffiths.hugh.declarative_knitting.images;

import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class ImageEdgeTracer {

	public List<int[]> traceEdgeDirections(Mat imgEdges){
		List<boolean[]> resultList = traceEdgePoints(imgEdges);

		List<int[]> directionsList = new DirectionAssigner().assignDirections(resultList);


		return directionsList;
	}

	public List<boolean[]> traceEdgePoints(Mat imgEdges) {
		List<boolean[]> resultList = new ArrayList<>();
		for (int i = 0; i < imgEdges.height(); i++) {
			boolean[] pixels = new boolean[imgEdges.width()];
			for (int j = 0; j < imgEdges.width(); j++) {
				pixels[j] = normExceedsThreshold(imgEdges.get(i, j));
			}
			resultList.add(pixels);
		}
		return resultList;
	}

	private static boolean normExceedsThreshold(double[] pixel) {
		return pixel[0] > 0.001;
	}
}
