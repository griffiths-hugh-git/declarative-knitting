package com.griffiths.hugh.declarative_knitting.core.model;

import com.griffiths.hugh.declarative_knitting.core.model.patterns.PatternSegment;
import com.griffiths.hugh.declarative_knitting.core.rules.BindOff;
import com.griffiths.hugh.declarative_knitting.core.rules.colour.BlockColour;
import com.griffiths.hugh.declarative_knitting.core.rules.StockingStitch;
import org.junit.Test;

public class StockingStitchTest {

	@Test
	public void testStockingStitchPattern(){
		PatternSegment patternSegment = PatternSegment.castOn(20);

		patternSegment.addRule(new StockingStitch(0))
				.addColourRule(new BlockColour(0))
				.knitRows(10);
		patternSegment.clearRules().addRule(new BindOff())
				.knitRow();

		patternSegment.getRows().forEach(
				System.out::println
		);
	}
}