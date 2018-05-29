package com.griffiths.hugh.declarative_knitting.images.shadow;

import java.util.List;
import org.junit.Test;

public class FlattenedImageTest {

	@Test
	public void flattenImage() {
		final FlattenedImage flattenedImage = new FlattenedImage("src/test/resources/dachshund.jpg", 100, 1.5, 2);
		renderFlattenedImage(flattenedImage);
	}

	@Test
	public void flattenImage_threeLayers() {
		final FlattenedImage flattenedImage = new FlattenedImage("src/test/resources/dachshund.jpg", 100, 1.5, 3);
		renderFlattenedImage(flattenedImage);
	}

	private void renderFlattenedImage(FlattenedImage flattenedImage) {
		final List<int[]> image = flattenedImage.getPixels();

		image.stream().forEach(row -> {
			for (int i = 0; i < row.length; i++) {
				System.out.print(renderChar(row[i]));
			}
			System.out.println();
		});
	}

	private Object renderChar(int i) {
		return i == 0 ? "-" : i;
	}

}