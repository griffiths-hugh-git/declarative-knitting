package com.griffiths.hugh.declarative_knitting.core.rules;

import com.griffiths.hugh.declarative_knitting.core.model.rows.ColourRule;
import com.griffiths.hugh.declarative_knitting.core.model.rows.Rule;
import com.griffiths.hugh.declarative_knitting.core.rules.colour.BlockColour;
import com.griffiths.hugh.declarative_knitting.core.rules.colour.StripedColour;
import com.griffiths.hugh.declarative_knitting.core.rules.modifiers.IncreaseBothEnds;

/**
 * Helper class exposing factory methods for the various Rules and ColourRules using intuitive language.
 * In future these could be split out thematically.
 */
public class RuleFactory {
	public static Rule bindOff(){
		return new BindOff();
	}
	public static Rule ribStitch(int numRow){
		return new RibStitch(numRow);
	}
	public static Rule stockingStitch(int numRow){
		return new StockingStitch(numRow);
	}
	public static Rule increaseBothEnds(int numRow){
		return IncreaseBothEnds.getInstance(numRow);
	}

	public static ColourRule blockColour(int blockColour) {
		return new BlockColour(blockColour);
	}
	public static ColourRule stripedColour(int primaryStripeWidth, int secondaryStripeWidth, int primaryColour, int secondaryColour){
		return new StripedColour(primaryStripeWidth, secondaryStripeWidth, primaryColour, secondaryColour);
	}
}
