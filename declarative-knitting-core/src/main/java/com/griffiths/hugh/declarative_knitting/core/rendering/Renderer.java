package com.griffiths.hugh.declarative_knitting.core.rendering;

import com.griffiths.hugh.declarative_knitting.core.model.patterns.Pattern;
import com.griffiths.hugh.declarative_knitting.core.model.patterns.PatternSegment;

public interface Renderer {
	void render(Pattern pattern);

	void render(PatternSegment patternSegment);

}
