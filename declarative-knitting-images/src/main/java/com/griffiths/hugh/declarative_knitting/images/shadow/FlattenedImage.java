package com.griffiths.hugh.declarative_knitting.images.shadow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nu.pattern.OpenCV;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.core.TermCriteria;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 * Image processing code copied from:
 * https://github.com/badlogic/opencv-fun/blob/master/src/pool/tests/Cluster.java
 */
public class FlattenedImage {

	public static final int NUM_SEGMENTS = 2;
	public static final double DIFFERENCE_THRESHOLD = 0.001;

	static {
		OpenCV.loadLocally();
	}

	private Mat flattenedImage;
	private final List<boolean[]> pixels;
	private double[] background;
	private double[] foreground;

	public FlattenedImage(String filename, int width, double pixelAspectRatio) {
		this(Imgcodecs.imread(filename), width, pixelAspectRatio);
	}

	public FlattenedImage(Mat image, int width, double pixelAspectRatio){
		this.pixels = flattenImage(image, width, pixelAspectRatio);
	}

	public Mat getFlattenedImage() {
		return flattenedImage;
	}

	public List<boolean[]> getPixels() {
		return pixels;
	}

	public double[] getBackground() {
		return background;
	}

	public double[] getForeground() {
		return foreground;
	}

	private List<boolean[]> flattenImage(Mat img, int width, double pixelAspectRatio) {
		img = resize(img, width, pixelAspectRatio);

		// Keep a copy of the image for reference
		this.flattenedImage = cluster(img);

		this.background = flattenedImage.get(0, 0);
		List<boolean[]> imagePixels = new ArrayList<>(flattenedImage.height());
		for (int y = 0; y < flattenedImage.height(); y++) {
			boolean[] rowPixels = new boolean[flattenedImage.width()];
			imagePixels.add(rowPixels);

			for (int x = 0; x < flattenedImage.width(); x++) {
				boolean pixelsAreEqual = comparePixels(background, flattenedImage.get(y, x));
				rowPixels[x] = pixelsAreEqual;

				if (foreground == null && !pixelsAreEqual) {
					foreground = flattenedImage.get(y, x);
				}
			}
		}

		return imagePixels;
	}


	private static boolean comparePixels(double[] p1, double[] p2) {
		if (p1.length != p2.length)
			return false;

		// Calculate the L2 distance between the pixels
		double norm = 0;
		for (int i = 0; i < p1.length; i++) {
			norm += (p1[i] - p2[i]) * (p1[i] - p2[i]);
		}

		return norm < DIFFERENCE_THRESHOLD;
	}

	private Mat cluster(Mat img) {
		Mat samples = img.reshape(1, img.cols() * img.rows());
		Mat samples32f = new Mat();
		samples.convertTo(samples32f, CvType.CV_32F, 1.0 / 255.0);

		Mat labels = new Mat();
		TermCriteria criteria = new TermCriteria(TermCriteria.COUNT, 100, 1);
		Mat centers = new Mat();
		Core.kmeans(samples32f, NUM_SEGMENTS, labels, criteria, 3, Core.KMEANS_PP_CENTERS, centers);
		return showClusters(img, labels, centers);
	}

	private static Mat showClusters(Mat cutout, Mat labels, Mat centers) {
		centers.convertTo(centers, CvType.CV_8UC1, 255.0);
		centers.reshape(3);

		Mat flattened = Mat.zeros(cutout.size(), cutout.type());

		Map<Integer, Integer> counts = new HashMap<Integer, Integer>();
		for (int i = 0; i < centers.rows(); i++) counts.put(i, 0);

		int rows = 0;
		for (int y = 0; y < cutout.rows(); y++) {
			for (int x = 0; x < cutout.cols(); x++) {
				int label = (int) labels.get(rows, 0)[0];
				int r = (int) centers.get(label, 2)[0];
				int g = (int) centers.get(label, 1)[0];
				int b = (int) centers.get(label, 0)[0];
				counts.put(label, counts.get(label) + 1);
				flattened.put(y, x, b, g, r);
				rows++;
			}
		}

		return flattened;
	}

	private static Mat resize(Mat img, int width, final double pixelAspectRatio) {
		Mat resizeimage = new Mat();
		// Preserve aspect ratio
		Size sz = new Size(width, (img.height() * width * pixelAspectRatio) / img.width());
		Imgproc.resize(img, resizeimage, sz);

		return resizeimage;
	}
}
