package com.griffiths.hugh.declarative_knitting.core.model.rows.filters;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.griffiths.hugh.declarative_knitting.core.model.rows.Row;
import com.griffiths.hugh.declarative_knitting.core.model.stitches.Loop;
import com.griffiths.hugh.declarative_knitting.core.model.stitches.Stitch;
import com.griffiths.hugh.declarative_knitting.core.model.stitches.Technique;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FilteredRow implements Row {
	private final Row originalRow;
	private final int startSt;
	private final int endSt;

	public FilteredRow(final Row originalRow, final int startSt, final int endSt) {
		this.originalRow = originalRow;
		this.startSt = startSt;
		this.endSt = endSt;
	}

	@Override
	public void knitNextStitch(final Technique technique) {
		originalRow.knitNextStitch(technique);
	}

	@Override
	public boolean replaceStitch(final int i, final Technique technique) {
		return originalRow.replaceStitch(i + startSt, technique);
	}

	@Override
	public List<Loop> getParentLoops() {
		final List<Loop> parentLoops = originalRow.getParentLoops();
		if (parentLoops.size() > endSt) {
			return parentLoops.subList(startSt, endSt + 1);
		} else {
			return Collections.emptyList();
		}
	}

	@Override
	public List<Stitch> getStitches() {
		return getStitchStream().collect(Collectors.toList());
	}

	@Override
	public int getNumChildLoops() {
		return getStitchStream().flatMap(st -> st.getChildren().stream())
				.collect(Collectors.toList()).size();
	}

	@Override
	public Map<Loop, Stitch> getParentToStitchMap() {
		return getParentLoops().stream().
				collect(Collectors.toMap(loop -> loop, loop -> originalRow.getParentToStitchMap().get(loop)));
	}

	@Override
	public Multimap<Loop, Integer> getLoopToTechniqueIndexMap() {
		final Multimap<Loop, Integer> filteredMap = HashMultimap.create();
		getParentLoops().forEach(loop -> filteredMap.putAll(loop, originalRow.getLoopToTechniqueIndexMap().get(loop)));

		return filteredMap;
	}

	private Stream<Stitch> getStitchStream() {
		return getParentLoops().stream().map(loop -> originalRow.getParentToStitchMap().get(loop));
	}
}
