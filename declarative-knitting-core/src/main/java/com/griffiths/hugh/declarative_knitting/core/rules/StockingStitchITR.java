package com.griffiths.hugh.declarative_knitting.core.rules;

import com.griffiths.hugh.declarative_knitting.core.model.rows.Row;
import com.griffiths.hugh.declarative_knitting.core.model.rows.Rule;
import com.griffiths.hugh.declarative_knitting.core.model.stitches.Technique;
import com.griffiths.hugh.declarative_knitting.core.model.stitches.TechniqueFactory;

public class StockingStitchITR implements Rule {
	@Override
	public void apply(final Row row) {
		final Technique technique = TechniqueFactory.knit();

		row.getParentLoops().stream().forEach(loop -> row.knitNextStitch(technique));
	}
}
