package com.griffiths.hugh.declarative_knitting.images.lace;

import com.griffiths.hugh.declarative_knitting.core.model.rows.Row;
import com.griffiths.hugh.declarative_knitting.core.model.rows.Rule;
import com.griffiths.hugh.declarative_knitting.core.model.stitches.TechniqueFactory;
import com.griffiths.hugh.declarative_knitting.core.rules.decorators.RightSideOnly;
import java.util.Iterator;
import java.util.List;

public class PixelBasedLaceImageRule implements Rule {
	private final Iterator<boolean[]> image;

	private PixelBasedLaceImageRule(List<boolean[]> image) {
		this.image = image.iterator();
	}

	public static Rule getInstance(List<boolean[]> image){
		return new RightSideOnly(new PixelBasedLaceImageRule(image));
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
