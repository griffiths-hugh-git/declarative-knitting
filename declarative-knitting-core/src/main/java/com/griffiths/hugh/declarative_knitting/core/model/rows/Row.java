package com.griffiths.hugh.declarative_knitting.core.model.rows;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import com.griffiths.hugh.declarative_knitting.core.model.stitches.Loop;
import com.griffiths.hugh.declarative_knitting.core.model.stitches.Stitch;
import com.griffiths.hugh.declarative_knitting.core.model.stitches.Technique;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class Row {
	private final List<Loop> parentLoops;
	private final List<Technique> techniques = new ArrayList<>();

	// Calculated
	private final List<Stitch> stitches = new ArrayList<>();
	private final Map<Loop, Stitch> parentToStitchMap = new HashMap<>();
	private final Multimap<Loop, Integer> loopToTechniqueIndexMap = LinkedListMultimap.create();
	private boolean isComplete;
	// Detects whether changes have been made, to avoid recalculating unnecessarily.
	private boolean needsRecalcating=false;

	public Row(Row parent) {
		this.parentLoops = Collections.unmodifiableList(parent.getChildLoops());
	}

	public Row() {
		this.parentLoops = Collections.emptyList();
	}

	public void knitNextStitch(Technique technique) {
		techniques.add(technique);
		needsRecalcating=true;
	}

	/**
	 * Returns whether or not the replacement was successful.
	 * NB - if replacing a stitch leaves other loops unhandled, it is the responsibility of the caller to identify this.
	 *
	 * @param i
	 * @param technique
	 * @return
	 */
	public boolean replaceStitch(int i, Technique technique) {
		recalculate();

		if (i >= stitches.size()) {
			return false;
		} else {
			Stitch replacedStitch = stitches.get(i);
			// Look up the technique(s) impacted by the parents for the stitch we are replacing
			List<Integer> techniqueIndexesToRemove = replacedStitch.getParents().stream().flatMap(parent -> loopToTechniqueIndexMap.get(parent).stream())
					// Identity the affected techniques
					.sorted().collect(Collectors.toList());

			if (techniqueIndexesToRemove.isEmpty()){
				// Nothing to replace
				return false;
			}

			// Remove the affected techniques
			for (Integer techniqueIndex: techniqueIndexesToRemove){
				techniques.remove(techniqueIndex.intValue());
			}
			// Add the new technique
			techniques.add(techniqueIndexesToRemove.get(0).intValue(), technique);
			needsRecalcating=true;

			return true;
		}
	}

	public List<Loop> getParentLoops() {
		return parentLoops;
	}

	public List<Stitch> getStitches() {
		recalculate();
		return stitches;
	}

	public String toString() {
		return String.join(",", techniques.stream().map(Technique::toString).collect(Collectors.toList()));
	}

	public int getNumChildLoops(){
		return getChildLoops().size();
	}

	private List<Loop> getChildLoops() {
		if (recalculate()) {
			return stitches.stream().flatMap(stitch -> stitch.getChildren().stream()).collect(Collectors.toList());
		} else {
			throw new IllegalStateException("Row is not complete : " + parentLoops.size() + ", " + toString());
		}

	}

	private boolean recalculate() {
		if (!needsRecalcating){
			return true;
		}

		stitches.clear();
		parentToStitchMap.clear();
		loopToTechniqueIndexMap.clear();
		isComplete = false;

		Iterator<Loop> parents = parentLoops.iterator();
		try {
			for (int techniqueIndex =0; techniqueIndex< techniques.size(); techniqueIndex++){
				Technique technique = techniques.get(techniqueIndex);
				Stitch stitch = technique.apply(parents);

				stitches.add(stitch);
				for (Loop loop : stitch.getParents()){
					// Index the stitches by parent
					parentToStitchMap.put(loop, stitch);
					// Index the technique by parent
					loopToTechniqueIndexMap.put(loop, techniqueIndex);
				}
			}
			// The row is complete if we have used up precisely all the parents
			isComplete = !parents.hasNext();
		} catch (NoSuchElementException e) {
			// Indicates we overran the row
			isComplete = false;
		}

		needsRecalcating=false;
		return isComplete;
	}

	public static Row getChildRow(Row parent) {
		return new Row(parent);
	}
}
