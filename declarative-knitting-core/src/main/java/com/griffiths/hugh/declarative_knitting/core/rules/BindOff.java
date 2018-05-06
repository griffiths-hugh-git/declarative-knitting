package com.griffiths.hugh.declarative_knitting.core.rules;

import com.griffiths.hugh.declarative_knitting.core.model.rows.Row;
import com.griffiths.hugh.declarative_knitting.core.model.rows.Rule;
import com.griffiths.hugh.declarative_knitting.core.model.stitches.Technique;
import com.griffiths.hugh.declarative_knitting.core.model.stitches.TechniqueFactory;

public class BindOff implements Rule {
	@Override
	public void apply(Row row) {
		Technique bindOff = TechniqueFactory.bindOff();
		row.getParentLoops().stream().forEach(loop -> {
			row.knitNextStitch(bindOff);
		});
	}
}
