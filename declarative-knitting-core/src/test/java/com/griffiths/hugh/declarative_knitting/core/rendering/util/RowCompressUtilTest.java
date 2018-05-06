package com.griffiths.hugh.declarative_knitting.core.rendering.util;

import com.griffiths.hugh.declarative_knitting.core.rendering.TextRenderer;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

import static org.junit.Assert.*;

public class RowCompressUtilTest {

	@Test
	public void compressLongestBlock() {
		assertEquals("A, B, C", testCompress("ABC"));
		assertEquals("A, B 3, C", testCompress("ABBBC"));
		assertEquals("B 3", testCompress("BBB"));
		assertEquals("* A, B * 2 times, C", testCompress("ABABC"));
		assertEquals("* A, B * 2 times, * C, D * 3 times", testCompress("ABABCDCDCD"));
		assertEquals("* A 3, B * 2 times, C 3, D", testCompress("AAABAAABCCCD"));
		assertEquals("* A, B * 2 times, C", testCompress("ABABC"));
	}

	private String testCompress(String string) {
		List<String> inputSymbols = Arrays.asList(string.split(""));
		return RowCompressUtil.compressLongestBlock(inputSymbols);
	}
}