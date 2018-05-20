package com.griffiths.hugh.ui;

import com.griffiths.hugh.declarative_knitting.core.rendering.XlsxRenderer;
import com.griffiths.hugh.declarative_knitting.images.shadow.FlattenedImage;
import com.griffiths.hugh.declarative_knitting.images.shadow.ShadowKnittingHelper;
import com.griffiths.hugh.ui.util.ImageUtil;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.FileUtils;
import org.opencv.imgcodecs.Imgcodecs;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShadowKnittingController {
	public static final int PATTERN_MAX_WIDTH = 500;
	private Logger log = Logger.getLogger(this.getClass().getSimpleName());

	@RequestMapping(value = "rest/shadow", method = RequestMethod.GET)
	public void generateCellularAutomaton(final HttpServletResponse response, @RequestParam("imageUrl") final String imageUrl,
										  @RequestParam("width") final int width) throws IOException {
		String requestId = UUID.randomUUID().toString();
		log.info(String.format("Handling request %s from URL '%s'", requestId, imageUrl));

		if (width> PATTERN_MAX_WIDTH){
			throw new IllegalArgumentException("Requested width is too large, unable to process");
		}

		response.addHeader("Content-Disposition", "attachment; filename=\"shadow_knitting.xlsx\"");
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

		transformImage(requestId, imageUrl, width, response.getOutputStream());

		// Flush
		response.flushBuffer();
	}

	private void transformImage(String requestId, String imageUrl, int width, OutputStream outputStream) throws IOException {
		File imageFile = ImageUtil.downloadImageFile(requestId, imageUrl);
		try {
			String filename = imageFile.getAbsolutePath();

			// Create pattern and output as spreadsheet
			try (XlsxRenderer xlsxRenderer = new XlsxRenderer(outputStream)) {
				FlattenedImage shadowKnittingPattern = ShadowKnittingHelper.createShadowKnittingPattern(filename, xlsxRenderer, width);

				// Save a copy of the flattened image for reference
				Imgcodecs.imwrite(filename.replaceAll("\\.(\\w+)$", "_flattened.$1"), shadowKnittingPattern.getFlattenedImage());
			}
		} finally {
			// Make sure the downloaded file is deleted
			FileUtils.deleteQuietly(imageFile);
		}
	}
}
