package com.griffiths.hugh.ui;

import com.griffiths.hugh.declarative_knitting.core.model.patterns.PatternSegment;
import com.griffiths.hugh.declarative_knitting.core.model.rows.Rule;
import com.griffiths.hugh.declarative_knitting.core.rendering.XlsxRenderer;
import com.griffiths.hugh.declarative_knitting.core.rules.BindOff;
import com.griffiths.hugh.declarative_knitting.core.rules.StockingStitch;
import com.griffiths.hugh.declarative_knitting.images.lace.EdgeDetector;
import com.griffiths.hugh.declarative_knitting.images.lace.ImageEdgeTracer;
import com.griffiths.hugh.declarative_knitting.images.lace.PixelBasedLaceImageRule;
import com.griffiths.hugh.ui.util.ImageUtil;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//@RestController
public class ImageEdgeLaceController {
	private static final double STITCH_ASPECT_RATIO = 1.5;

	private Logger log = Logger.getLogger(this.getClass().getSimpleName());

	@RequestMapping(value = "edge", method = RequestMethod.GET)
	public void generateCellularAutomaton(final HttpServletResponse response, @RequestParam("imageUrl") final String imageUrl,
										  @RequestParam("width") final int width) throws IOException {
		String requestId = UUID.randomUUID().toString();
		log.info(String.format("Handling request %s from URL '%s'", requestId, imageUrl));

		response.addHeader("Content-Disposition",  "attachment; filename=\"edge_lace.xlsx\"");
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

		transformImage(requestId, imageUrl, width, response.getOutputStream());

		// Flush
		response.flushBuffer();
	}

	private void transformImage(String requestId, String imageUrl, int size, OutputStream outputStream) throws IOException {
		List<boolean[]> result = detectEdges(requestId, imageUrl, size);

		Rule imageRule = PixelBasedLaceImageRule.getInstance(result);

		PatternSegment patternSegment = PatternSegment.castOn(result.get(0).length*2);
		patternSegment.addRule(new StockingStitch(0)).addRule(imageRule)
				.knitRows(result.size()*2);
		patternSegment.clearRules().addRule(new BindOff())
				.knitRow();

		try (XlsxRenderer renderer = new XlsxRenderer(outputStream)){
			renderer.render(patternSegment);
		}
	}

	private List<boolean[]> detectEdges(String requestId, String url, int width) throws IOException {
		String tempFilename = ImageUtil.downloadImageFile(requestId, url);
		Mat img = Imgcodecs.imread(tempFilename);
		Mat imgEdges = EdgeDetector.segmentImage(img, width, STITCH_ASPECT_RATIO);

		// Save a copy of the edge detected image for reference
		Imgcodecs.imwrite(tempFilename.replaceAll("\\.(\\w+)$", "_edges.$1"), imgEdges);

		return new ImageEdgeTracer().traceEdgePoints(imgEdges);
	}
}
