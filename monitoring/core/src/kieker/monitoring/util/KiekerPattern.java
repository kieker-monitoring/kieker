package kieker.monitoring.util;

import java.util.regex.Pattern;

public class KiekerPattern {
	private final String onlyClass;
	private final Pattern fullPattern;

	public KiekerPattern(final String onlyClass, final Pattern fullPattern) {
		this.onlyClass = onlyClass;
		this.fullPattern = fullPattern;
	}

	public String getOnlyClass() {
		return onlyClass;
	}

	public Pattern getFullPattern() {
		return fullPattern;
	}
}
