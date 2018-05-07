package com.griffiths.hugh.declarative_knitting.images.lace.cellular_automata;

import com.griffiths.hugh.declarative_knitting.images.lace.ArrayUtils;
import java.util.List;
import org.junit.Test;

public class CellularAutomataGeneratorTest {
	@Test
	public void main() {
		CellularAutomataGenerator cellularAutomataGenerator = new CellularAutomataGenerator();
		boolean[] rule = {false, true, true, true, false, true, true, false};

		List<boolean[]> result = cellularAutomataGenerator.generateCellularAutomaton(rule, 50);

		result.stream().forEach(ArrayUtils::printBooleanArray);
	}
}