package com.griffiths.hugh.declarative_knitting.core.rules.decorators;

import com.griffiths.hugh.declarative_knitting.core.model.rows.Row;
import com.griffiths.hugh.declarative_knitting.core.model.rows.Rule;
import com.griffiths.hugh.declarative_knitting.core.model.rows.filters.Filter;
import java.util.ArrayList;
import java.util.List;

public class Panel implements Rule {
	private final List<Rule> panelRules = new ArrayList<>();
	private final Filter filter;

	public Panel(final Filter filter) {
		this.filter = filter;
	}

	public static Panel createPanel(final Filter filter) {
		return new Panel(filter);
	}

	public Panel addRule(final Rule panelRule) {
		panelRules.add(panelRule);
		return this;
	}

	public Panel clearRules() {
		panelRules.clear();
		return this;
	}

	@Override
	public void apply(final Row row) {
		final Row filteredRow = filter.filterRow(row);

		panelRules.stream().forEach(rule -> rule.apply(filteredRow));
	}
}
