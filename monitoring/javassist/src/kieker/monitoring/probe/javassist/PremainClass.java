package kieker.monitoring.probe.javassist;

import java.lang.instrument.Instrumentation;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import kieker.monitoring.core.signaturePattern.InvalidPatternException;
import kieker.monitoring.core.signaturePattern.PatternParser;
import kieker.monitoring.util.KiekerPattern;
import kieker.monitoring.util.KiekerPatternUtil;

public class PremainClass {

	private static Instrumentation currentInstrumentation;

	public static void premain(String agentArgs, Instrumentation inst) {
		currentInstrumentation = inst;
		

		String instrumentables = System.getenv("KIEKER_SIGNATURES");
		if (instrumentables != null) {
			List<KiekerPattern> patternObjects = KiekerPatternUtil.getPatterns(instrumentables);
			KiekerClassTransformer kiekerTransformer = new KiekerClassTransformer(patternObjects);
			currentInstrumentation.addTransformer(kiekerTransformer);
		} else {
			System.err.println("Environment variable KIEKER_SIGNATURES not defined - not instrumenting anything!");
		}
	}

	
}
