package com.griffiths.hugh.ui;

import com.griffiths.hugh.declarative_knitting.core.model.patterns.PatternSegment;
import com.griffiths.hugh.declarative_knitting.core.model.rows.Rule;
import com.griffiths.hugh.declarative_knitting.core.rendering.XlsxRenderer;
import com.griffiths.hugh.declarative_knitting.core.rules.BindOff;
import com.griffiths.hugh.declarative_knitting.core.rules.StockingStitch;
import com.griffiths.hugh.declarative_knitting.images.EdgeDetector;
import com.griffiths.hugh.declarative_knitting.images.ImageEdgeTracer;
import com.griffiths.hugh.declarative_knitting.images.PixelBasedLaceImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import nu.pattern.OpenCV;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ImageEdgeLaceController {
	private static final double STITCH_ASPECT_RATIO = 1.5;
	private static final String IMAGE_FOLDER = "images/";

	static {
		OpenCV.loadLocally();
	}

	private Logger log = Logger.getLogger(this.getClass().getSimpleName());

	@PostConstruct
	public void setUp(){
		new File(IMAGE_FOLDER).mkdirs();
	}

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
		List<boolean[]> result = readImage(requestId, imageUrl, size);

		Rule imageRule = PixelBasedLaceImage.getInstance(result);

		PatternSegment patternSegment = PatternSegment.castOn(result.get(0).length*2);
		patternSegment.addRule(new StockingStitch(0)).addRule(imageRule)
				.knitRows(result.size()*2);
		patternSegment.clearRules().addRule(new BindOff())
				.knitRow();

		try (XlsxRenderer renderer = new XlsxRenderer(outputStream)){
			renderer.render(patternSegment);
		}
	}

	private List<boolean[]> readImage(String requestId, String url, int width) throws IOException {
		String tempFilename= IMAGE_FOLDER +requestId+".jpg";
		ImageIO.write(ImageIO.read(new URL(url)), "jpeg", new File(tempFilename));
		Mat img = Imgcodecs.imread(tempFilename);
		Mat imgEdges = EdgeDetector.segmentImage(img, width, STITCH_ASPECT_RATIO);
		Imgcodecs.imwrite(tempFilename.replaceAll("\\.(\\w+)$", "_edges.$1"), imgEdges);

		return new ImageEdgeTracer().traceEdgePoints(imgEdges);
	}
}
