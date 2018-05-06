package com.griffiths.hugh.declarative_knitting.core.rendering;

import com.griffiths.hugh.declarative_knitting.core.model.patterns.Pattern;
import com.griffiths.hugh.declarative_knitting.core.model.patterns.PatternSegment;
import com.griffiths.hugh.declarative_knitting.core.model.stitches.Stitch;
import com.griffiths.hugh.declarative_knitting.core.rendering.util.RowCompressUtil;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Renderer designed to render source code for the LaTeX KnittingPattern class:
 * https://ctan.org/tex-archive/macros/latex/contrib/knittingpattern?lang=en
 *
 * This is intended to produce snippets to scaffold patterns as much as possible, it won't produce entire documents.
 */
public class LatexRenderer implements Renderer, AutoCloseable {
	private final PrintWriter printWriter;
	public LatexRenderer(OutputStream outputStream) {
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
			printWriter.println(String.format("\\section*{%s}\n\n" +
					"\\intro{\nDescription text, description text.\n}{lion.png}\n", entry.getKey()));

			// Render the segment
			render(entry.getValue());
			printWriter.println();
		});
	}

	@Override
	public void render(PatternSegment patternSegment) {
		printWriter.println("\\begin{pattern}{colour1}{colour2}");
		patternSegment.getRows().stream()
				.map(row ->
					String.format("\\phantom{}%s & (%d)\\\\", renderRow(row.getStitches()), row.getNumChildLoops()))
				.forEach(printWriter::println);
		printWriter.println("\\end{pattern}");
	}

	private String renderRow(List<Stitch> stitches) {
		List<String> chars = stitches.stream().map(Stitch::toString).collect(Collectors.toList());
		return RowCompressUtil.compressLongestBlock(chars);
	}
}
