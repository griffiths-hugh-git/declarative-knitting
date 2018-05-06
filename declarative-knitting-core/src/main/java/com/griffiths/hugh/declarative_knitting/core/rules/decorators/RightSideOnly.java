package com.griffiths.hugh.declarative_knitting.core.rules.decorators;

import com.griffiths.hugh.declarative_knitting.core.model.rows.Row;
import com.griffiths.hugh.declarative_knitting.core.model.rows.Rule;

public class RightSideOnly implements Rule {
	private final Rule decoratedRule;
	private boolean isRightSide=true;

	public RightSideOnly(Rule decoratedRule) {
		this.decoratedRule = decoratedRule;
	}

	public RightSideOnly(Rule decoratedRule, boolean isRightSide) {
		this.decoratedRule = decoratedRule;
		this.isRightSide = isRightSide;
	}

	@Override
	public void apply(Row row) {
		if (isRightSide)
			decoratedRule.apply(row);

		isRightSide^=true;
	}
}
