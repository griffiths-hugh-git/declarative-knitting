package com.griffiths.hugh.declarative_knitting.core.rules;

import com.griffiths.hugh.declarative_knitting.core.model.rows.Row;
import com.griffiths.hugh.declarative_knitting.core.model.rows.Rule;
import com.griffiths.hugh.declarative_knitting.core.model.stitches.Technique;
import com.griffiths.hugh.declarative_knitting.core.model.stitches.TechniqueFactory;

public class StockingStitch implements Rule {
	private int rowNum;

	public StockingStitch(int rowNum) {
		this.rowNum = rowNum;
	}

	@Override
	public void apply(Row row) {
		final Technique technique;
		if (rowNum++ %2 ==0){
			// RS
			technique = TechniqueFactory.knit();
		} else {
			// WS
			technique = TechniqueFactory.purl();
		}

		row.getParentLoops().stream().forEach(loop -> row.knitNextStitch(technique));
	}
}
