package com.griffiths.hugh.declarative_knitting.core.rules.decorators;

import com.griffiths.hugh.declarative_knitting.core.model.patterns.PatternSegment;
import org.junit.Test;

import static com.griffiths.hugh.declarative_knitting.core.model.rows.filters.FixedOffsetFilter.fixedOffset;
import static com.griffiths.hugh.declarative_knitting.core.rules.RuleFactory.bindOff;
import static com.griffiths.hugh.declarative_knitting.core.rules.RuleFactory.increaseBothEnds;
import static com.griffiths.hugh.declarative_knitting.core.rules.RuleFactory.stockingStitch;
import static com.griffiths.hugh.declarative_knitting.core.rules.decorators.Panel.createPanel;

public class PanelTest {

	@Test
	public void testStockingStitchPattern() {
		final PatternSegment patternSegment = PatternSegment.castOn(20);

		patternSegment.addRule(stockingStitch(0))
				.addRule(
						createPanel(fixedOffset(2, 2)).addRule(
								increaseBothEnds(0))
				)
				.knitRows(10);
		patternSegment.clearRules().addRule(bindOff())
				.knitRow();

		patternSegment.getRows().forEach(
				System.out::println
		);
	}
}