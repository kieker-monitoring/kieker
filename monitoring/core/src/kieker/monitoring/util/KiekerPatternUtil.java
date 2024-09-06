/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
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

import org.apache.commons.io.FilenameUtils;

import kieker.monitoring.core.signaturePattern.InvalidPatternException;
import kieker.monitoring.core.signaturePattern.PatternParser;

/**
 * Utility class for checking whether something should be monitored or not (which is specified by a semicolon separated list of kieker patterns).
 */
public class KiekerPatternUtil {
	public static List<KiekerPattern> getPatterns(final String instrumentables) {
		final List<KiekerPattern> patternObjects = new LinkedList<>();
		final String[] patterns = instrumentables.split(";");
		for (final String pattern : patterns) {
			if (pattern.contains("(")) {
				parseMethodPattern(patternObjects, pattern);
			} else {
				// In case there is something like * my.package.Class, we need to get rid of everything before the last space
				final String clazzName = pattern.substring(pattern.lastIndexOf(" ") + 1);
				final KiekerPattern patternObject = new KiekerPattern(clazzName, null);
				patternObjects.add(patternObject);
			}

		}
		return patternObjects;
	}

	private static void parseMethodPattern(final List<KiekerPattern> patternObjects, final String pattern) {
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

	public static boolean classIsContained(final List<KiekerPattern> patternObjects, final String clazz) {
		for (final KiekerPattern pattern : patternObjects) {
			if (clazz.equals(pattern.getOnlyClass())) {
				return true;
			}
			if (FilenameUtils.wildcardMatch(clazz, pattern.getOnlyClass())) {
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
