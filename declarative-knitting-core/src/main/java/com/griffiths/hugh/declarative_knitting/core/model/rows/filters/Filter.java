package com.griffiths.hugh.declarative_knitting.core.model.rows.filters;

import com.griffiths.hugh.declarative_knitting.core.model.rows.Row;

public interface Filter {
	Row filterRow(Row originalRow);
}
