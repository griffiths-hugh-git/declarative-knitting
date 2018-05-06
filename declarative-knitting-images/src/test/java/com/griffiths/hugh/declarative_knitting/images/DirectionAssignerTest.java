package com.griffiths.hugh.declarative_knitting.images;

import com.griffiths.hugh.declarative_knitting.images.cellular_automata.CellularAutomataGenerator;
import java.util.List;
import org.junit.Test;

import static com.griffiths.hugh.declarative_knitting.images.DirectionAssigner.printDirectionsArray;

public class DirectionAssignerTest {
	@Test
	public void main() {
		CellularAutomataGenerator cellularAutomataGenerator = new CellularAutomataGenerator();

		boolean[] rule = {false, true, true, true, false, true, true, false};

		List<boolean[]> resultList = cellularAutomataGenerator.generateCellularAutomaton(rule, 100);
		List<int[]> directionsList = new DirectionAssigner().assignDirections(resultList);

		for (int i = 0; i < directionsList.size(); i++) {
			printDirectionsArray(directionsList.get(i));
		}
	}
}