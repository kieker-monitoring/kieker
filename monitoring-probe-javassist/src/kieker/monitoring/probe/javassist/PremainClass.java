package kieker.monitoring.probe.javassist;

import java.lang.instrument.Instrumentation;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import kieker.monitoring.core.signaturePattern.InvalidPatternException;
import kieker.monitoring.core.signaturePattern.PatternParser;

class KiekerPattern {
	private final String onlyClass;
	private final Pattern fullPattern;

	public KiekerPattern(String onlyClass, Pattern fullPattern) {
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

public class PremainClass {

	private static Instrumentation currentInstrumentation;

	public static void premain(String agentArgs, Instrumentation inst) {
		currentInstrumentation = inst;
		

		String instrumentables = System.getenv("KIEKER_SIGNATURES");
		if (instrumentables != null) {
			List<KiekerPattern> patternObjects = new LinkedList<KiekerPattern>();
			String[] patterns = instrumentables.split(";");
			for (String pattern : patterns) {
				String clazzMethodAndPrefix = pattern.substring(0, pattern.indexOf('('));
				String clazzAndPrefix = clazzMethodAndPrefix.substring(0, pattern.lastIndexOf('.'));
				String onlyClazz = clazzAndPrefix.contains(" ") ? clazzAndPrefix.substring(clazzAndPrefix.lastIndexOf(" ")+1) : clazzAndPrefix;
				try {
					KiekerPattern patternObject = new KiekerPattern(onlyClazz, PatternParser.parseToPattern(pattern));
					patternObjects.add(patternObject);
				} catch (InvalidPatternException e) {
					e.printStackTrace();
				}
			}
			KiekerClassTransformer kiekerTransformer = new KiekerClassTransformer(patternObjects);
			currentInstrumentation.addTransformer(kiekerTransformer);
		} else {
			System.err.println("Environment variable KIEKER_SIGNATURES not defined - not instrumenting anything!");
		}
	}
}
