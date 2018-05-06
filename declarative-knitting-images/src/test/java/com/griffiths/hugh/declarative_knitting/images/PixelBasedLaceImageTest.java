package com.griffiths.hugh.declarative_knitting.images;

import com.griffiths.hugh.declarative_knitting.core.model.patterns.PatternSegment;
import com.griffiths.hugh.declarative_knitting.core.model.rows.Rule;
import com.griffiths.hugh.declarative_knitting.core.rendering.LatexRenderer;
import com.griffiths.hugh.declarative_knitting.core.rules.BindOff;
import com.griffiths.hugh.declarative_knitting.core.rules.StockingStitch;
import java.io.FileOutputStream;
import java.util.List;
import org.junit.Test;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class PixelBasedLaceImageTest {
	private static final double STITCH_ASPECT_RATIO = 1.5;

	@Test
	public void knitCellularAutomatonTest() throws Exception {
		String filename = "dachshund.jpg";
		List<boolean[]> result = readImage(filename, 200);

		Rule imageRule = PixelBasedLaceImage.getInstance(result);

		PatternSegment patternSegment = PatternSegment.castOn(result.get(0).length*2);
		patternSegment.addRule(new StockingStitch(0)).addRule(imageRule)
				.knitRows(result.size()*2);
		patternSegment.clearRules().addRule(new BindOff())
				.knitRow();

//		try (XlsxRenderer renderer = new XlsxRenderer(new FileOutputStream(String.format("target/%s.xlsx", filename)))){
//			renderer.render(patternSegment);
//		}
		try (LatexRenderer renderer = new LatexRenderer(new FileOutputStream("target/temp.txt"))){
			renderer.render(patternSegment);
		}
	}

	private List<boolean[]> readImage(String filename, int width) {
		Mat imgEdges = EdgeDetector.segmentImage(String.format("src/main/resources/%s", filename), width, STITCH_ASPECT_RATIO);
		Imgcodecs.imwrite("target/"+filename.replaceAll("\\.(\\w+)", "_edges.$1"), imgEdges);
		return new ImageEdgeTracer().traceEdgePoints(imgEdges);
	}
}