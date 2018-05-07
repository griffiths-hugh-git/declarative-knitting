package com.griffiths.hugh.declarative_knitting.images.shadow;

import java.util.Arrays;
import java.util.List;
import org.junit.Test;

public class FlattenedImageTest {

	@Test
	public void flattenImage() {
		FlattenedImage flattenedImage = new FlattenedImage("src/test/resources/dachshund.jpg", 100, 1.5);
		List<boolean[]> image = flattenedImage.getPixels();

		image.stream().forEach(row -> {
			for (int i=0; i<row.length; i++){
				System.out.print(row[i]? "1" : "-");
			}
			System.out.println();
		});
	}
}