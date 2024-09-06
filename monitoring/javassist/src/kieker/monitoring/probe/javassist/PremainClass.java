/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/
package kieker.monitoring.probe.javassist;

import java.lang.instrument.Instrumentation;
import java.util.List;

import kieker.monitoring.util.KiekerPattern;
import kieker.monitoring.util.KiekerPatternUtil;

/**
 * Javassist monitoring.
 *
 * @author David G. Reichelt
 */
public final class PremainClass { // NOPMD not sure whether this class could be renamed

	private static Instrumentation currentInstrumentation;

	private PremainClass() {
		// Nothing to do here
	}

	public static void premain(final String agentArgs, final Instrumentation inst) {
		currentInstrumentation = inst;

		final String instrumentables = System.getenv("KIEKER_SIGNATURES");
		if (instrumentables != null) {
			final List<KiekerPattern> patternObjects = KiekerPatternUtil.getPatterns(instrumentables);
			final KiekerClassTransformer kiekerTransformer = new KiekerClassTransformer(patternObjects);
			currentInstrumentation.addTransformer(kiekerTransformer);
		} else {
			System.err.println("Environment variable KIEKER_SIGNATURES not defined - not instrumenting anything!"); // NOPMD
		}
	}

}
