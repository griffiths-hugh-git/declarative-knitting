package com.griffiths.hugh.ui.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import org.apache.commons.io.IOUtils;

public class ImageUtil {
	private static final String IMAGE_FOLDER = "images/";

	static {
		new File(IMAGE_FOLDER).mkdirs();
	}

	public static String downloadImageFile(String requestId, String url) throws IOException {
		final String tempFilename;
		if (!url.matches("^.*\\.\\w+$")){
//			tempFilename = IMAGE_FOLDER + requestId+".jpg";
			throw new IllegalArgumentException("Not sure how to handle this yet");
		} else {
			tempFilename = IMAGE_FOLDER+ requestId+ url.replaceAll(".*\\.(\\w+)", ".$1");
		}
//		BufferedImage originalImage = ImageIO.read(new URL(url));
//		ImageIO.write(originalImage, "jpeg", new File(tempFilename));

		try (FileOutputStream fos = new FileOutputStream(tempFilename)){
			IOUtils.copy(new URL(url).openStream(), fos);
		}

		return tempFilename;
	}
}
