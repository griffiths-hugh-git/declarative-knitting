package com.griffiths.hugh.declarative_knitting.core.model;

import com.griffiths.hugh.declarative_knitting.core.model.patterns.PatternSegment;
import org.junit.Test;

import static com.griffiths.hugh.declarative_knitting.core.rules.RuleFactory.bindOff;
import static com.griffiths.hugh.declarative_knitting.core.rules.RuleFactory.blockColour;
import static com.griffiths.hugh.declarative_knitting.core.rules.RuleFactory.stockingStitch;

public class StockingStitchTest {

	@Test
	public void testStockingStitchPattern() {
		PatternSegment patternSegment = PatternSegment.castOn(20);

		patternSegment.addRule(stockingStitch(0))
				.addColourRule(blockColour(0))
				.knitRows(10);
		patternSegment.clearRules().addRule(bindOff())
				.knitRow();

		patternSegment.getRows().forEach(
				System.out::println
		);
	}
}