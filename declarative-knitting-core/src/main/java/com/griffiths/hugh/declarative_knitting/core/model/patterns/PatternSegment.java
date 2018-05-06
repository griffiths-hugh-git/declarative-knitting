package com.griffiths.hugh.declarative_knitting.core.model.patterns;

import com.griffiths.hugh.declarative_knitting.core.model.rows.ColourRule;
import com.griffiths.hugh.declarative_knitting.core.model.rows.Row;
import com.griffiths.hugh.declarative_knitting.core.model.rows.Rule;
import com.griffiths.hugh.declarative_knitting.core.model.stitches.Technique;
import com.griffiths.hugh.declarative_knitting.core.model.stitches.TechniqueFactory;
import java.util.*;
import java.util.stream.IntStream;

public class PatternSegment {
	protected final List<Rule> ruleList = new LinkedList<>();
	protected final List<ColourRule> colourRuleList = new LinkedList<>();
	protected final List<Row> rows = new LinkedList<>();

	public PatternSegment addRule(Rule rule){
		ruleList.add(rule);
		return this;
	}

	public PatternSegment addColourRule(ColourRule colourRule){
		colourRuleList.add(colourRule);
		return this;
	}

	public PatternSegment clearRules(){
		ruleList.clear();
		colourRuleList.clear();
		return this;
	}

	private Row rowInProgress;

	public PatternSegment(Row rowInProgress){
		this.rowInProgress=rowInProgress;
	}

	public static PatternSegment castOn(int stitches){
		Row castOnRow = new Row();
		Technique technique = TechniqueFactory.castOn();
		IntStream.range(0, stitches).forEach(i -> castOnRow.knitNextStitch(technique));

		return new PatternSegment(castOnRow);
	}

	public void knitRows(int numRows){
		for (int i=0; i<numRows; i++){
			knitRow();
		}
	}

	public void knitRow(){
		ruleList.stream().forEach(rule -> rule.apply(rowInProgress));
		colourRuleList.stream().forEach(rule -> rule.apply(rowInProgress));

		rows.add(rowInProgress);
		this.rowInProgress=new Row(rowInProgress);
	}

	public List<Row> getRows(){
		return Collections.unmodifiableList(rows);
	}
}
