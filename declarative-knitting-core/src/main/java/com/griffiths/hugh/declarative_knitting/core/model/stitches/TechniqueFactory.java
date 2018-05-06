package com.griffiths.hugh.declarative_knitting.core.model.stitches;

public class TechniqueFactory {
	public static Technique castOn(){
		return Technique.createTechnique(0, "CO", 1);
	}
	public static Technique knit(){
		return Technique.createTechnique(1, "K", 1);
	}
	public static Technique purl(){
		return Technique.createTechnique(1, "P", 1);
	}
	public static Technique kfb(){
		return Technique.createTechnique(1, "Kfb", 2);
	}
	public static Technique k2tog(){
		return Technique.createTechnique(2, "K2tog", 1);
	}
	public static Technique bindOff(){
		return Technique.createTechnique(1, "BO", 0);
	}

	public static Technique yarnOver(){
		return Technique.createTechnique(0, "YO", 1);
	}
}
