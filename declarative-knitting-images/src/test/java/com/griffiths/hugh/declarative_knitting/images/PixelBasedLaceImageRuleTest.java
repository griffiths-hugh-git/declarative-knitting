package com.griffiths.hugh.declarative_knitting.images;

import com.griffiths.hugh.declarative_knitting.core.model.patterns.PatternSegment;
import com.griffiths.hugh.declarative_knitting.core.model.rows.Rule;
import com.griffiths.hugh.declarative_knitting.core.rendering.LatexRenderer;
import com.griffiths.hugh.declarative_knitting.core.rendering.XlsxRenderer;
import com.griffiths.hugh.declarative_knitting.images.lace.EdgeDetector;
import com.griffiths.hugh.declarative_knitting.images.lace.ImageEdgeTracer;
import com.griffiths.hugh.declarative_knitting.images.lace.PixelBasedLaceImageRule;
import java.io.FileOutputStream;
import java.util.List;
import org.junit.Test;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import static com.griffiths.hugh.declarative_knitting.core.rules.RuleFactory.bindOff;
import static com.griffiths.hugh.declarative_knitting.core.rules.RuleFactory.stockingStitch;

public class PixelBasedLaceImageRuleTest {
	private static final double STITCH_ASPECT_RATIO = 1.5;

	@Test
	public void knitCellularAutomatonTest() throws Exception {
		String filename = "src/test/resources/dachshund.jpg";
		List<boolean[]> result = readImage(filename, 200);

		Rule imageRule = PixelBasedLaceImageRule.getInstance(result);

		PatternSegment patternSegment = PatternSegment.castOn(result.get(0).length*2);
		patternSegment.addRule(stockingStitch(0)).addRule(imageRule)
				.knitRows(result.size()*2);
		patternSegment.clearRules().addRule(bindOff())
				.knitRow();

		try (XlsxRenderer renderer = new XlsxRenderer(new FileOutputStream(getOutputFilename(filename, "target/$1.xlsx")))){
			renderer.render(patternSegment);
		}
	}

	private List<boolean[]> readImage(String filename, int width) {
		Mat imgEdges = EdgeDetector.segmentImage(filename, width, STITCH_ASPECT_RATIO);
		Imgcodecs.imwrite(getOutputFilename(filename, "target/$1_edges.$2"), imgEdges);
		return new ImageEdgeTracer().traceEdgePoints(imgEdges);
	}

	private String getOutputFilename(String filename, String replacement) {
		return filename.replaceAll(".*?(\\w+)\\.(\\w+)", replacement);
	}
}