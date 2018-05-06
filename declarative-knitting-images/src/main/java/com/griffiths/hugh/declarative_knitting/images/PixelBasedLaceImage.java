package com.griffiths.hugh.declarative_knitting.images;

import com.griffiths.hugh.declarative_knitting.core.model.rows.Row;
import com.griffiths.hugh.declarative_knitting.core.model.rows.Rule;
import com.griffiths.hugh.declarative_knitting.core.model.stitches.TechniqueFactory;
import com.griffiths.hugh.declarative_knitting.core.rules.decorators.RightSideOnly;
import java.util.Iterator;
import java.util.List;

public class PixelBasedLaceImage implements Rule {
	private final Iterator<boolean[]> image;

	private PixelBasedLaceImage(List<boolean[]> image) {
		this.image = image.iterator();
	}

	public static Rule getInstance(List<boolean[]> image){
		return new RightSideOnly(new PixelBasedLaceImage(image));
	}

	@Override
	public void apply(Row row) {
		if (!image.hasNext())
			return;

		boolean[] imageRow = image.next();

		for (int i=0; i<imageRow.length; i++){
			if (imageRow[i]){
				// The pixel is on, do a YO
				row.replaceStitch(2*i, TechniqueFactory.yarnOver());
				row.replaceStitch(2*i+1, TechniqueFactory.k2tog());
			}
		}
	}
}
