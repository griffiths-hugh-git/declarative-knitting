package com.griffiths.hugh.declarative_knitting.core.rendering;

import com.griffiths.hugh.declarative_knitting.core.model.patterns.Pattern;
import com.griffiths.hugh.declarative_knitting.core.model.patterns.PatternSegment;
import com.griffiths.hugh.declarative_knitting.core.model.stitches.Stitch;
import com.griffiths.hugh.declarative_knitting.core.rendering.util.RowCompressUtil;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

public class TextRenderer implements Renderer, AutoCloseable {
	private final PrintWriter printWriter;

	public TextRenderer(OutputStream outputStream) {
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
			for(int i=0; i<entry.getKey().length(); i++)
				printWriter.print('-');
			printWriter.println();

			// Render the segment
			render(entry.getValue());
			printWriter.println();
		});
	}

	@Override
	public void render(PatternSegment patternSegment) {
		patternSegment.getRows().stream().map(row -> row.getStitches())
				.map(stitches -> renderRow(stitches))
				.forEach(printWriter::println);
	}

	private String renderRow(List<Stitch> stitches) {
		List<String> chars = stitches.stream().map(Stitch::toString).collect(Collectors.toList());
		return RowCompressUtil.compressLongestBlock(chars);
	}

}
