package com.griffiths.hugh.declarative_knitting.core.rendering;

import com.griffiths.hugh.declarative_knitting.core.model.patterns.Pattern;
import com.griffiths.hugh.declarative_knitting.core.model.patterns.PatternSegment;
import com.griffiths.hugh.declarative_knitting.core.rules.BindOff;
import com.griffiths.hugh.declarative_knitting.core.rules.StockingStitch;
import com.griffiths.hugh.declarative_knitting.core.rules.colour.BlockColour;
import com.griffiths.hugh.declarative_knitting.core.rules.colour.StripedColour;
import com.griffiths.hugh.declarative_knitting.core.rules.modifiers.IncreaseBothEnds;
import org.junit.Ignore;

@Ignore
public class CommonPatternsUtil {
	public static Pattern knitShapes() {
		Pattern p = new Pattern();
		PatternSegment square = p.createSegment("Square", 6);
		square.addRule(new StockingStitch(1))
				.addColourRule(new StripedColour(1, 1, 1, 2))
				.knitRows(4);
		square.clearRules().addRule(new BindOff())
				.addColourRule(new BlockColour(1)).knitRow();

		PatternSegment triangle = p.createSegment("Triangle", 2);
		triangle.addRule(new StockingStitch(1)).addRule(new IncreaseBothEnds(1))
				.addColourRule(new StripedColour(1, 2, 1, 3))
				.knitRows(6);
		triangle.clearRules().addRule(new BindOff()).addColourRule(new BlockColour(3)).knitRow();
		return p;
	}

}
