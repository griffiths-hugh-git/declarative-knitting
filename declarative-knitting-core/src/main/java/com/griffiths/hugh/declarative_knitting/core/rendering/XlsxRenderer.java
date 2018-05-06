package com.griffiths.hugh.declarative_knitting.core.rendering;

import com.griffiths.hugh.declarative_knitting.core.model.patterns.Pattern;
import com.griffiths.hugh.declarative_knitting.core.model.patterns.PatternSegment;
import com.griffiths.hugh.declarative_knitting.core.model.rows.Row;
import com.griffiths.hugh.declarative_knitting.core.model.stitches.Stitch;
import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class XlsxRenderer implements Renderer, AutoCloseable {
	private final Map<Integer, Color> colourMap = new HashMap<>();
	private final Map<Integer, CellStyle> styleMap = new HashMap<>();
	private final XSSFWorkbook workbook;

	private final OutputStream outputStream;

	public XlsxRenderer(OutputStream outputStream) {
		workbook = new XSSFWorkbook();
		this.outputStream = outputStream;
	}

	public void addColour(int index, Color colour) {
		colourMap.put(index, colour);
	}

	@Override
	public void close() throws IOException {
		workbook.write(outputStream);
	}


	@Override
	public void render(Pattern pattern) {
		// Set the colours
		colourMap.putAll(pattern.getColours());

		pattern.getSegments().entrySet().forEach(entry -> {
			// Render the segment as a sheet
			createSheet(entry.getValue(), entry.getKey());
		});
	}

	@Override
	public void render(PatternSegment patternSegment) {
		createSheet(patternSegment, "Pattern");
	}

	private void createSheet(PatternSegment patternSegment, String sheetTitle) {
		Sheet patternSheet = workbook.createSheet(sheetTitle);

		// Set the cell sizes to stitch aspect ratios
		patternSheet.setDefaultColumnWidth(3);
		writeSheetContent(patternSegment, patternSheet);
	}

	private void writeSheetContent(PatternSegment patternSegment, Sheet patternSheet) {
		for (int rowNum = 0; rowNum < patternSegment.getRows().size(); rowNum++) {
			Row row = patternSegment.getRows().get(rowNum);
			org.apache.poi.ss.usermodel.Row sheetRow = patternSheet.createRow(rowNum);
			for (int cellNum = 0; cellNum < row.getStitches().size(); cellNum++) {
				Stitch st = row.getStitches().get(cellNum);
				Cell cell = sheetRow.createCell(cellNum);

				// Set the content
				cell.setCellValue(st.getType());

				// Set the background colour
				CellStyle style = getOrCreateCellStyle(st);
				cell.setCellStyle(style);
			}
		}
	}

	private CellStyle getOrCreateCellStyle(Stitch st) {
		if (!styleMap.containsKey(st.getColour())) {
			XSSFCellStyle cellStyle = workbook.createCellStyle();
			if (colourMap.containsKey(st.getColour())) {
				cellStyle.setFillForegroundColor(new XSSFColor(colourMap.get(st.getColour())));
			} else {
				// As a fallback, just use the predefined colours.  These will be quite random, but will at least
				// distinguish the colours.
				cellStyle.setFillForegroundColor((short) (st.getColour() + 1));
			}
			cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			styleMap.put(st.getColour(), cellStyle);
		}
		return styleMap.get(st.getColour());
	}
}
