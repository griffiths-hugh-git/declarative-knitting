package com.griffiths.hugh.ui.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.springframework.http.HttpStatus;

public class ImageUtil {
	private static final File IMAGE_FOLDER = new File(System.getProperty("java.io.tmpdir") + "images");
	private static final int MAX_IMAGE_SIZE_BYTES = 1024 * 1024 * 10;
	private static final String IMAGE_MIME_TYPE = "image";

	private static final CloseableHttpClient HTTP_CLIENT = HttpClients.createDefault();

	static {
		IMAGE_FOLDER.mkdirs();
	}

	public static File downloadImageFile(String requestId, String url) throws ImageNotFoundException {
		// Try to connect
		try (CloseableHttpResponse httpResponse = HTTP_CLIENT.execute(new HttpGet(url));) {
			// Check response
			if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.OK.value()) {
				throw new ImageNotFoundException("Failed to download URL : " + httpResponse.getStatusLine().getReasonPhrase());
			}
			// Check mime type
			else if (!httpResponse.getEntity().getContentType().getValue().startsWith(IMAGE_MIME_TYPE)) {
				throw new ImageNotFoundException("URL is not an image type");
			}
			// Check length
			else if (httpResponse.getEntity().getContentLength() > MAX_IMAGE_SIZE_BYTES) {
				throw new ImageNotFoundException("Image is too large to process");
			}

			// Construct filename
			String extension = httpResponse.getEntity().getContentType().getValue().replaceAll(".*/", "");
			if (!extension.matches("\\w+")) {
				throw new ImageNotFoundException("Could not handle this image type : " + extension);
			}
			File tempFile = new File(IMAGE_FOLDER, String.format("%s.%s", requestId, extension));

			// Write data to file
			try (FileOutputStream fos = new FileOutputStream(tempFile)) {
				IOUtils.copy(new URL(url).openStream(), fos);
			} catch (IOException e) {
				throw new ImageNotFoundException("Failed to copy image", e);
			}

			// Check the image can be read
			Mat imread = Imgcodecs.imread(tempFile.getAbsolutePath());
			if (imread==null || imread.width()==0){
				throw new ImageNotFoundException("Unable to read image");
			}

			return tempFile;
		} catch (IOException e) {
			throw new ImageNotFoundException("Failed to download image", e);
		}
	}
}
