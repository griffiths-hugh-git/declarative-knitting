package com.griffiths.hugh.ui;

import com.griffiths.hugh.declarative_knitting.core.rendering.XlsxRenderer;
import com.griffiths.hugh.declarative_knitting.images.shadow.FlattenedImage;
import com.griffiths.hugh.declarative_knitting.images.shadow.ShadowKnittingHelper;
import com.griffiths.hugh.ui.util.ImageUtil;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import org.opencv.imgcodecs.Imgcodecs;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShadowKnittingController {
	private Logger log = Logger.getLogger(this.getClass().getSimpleName());

	/*
	 * Example: 
	 * http://localhost:8080/shadow?width=100&imageUrl=https%3A%2F%2Fi.pinimg.com%2Foriginals%2F5c%2F54%2F09%2F5c54099509dd08dcb7db52b94a4ff467.png
	 */

	@RequestMapping(value = "shadow", method = RequestMethod.GET)
	public void generateCellularAutomaton(final HttpServletResponse response, @RequestParam("imageUrl") final String imageUrl,
										  @RequestParam("width") final int width) throws IOException {
		String requestId = UUID.randomUUID().toString();
		log.info(String.format("Handling request %s from URL '%s'", requestId, imageUrl));

		response.addHeader("Content-Disposition", "attachment; filename=\"shadow_knitting.xlsx\"");
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

		transformImage(requestId, imageUrl, width, response.getOutputStream());

		// Flush
		response.flushBuffer();
	}

	private void transformImage(String requestId, String imageUrl, int width, OutputStream outputStream) throws IOException {
		String tempFilename = ImageUtil.downloadImageFile(requestId, imageUrl);

		// Create pattern and output as spreadsheet
		try (XlsxRenderer xlsxRenderer = new XlsxRenderer(outputStream)) {
			FlattenedImage shadowKnittingPattern = ShadowKnittingHelper.createShadowKnittingPattern(tempFilename, xlsxRenderer);

			// Save a copy of the flattened image for reference
			Imgcodecs.imwrite(tempFilename.replaceAll("\\.(\\w+)$", "_flattened.$1"), shadowKnittingPattern.getFlattenedImage());
		}
	}
}
