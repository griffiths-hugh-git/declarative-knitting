package com.griffiths.hugh.declarative_knitting.images.shadow;

import java.util.ArrayList;
import java.util.LinkedHashMap;
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
 * Image processing code based on :
 * https://github.com/badlogic/opencv-fun/blob/master/src/pool/tests/Cluster.java
 */
public class FlattenedImage {

	public static final double DIFFERENCE_THRESHOLD = 0.001;

	static {
		OpenCV.loadLocally();
	}

	public final int numSegments;
	private final List<int[]> pixels;
	private final Map<Integer, double[]> colours = new LinkedHashMap<>();

	public FlattenedImage(final String filename, final int width, final double pixelAspectRatio, final int numColours) {
		this(Imgcodecs.imread(filename), width, pixelAspectRatio, numColours);
	}

	public FlattenedImage(final Mat image, final int width, final double pixelAspectRatio, final int numColours) {
		this.numSegments = numColours;
		this.pixels = flattenImage(image, width, pixelAspectRatio);
	}

	private boolean comparePixels(final double[] p1, final double[] p2) {
		if (p1.length != p2.length) {
			return false;
		}

		// Calculate the L2 distance between the pixels
		double norm = 0;
		for (int i = 0; i < p1.length; i++) {
			norm += (p1[i] - p2[i]) * (p1[i] - p2[i]);
		}

		return norm < DIFFERENCE_THRESHOLD;
	}

	private Mat resize(final Mat img, final int width, final double pixelAspectRatio) {
		final Mat resizeimage = new Mat();
		// Preserve aspect ratio
		final Size sz = new Size(width, (img.height() * width * pixelAspectRatio) / img.width());
		Imgproc.resize(img, resizeimage, sz);

		return resizeimage;
	}

	public int getNumSegments() {
		return numSegments;
	}

	public List<int[]> getPixels() {
		return pixels;
	}

	public Map<Integer, double[]> getColours() {
		return colours;
	}

	private List<int[]> flattenImage(Mat img, final int width, final double pixelAspectRatio) {
		img = resize(img, width, pixelAspectRatio);
		return cluster(img);
	}

	private List<int[]> cluster(final Mat img) {
		final Mat samples = img.reshape(1, img.cols() * img.rows());
		final Mat samples32f = new Mat();
		samples.convertTo(samples32f, CvType.CV_32F, 1.0 / 255.0);

		final Mat labels = new Mat();
		final TermCriteria criteria = new TermCriteria(TermCriteria.COUNT, 100, 1);
		final Mat centers = new Mat();
		Core.kmeans(samples32f, numSegments, labels, criteria, 3, Core.KMEANS_PP_CENTERS, centers);
		return showClusters(img, labels, centers);
	}

	private List<int[]> showClusters(final Mat cutout, final Mat labels, final Mat centers) {
		centers.convertTo(centers, CvType.CV_8UC1, 255.0);
		centers.reshape(3);

		for (int label = 0; label < centers.rows(); label++) {
			final int r = (int) centers.get(label, 2)[0];
			final int g = (int) centers.get(label, 1)[0];
			final int b = (int) centers.get(label, 0)[0];

			colours.put(label, new double[]{b, g, r});
		}

		int pixel = 0;
		final List<int[]> pixels = new ArrayList<>(cutout.height());
		for (int y = 0; y < cutout.rows(); y++) {
			final int[] row = new int[cutout.width()];
			pixels.add(row);

			for (int x = 0; x < cutout.cols(); x++) {
				row[x] = (int) labels.get(pixel, 0)[0];
				pixel++;

			}
		}

		return pixels;
	}
}
