package com.griffiths.hugh.declarative_knitting.core.rendering;

import com.griffiths.hugh.declarative_knitting.core.model.patterns.Pattern;
import com.griffiths.hugh.declarative_knitting.core.model.patterns.PatternSegment;
import com.griffiths.hugh.declarative_knitting.core.model.stitches.Stitch;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.stream.Collectors;

/**
 * Simple Renderer implementation which outputs a pattern as a CSV
 */
public class CsvRenderer implements Renderer, AutoCloseable {
	private final PrintWriter printWriter;

	public CsvRenderer(OutputStream outputStream) {
		this.printWriter = new PrintWriter(outputStream);
	}

	@Override
	public void close() throws Exception {
		printWriter.close();
	}

	@Override
	public void render(Pattern pattern) {
		pattern.getSegments().entrySet().forEach(entry -> {
			// Print the title
			printWriter.println(entry.getKey());
			printWriter.println();

			// Render the segment
			render(entry.getValue());
			printWriter.println();
		});
	}

	@Override
	public void render(PatternSegment patternSegment) {
		patternSegment.getRows().stream().map(row -> row.getStitches())
				.map(stitches -> String.join(",", stitches.stream().map(Stitch::toString).collect(Collectors.toList())))
				.forEach(printWriter::println);
	}


}
