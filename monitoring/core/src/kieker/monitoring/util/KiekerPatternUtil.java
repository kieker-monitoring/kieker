/***************************************************************************
 * Copyright 2024 Kieker Project (http://kieker-monitoring.net)
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

package kieker.monitoring.util;

import java.util.LinkedList;
import java.util.List;

import kieker.monitoring.core.signaturePattern.InvalidPatternException;
import kieker.monitoring.core.signaturePattern.PatternParser;

/**
 * Helper class for checking whether a signature is contained in KIEKER_PATTERNS
 * 
 * @author David Georg Reichelt
 */
public final class KiekerPatternUtil {

	private KiekerPatternUtil() {

	}

	public static List<KiekerPattern> getPatterns(final String instrumentables) {
		final List<KiekerPattern> patternObjects = new LinkedList<KiekerPattern>();
		final String[] patterns = instrumentables.split(";");
		for (final String pattern : patterns) {
			final String clazzMethodAndPrefix = pattern.substring(0, pattern.indexOf('('));
			final String clazzAndPrefix = clazzMethodAndPrefix.substring(0, pattern.lastIndexOf('.'));
			final String onlyClazz = clazzAndPrefix.contains(" ")
					? clazzAndPrefix.substring(clazzAndPrefix.lastIndexOf(' ') + 1)
					: clazzAndPrefix;
			try {
				final KiekerPattern patternObject = new KiekerPattern(onlyClazz, PatternParser.parseToPattern(pattern));
				patternObjects.add(patternObject);
			} catch (final InvalidPatternException e) {
				e.printStackTrace();
			}
		}
		return patternObjects;
	}

	public static boolean classIsContained(final List<KiekerPattern> patternObjects, final String clazz) {
		for (final KiekerPattern pattern : patternObjects) {
			if (clazz.equals(pattern.getOnlyClass())) {
				return true;
			}
		}
		return false;
	}

	public static boolean methodIsContained(final List<KiekerPattern> patternObjects, final String signature) {
		for (final KiekerPattern pattern : patternObjects) {
			if (pattern.getFullPattern().matcher(signature).matches()) {
				return true;
			}
		}
		return false;
	}
}
