package com.griffiths.hugh.declarative_knitting.images;

import com.griffiths.hugh.declarative_knitting.core.model.patterns.PatternSegment;
import com.griffiths.hugh.declarative_knitting.core.model.rows.Rule;
import com.griffiths.hugh.declarative_knitting.core.rendering.XlsxRenderer;
import com.griffiths.hugh.declarative_knitting.core.rules.BindOff;
import com.griffiths.hugh.declarative_knitting.core.rules.StockingStitch;
import com.griffiths.hugh.declarative_knitting.images.cellular_automata.CellularAutomataGenerator;
import java.io.FileOutputStream;
import java.util.List;
import org.junit.Test;

public class PixelBasedCellularAutomatonTest {

	@Test
	public void knitCellularAutomatonTest() throws Exception {
		List<boolean[]> result = generateCellularAutomatonPattern();
		Rule imageRule = PixelBasedLaceImage.getInstance(result);

		PatternSegment patternSegment = PatternSegment.castOn(100);
		patternSegment.addRule(new StockingStitch(0)).addRule(imageRule)
				.knitRows(100);
		patternSegment.clearRules().addRule(new BindOff())
				.knitRow();

		try (XlsxRenderer renderer = new XlsxRenderer(new FileOutputStream("target/ca.xlsx"))){
			renderer.render(patternSegment);
		}
	}

	private List<boolean[]> generateCellularAutomatonPattern() {
		CellularAutomataGenerator cellularAutomataGenerator = new CellularAutomataGenerator();
		boolean[] rule = {false, true, true, true, false, true, true, false};
		return cellularAutomataGenerator.generateCellularAutomaton(rule, 50);
	}
}