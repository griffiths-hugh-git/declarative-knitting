package com.griffiths.hugh.declarative_knitting.core.model.rows.filters;

import com.griffiths.hugh.declarative_knitting.core.model.rows.Row;

public class FixedOffsetFilter implements Filter {
	private final int startOffset;
	private final int endOffset;

	public FixedOffsetFilter(final int startOffset, final int endOffset) {
		this.startOffset = startOffset;
		this.endOffset = endOffset;
	}

	public static FixedOffsetFilter fixedOffset(final int startOffset, final int endOffset) {
		return new FixedOffsetFilter(startOffset, endOffset);
	}

	@Override
	public Row filterRow(final Row originalRow) {
		return new FilteredRow(originalRow, startOffset, originalRow.getParentLoops().size() - endOffset - 1);
	}
}
