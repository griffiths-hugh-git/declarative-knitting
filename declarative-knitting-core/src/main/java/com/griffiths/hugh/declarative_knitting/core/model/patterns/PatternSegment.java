package com.griffiths.hugh.declarative_knitting.core.model.patterns;

import com.griffiths.hugh.declarative_knitting.core.model.rows.BasicRow;
import com.griffiths.hugh.declarative_knitting.core.model.rows.ColourRule;
import com.griffiths.hugh.declarative_knitting.core.model.rows.Row;
import com.griffiths.hugh.declarative_knitting.core.model.rows.Rule;
import com.griffiths.hugh.declarative_knitting.core.model.stitches.Technique;
import com.griffiths.hugh.declarative_knitting.core.model.stitches.TechniqueFactory;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

public class PatternSegment {
	protected final List<Rule> ruleList = new LinkedList<>();
	protected final List<ColourRule> colourRuleList = new LinkedList<>();
	protected final List<Row> rows = new LinkedList<>();
	private BasicRow rowInProgress;

	public PatternSegment(final BasicRow rowInProgress) {
		this.rowInProgress = rowInProgress;
	}

	public static PatternSegment castOn(final int stitches) {
		final BasicRow castOnRow = new BasicRow();
		final Technique technique = TechniqueFactory.castOn();
		IntStream.range(0, stitches).forEach(i -> castOnRow.knitNextStitch(technique));

		final PatternSegment patternSegment = new PatternSegment(castOnRow);
		patternSegment.knitRow();
		return patternSegment;
	}

	public PatternSegment addRule(final Rule rule) {
		ruleList.add(rule);
		return this;
	}

	public PatternSegment addColourRule(final ColourRule colourRule) {
		colourRuleList.add(colourRule);
		return this;
	}

	public PatternSegment clearRules() {
		ruleList.clear();
		colourRuleList.clear();
		return this;
	}

	public void knitRows(final int numRows) {
		for (int i = 0; i < numRows; i++) {
			knitRow();
		}
	}

	public void knitRow() {
		ruleList.stream().forEach(rule -> rule.apply(rowInProgress));
		colourRuleList.stream().forEach(rule -> rule.apply(rowInProgress));

		rows.add(rowInProgress);
		this.rowInProgress = new BasicRow(rowInProgress);
	}

	public List<Row> getRows() {
		return Collections.unmodifiableList(rows);
	}
}
