package com.griffiths.hugh.declarative_knitting.core.model;

import com.griffiths.hugh.declarative_knitting.core.model.patterns.PatternSegment;
import com.griffiths.hugh.declarative_knitting.core.rules.RibStitch;
import com.griffiths.hugh.declarative_knitting.core.rules.StockingStitch;
import org.junit.Test;

import static com.griffiths.hugh.declarative_knitting.core.rules.RuleFactory.bindOff;
import static com.griffiths.hugh.declarative_knitting.core.rules.RuleFactory.increaseBothEnds;

public class SleeveTest {

	@Test
	public void testSleevePattern(){
		PatternSegment patternSegment = PatternSegment.castOn(16);

		patternSegment.addRule(new RibStitch(0))
				.knitRows(6);
		patternSegment.clearRules().addRule(new StockingStitch(0))
				.addRule(increaseBothEnds(0))
				.knitRows(10);
		patternSegment.clearRules().addRule(bindOff())
				.knitRow();

		patternSegment.getRows().forEach(
				System.out::println
		);
	}
}