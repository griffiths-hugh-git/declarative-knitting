package com.griffiths.hugh.declarative_knitting.core.rules;

import com.griffiths.hugh.declarative_knitting.core.model.rows.Row;
import com.griffiths.hugh.declarative_knitting.core.model.rows.Rule;
import com.griffiths.hugh.declarative_knitting.core.model.stitches.TechniqueFactory;

public class RibStitch implements Rule {
	private int rowNum;

	public RibStitch(final int rowNum) {
		this.rowNum = rowNum;
	}

	@Override
	public void apply(final Row row) {
		for (int i = 0; i < row.getParentLoops().size(); i++) {
			if ((rowNum + i) % 2 == 0) {
				row.knitNextStitch(TechniqueFactory.knit());
			} else {
				row.knitNextStitch(TechniqueFactory.purl());
			}
		}

		rowNum++;
	}
}
