package com.griffiths.hugh.declarative_knitting.core.model;

import com.griffiths.hugh.declarative_knitting.core.model.patterns.PatternSegment;
import com.griffiths.hugh.declarative_knitting.core.rules.BindOff;
import com.griffiths.hugh.declarative_knitting.core.rules.RibStitch;
import com.griffiths.hugh.declarative_knitting.core.rules.StockingStitch;
import com.griffiths.hugh.declarative_knitting.core.rules.modifiers.IncreaseBothEnds;
import org.junit.Test;

public class SleeveTest {

	@Test
	public void testSleevePattern(){
		PatternSegment patternSegment = PatternSegment.castOn(16);

		patternSegment.addRule(new RibStitch(0))
				.knitRows(6);
		patternSegment.clearRules().addRule(new StockingStitch(0))
				.addRule(new IncreaseBothEnds(0))
				.knitRows(10);
		patternSegment.clearRules().addRule(new BindOff())
				.knitRow();

		patternSegment.getRows().forEach(
				System.out::println
		);
	}
}