package com.griffiths.hugh.declarative_knitting.core.rules.modifiers;

import com.griffiths.hugh.declarative_knitting.core.model.rows.Row;
import com.griffiths.hugh.declarative_knitting.core.model.rows.Rule;
import com.griffiths.hugh.declarative_knitting.core.model.stitches.TechniqueFactory;
import com.griffiths.hugh.declarative_knitting.core.rules.decorators.RightSideOnly;

public class IncreaseBothEnds implements Rule {
	private int rowNum=0;

	private IncreaseBothEnds(int rowNum) {
		this.rowNum = rowNum;
	}

	@Override
	public void apply(Row row) {
		if (rowNum++ %2==0){
			// Replace the first stitch
			row.replaceStitch(0, TechniqueFactory.kfb());
			// Replace the last stitch
			row.replaceStitch(row.getStitches().size()-1, TechniqueFactory.kfb());
		}
	}

	public static Rule getInstance(int numRow){
		return new RightSideOnly(new IncreaseBothEnds(numRow), numRow%2 ==0);
	}
}
