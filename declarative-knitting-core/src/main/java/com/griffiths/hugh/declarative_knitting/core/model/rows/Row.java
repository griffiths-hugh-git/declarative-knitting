package com.griffiths.hugh.declarative_knitting.core.model.rows;

import com.google.common.collect.Multimap;
import com.griffiths.hugh.declarative_knitting.core.model.stitches.Loop;
import com.griffiths.hugh.declarative_knitting.core.model.stitches.Stitch;
import com.griffiths.hugh.declarative_knitting.core.model.stitches.Technique;
import java.util.List;
import java.util.Map;

public interface Row {

	void knitNextStitch(Technique technique);

	boolean replaceStitch(int i, Technique technique);

	List<Loop> getParentLoops();

	List<Stitch> getStitches();

	String toString();

	int getNumChildLoops();

	Map<Loop, Stitch> getParentToStitchMap();

	Multimap<Loop, Integer> getLoopToTechniqueIndexMap();
}
