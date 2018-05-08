package com.griffiths.hugh.ui;

import com.griffiths.hugh.declarative_knitting.core.model.patterns.PatternSegment;
import com.griffiths.hugh.declarative_knitting.core.model.rows.Rule;
import com.griffiths.hugh.declarative_knitting.core.rendering.XlsxRenderer;
import com.griffiths.hugh.declarative_knitting.core.rules.BindOff;
import com.griffiths.hugh.declarative_knitting.core.rules.StockingStitch;
import com.griffiths.hugh.declarative_knitting.images.lace.PixelBasedLaceImageRule;
import com.griffiths.hugh.declarative_knitting.images.lace.cellular_automata.CellularAutomataGenerator;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//@RestController
public class CellularAutomataController {

	@RequestMapping("/healthcheck")
	public String healthcheck() {
		return new Date().toString();
	}

	@RequestMapping(value = "/ca", method = RequestMethod.GET)
	public void generateCellularAutomaton(final HttpServletResponse response, @RequestParam("rule") final String ruleStr,
										  @RequestParam("rows") final int rows) throws IOException {
		if (ruleStr == null || !ruleStr.matches("[01]{8}")) {
			throw new IllegalArgumentException("Invalid rule - should be 8 binary values, e.g. 01110110 : "+ruleStr);
		} else if (rows <= 0 || rows >= 1000) {
			throw new IllegalArgumentException("Invalid number of rows - should be between 1 and 1000");
		}

		response.addHeader("Content-Disposition",  "attachment; filename=\"cellular_automaton.xlsx\"");
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

		final boolean[] rule = parseRule(ruleStr);
		generatePattern(response.getOutputStream(), rule, rows);

		// Flush
		response.flushBuffer();
	}

	private boolean[] parseRule(String ruleStr) {
		boolean[] rule = new boolean[8];

		for (int i = 0; i < 8; i++) {
			rule[i] = (ruleStr.charAt(i) == '1');
		}

		return rule;
	}

	private void generatePattern(OutputStream outputStream, boolean[] rule, int rows) throws IOException{
		List<boolean[]> result = generateCellularAutomatonPattern(rule, rows);
		Rule imageRule = PixelBasedLaceImageRule.getInstance(result);

		PatternSegment patternSegment = PatternSegment.castOn(100);
		patternSegment.addRule(new StockingStitch(0)).addRule(imageRule)
				.knitRows(100);
		patternSegment.clearRules().addRule(new BindOff())
				.knitRow();

		try (XlsxRenderer renderer = new XlsxRenderer(outputStream)){
			renderer.render(patternSegment);
		}
	}

	private List<boolean[]> generateCellularAutomatonPattern(boolean[] rule, int rows) throws IOException {
		CellularAutomataGenerator cellularAutomataGenerator = new CellularAutomataGenerator();

		return cellularAutomataGenerator.generateCellularAutomaton(rule, rows);
	}

	private void printBooleanArray(boolean[] row, Writer writer) throws IOException {
		for (int i = 0; i < row.length; i++) {
			writer.write(row[i] ? "1" : "-");
		}
		writer.write("\n");
	}
}
