package com.griffiths.hugh.ui;

import com.griffiths.hugh.declarative_knitting.core.rendering.XlsxRenderer;
import com.griffiths.hugh.declarative_knitting.images.polygons.PolygonalImagesHelper;
import com.griffiths.hugh.declarative_knitting.images.shadow.FlattenedImage;
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
public class PolygonalKnittingController {
	private static final int PATTERN_MAX_ROWS = 300;
	private static final int PATTERN_MAX_SIDES = 12;
	private static final int PATTERN_MIN_SIDES = 3;
	private static final int PATTERN_MAX_COLOURS = 10;
	private final Logger log = Logger.getLogger(this.getClass().getSimpleName());
	private final PolygonalImagesHelper polygonalImagesHelper = new PolygonalImagesHelper();

	@RequestMapping(value = "rest/polygonal", method = RequestMethod.GET)
	public void generate(final HttpServletResponse response, @RequestParam("imageUrl") final String imageUrl,
						 @RequestParam("numRows") final int numRows,
						 @RequestParam("numSides") final int numSides,
						 @RequestParam("numColours") final int numColours
	) throws IOException {
		final String requestId = UUID.randomUUID().toString();
		log.info(String.format("Handling request %s from URL '%s'", requestId, imageUrl));

		// Bounds checks
		if (numRows > PATTERN_MAX_ROWS) {
			throw new IllegalArgumentException("Requested width is too large, unable to process");
		} else if (numSides < PATTERN_MIN_SIDES) {
			throw new IllegalArgumentException("Requested too few sides, unable to process");
		} else if (numSides > PATTERN_MAX_SIDES) {
			throw new IllegalArgumentException("Requested too many sides, unable to process");
		} else if (numColours > PATTERN_MAX_COLOURS) {
			throw new IllegalArgumentException("Requested too many colours, unable to process");
		}

		response.addHeader("Content-Disposition", "attachment; filename=\"polygonal_knitting.xlsx\"");
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

		transformImage(requestId, imageUrl, numRows, numSides, numColours, response.getOutputStream());

		// Flush
		response.flushBuffer();
	}

	private void transformImage(final String requestId, final String imageUrl, final int numRows, final int numSides, final int numColours,
								final OutputStream outputStream) throws IOException {
		final File imageFile = ImageUtil.downloadImageFile(requestId, imageUrl);
		try {
			final String filename = imageFile.getAbsolutePath();

			// Create pattern and output as spreadsheet
			try (final XlsxRenderer xlsxRenderer = new XlsxRenderer(outputStream)) {
				final FlattenedImage polygonalImagePattern = polygonalImagesHelper
						.createPolygonalImagePattern(filename, xlsxRenderer, numSides, numRows, numColours);

			}
		} finally {
			// Make sure the downloaded file is deleted
			FileUtils.deleteQuietly(imageFile);
		}
	}
}
