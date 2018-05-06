package com.griffiths.hugh.declarative_knitting.images;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import nu.pattern.OpenCV;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class EdgeDetector {
	static {
		OpenCV.loadLocally();
	}

	public static Mat segmentImage(final String filename, final int width, final double pixelAspectRatio) {
		Mat img = Imgcodecs.imread(filename);
		return segmentImage(img, width, pixelAspectRatio);
	}

	public static Mat segmentImage(Mat img, int width, double pixelAspectRatio) {
		img = resize(img, width, pixelAspectRatio);
		Mat transformed = doCanny(img);

		return transformed;
	}

	private static Mat resize(Mat img, int width, final double pixelAspectRatio) {
		Mat resizeimage = new Mat();
		// Preserve aspect ratio
		Size sz = new Size(width, (img.height() * width * pixelAspectRatio) / img.width());
		Imgproc.resize(img, resizeimage, sz);

		return resizeimage;
	}

	/**
	 * Apply Canny
	 *
	 * @param frame the current frame
	 * @return an image elaborated with Canny
	 */
	private static Mat doCanny(Mat frame) {
		// init
		Mat hsvImg = new Mat();
		List<Mat> hsvPlanes = new ArrayList<>();

		// threshold the image with the average hue value
		hsvImg.create(frame.size(), CvType.CV_8U);
		Imgproc.cvtColor(frame, hsvImg, Imgproc.COLOR_BGR2HSV);
		Core.split(hsvImg, hsvPlanes);

		// get the average hue value of the image
		double threshValue = getHistAverage(hsvImg, hsvPlanes.get(0));

		// init
		Mat grayImage = new Mat();
		Mat detectedEdges = new Mat();

		// convert to grayscale
		Imgproc.cvtColor(frame, grayImage, Imgproc.COLOR_BGR2GRAY);

		// reduce noise with a 3x3 kernel
		Imgproc.blur(grayImage, detectedEdges, new Size(3, 3));

		// canny detector, with ratio of lower:upper threshold of 3:1
		Imgproc.Canny(detectedEdges, detectedEdges, threshValue, threshValue * 3);

		// using Canny's output as a mask, display the result
		Mat dest = new Mat();
		frame.copyTo(dest, detectedEdges);

		return dest;
	}

	private static double getHistAverage(Mat hsvImg, Mat hueValues) {
		// init
		double average = 0.0;
		Mat hist_hue = new Mat();
		// 0-180: range of Hue values
		MatOfInt histSize = new MatOfInt(180);
		List<Mat> hue = new ArrayList<>();
		hue.add(hueValues);

		// compute the histogram
		Imgproc.calcHist(hue, new MatOfInt(0), new Mat(), hist_hue, histSize, new MatOfFloat(0, 179));

		// get the average Hue value of the image
		// (sum(bin(h)*h))/(image-height*image-width)
		// -----------------
		// equivalent to get the hue of each pixel in the image, add them, and
		// divide for the image size (height and width)
		for (int h = 0; h < 180; h++) {
			// for each bin, get its value and multiply it for the corresponding
			// hue
			average += (hist_hue.get(h, 0)[0] * h);
		}

		// return the average hue of the image
		return average = average / hsvImg.size().height / hsvImg.size().width;
	}

}
