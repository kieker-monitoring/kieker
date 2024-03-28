package kieker.monitoring.util;

import java.util.LinkedList;
import java.util.List;

import kieker.monitoring.core.signaturePattern.InvalidPatternException;
import kieker.monitoring.core.signaturePattern.PatternParser;

public class KiekerPatternUtil {
	public static List<KiekerPattern> getPatterns(String instrumentables) {
		List<KiekerPattern> patternObjects = new LinkedList<KiekerPattern>();
		String[] patterns = instrumentables.split(";");
		for (String pattern : patterns) {
			String clazzMethodAndPrefix = pattern.substring(0, pattern.indexOf('('));
			String clazzAndPrefix = clazzMethodAndPrefix.substring(0, pattern.lastIndexOf('.'));
			String onlyClazz = clazzAndPrefix.contains(" ")
					? clazzAndPrefix.substring(clazzAndPrefix.lastIndexOf(" ") + 1)
					: clazzAndPrefix;
			try {
				KiekerPattern patternObject = new KiekerPattern(onlyClazz, PatternParser.parseToPattern(pattern));
				patternObjects.add(patternObject);
			} catch (InvalidPatternException e) {
				e.printStackTrace();
			}
		}
		return patternObjects;
	}
	
	public static boolean classIsContained(List<KiekerPattern> patternObjects, String clazz) {
		for (KiekerPattern pattern : patternObjects) {
			if (clazz.equals(pattern.getOnlyClass())) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean methodIsContained(List<KiekerPattern> patternObjects, String signature) {
		for (KiekerPattern pattern : patternObjects) {
			if (pattern.getFullPattern().matcher(signature).matches()) {
				return true;
			}
		}
		return false;
	}
}
