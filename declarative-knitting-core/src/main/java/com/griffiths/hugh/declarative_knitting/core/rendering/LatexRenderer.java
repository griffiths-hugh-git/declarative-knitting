package com.griffiths.hugh.declarative_knitting.core.rendering;

import com.griffiths.hugh.declarative_knitting.core.model.patterns.Pattern;
import com.griffiths.hugh.declarative_knitting.core.model.patterns.PatternSegment;
import com.griffiths.hugh.declarative_knitting.core.model.stitches.Stitch;
import com.griffiths.hugh.declarative_knitting.core.rendering.util.RowCompressUtil;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * Renderer designed to render source code for the LaTeX KnittingPattern class:
 * https://ctan.org/tex-archive/macros/latex/contrib/knittingpattern?lang=en
 *
 * This is intended to produce snippets to scaffold patterns as much as possible, it won't produce entire documents.
 */
public class LatexRenderer implements Renderer, AutoCloseable {
	private static final String LATEX_DOCUMENT_PROPERTIES = "/latex_document.properties";
	private final PrintWriter printWriter;

	private Properties config;

	public LatexRenderer(OutputStream outputStream) {
		this.printWriter = new PrintWriter(outputStream);

		config = new Properties();
		try {
			config.load(this.getClass().getResourceAsStream(LATEX_DOCUMENT_PROPERTIES));
		} catch (IOException e) {
			throw new IllegalStateException("Configuration not found : "+LATEX_DOCUMENT_PROPERTIES);
		}
	}

	@Override
	public void close() throws IOException {
		printWriter.close();
	}

	@Override
	public void render(Pattern pattern) {
		// Front matter
		printWriter.println(config.getProperty("title_page"));

		printWriter.println(config.getProperty("colour_definitions"));

		printWriter.println(config.getProperty("introduction"));

		pattern.getSegments().entrySet().forEach(entry -> {
			// Print the title
			printWriter.println(String.format(config.getProperty("section_format"), entry.getKey()));

			// Render the segment
			render(entry.getValue());
			printWriter.println();
		});

		// End matter
		printWriter.println(config.getProperty("biography"));
		printWriter.println(config.getProperty("document_end"));
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
