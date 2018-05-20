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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShadowKnittingController {
	public static final int PATTERN_MAX_WIDTH = 500;
	private final Logger log = Logger.getLogger(this.getClass().getSimpleName());

	@RequestMapping(value = "rest/shadow", method = RequestMethod.GET)
	public void generate(final HttpServletResponse response, @RequestParam("imageUrl") final String imageUrl,
						 @RequestParam("width") final int width) throws IOException {
		final String requestId = UUID.randomUUID().toString();
		log.info(String.format("Handling request %s from URL '%s'", requestId, imageUrl));

		if (width > PATTERN_MAX_WIDTH) {
			throw new IllegalArgumentException("Requested width is too large, unable to process");
		}

		response.addHeader("Content-Disposition", "attachment; filename=\"shadow_knitting.xlsx\"");
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

		transformImage(requestId, imageUrl, width, response.getOutputStream());

		// Flush
		response.flushBuffer();
	}

	private void transformImage(final String requestId, final String imageUrl, final int width, final OutputStream outputStream) throws IOException {
		final File imageFile = ImageUtil.downloadImageFile(requestId, imageUrl);
		try {
			final String filename = imageFile.getAbsolutePath();

			// Create pattern and output as spreadsheet
			try (final XlsxRenderer xlsxRenderer = new XlsxRenderer(outputStream)) {
				final FlattenedImage shadowKnittingPattern = ShadowKnittingHelper.createShadowKnittingPattern(filename, xlsxRenderer, width);
			}
		} finally {
			// Make sure the downloaded file is deleted
			FileUtils.deleteQuietly(imageFile);
		}
	}
}
