package com.griffiths.hugh.declarative_knitting.core.rendering;

import com.griffiths.hugh.declarative_knitting.core.model.patterns.Pattern;
import com.griffiths.hugh.declarative_knitting.core.model.patterns.PatternSegment;
import org.junit.Ignore;

import static com.griffiths.hugh.declarative_knitting.core.rules.RuleFactory.bindOff;
import static com.griffiths.hugh.declarative_knitting.core.rules.RuleFactory.blockColour;
import static com.griffiths.hugh.declarative_knitting.core.rules.RuleFactory.increaseBothEnds;
import static com.griffiths.hugh.declarative_knitting.core.rules.RuleFactory.ribStitch;
import static com.griffiths.hugh.declarative_knitting.core.rules.RuleFactory.stockingStitch;
import static com.griffiths.hugh.declarative_knitting.core.rules.RuleFactory.stripedColour;

/**
 * Util class containing instructions to knit some simple shapes to test the renderers.
 */
@Ignore
public class RendererTestsUtil {

	public static PatternSegment knitSleeve() {
		PatternSegment patternSegment = PatternSegment.castOn(16);

		patternSegment.addRule(ribStitch(0))
				.knitRows(6);
		patternSegment.clearRules().addRule(stockingStitch(0))
				.addRule(increaseBothEnds(0))
				.addColourRule(stripedColour(1, 1, 1, 2))
				.knitRows(10);
		patternSegment.clearRules().addRule(bindOff())
				.knitRow();
		return patternSegment;
	}

	public static Pattern knitShapes() {
		Pattern p = new Pattern();
		PatternSegment square = p.createSegment("Square", 6);
		square.addRule(stockingStitch(1))
				.addColourRule(stripedColour(1, 1, 1, 2))
				.knitRows(4);
		square.clearRules().addRule(bindOff())
				.addColourRule(blockColour(1)).knitRow();

		PatternSegment triangle = p.createSegment("Triangle", 2);
		triangle.addRule(stockingStitch(1)).addRule(increaseBothEnds(1))
				.addColourRule(stripedColour(1, 2, 1, 3))
				.knitRows(6);
		triangle.clearRules().addRule(bindOff()).addColourRule(blockColour(3)).knitRow();
		return p;
	}

}
