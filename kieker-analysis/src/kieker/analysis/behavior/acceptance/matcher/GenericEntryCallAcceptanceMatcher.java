/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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
package kieker.analysis.behavior.acceptance.matcher;

import java.util.List;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.analysis.behavior.data.EntryCallEvent;

/**
 * Allows to match operation and class signatures regarding a set of patterns to decide
 * whether they belong to the user behavior.
 *
 * @author Reiner Jung
 * @since 2.0.0
 */
public class GenericEntryCallAcceptanceMatcher implements IEntryCallAcceptanceMatcher {

	private static final Logger LOGGER = LoggerFactory.getLogger(GenericEntryCallAcceptanceMatcher.class);

	private final List<Pattern> classPatterns;
	private final List<Pattern> operationPatterns;
	private final boolean inverse;

	public GenericEntryCallAcceptanceMatcher(final List<Pattern> classPatterns, final List<Pattern> operationPatterns, final boolean inverse) {
		this.classPatterns = classPatterns;
		this.operationPatterns = operationPatterns;
		this.inverse = inverse;
	}

	@Override
	public boolean match(final EntryCallEvent call) {
		if (this.inverse) {
			return this.inverseMatch(call.getClassSignature(), this.classPatterns) && this.inverseMatch(call.getOperationSignature(), this.operationPatterns);
		} else {
			return this.match(call.getClassSignature(), this.classPatterns) && this.match(call.getOperationSignature(), this.operationPatterns);
		}
	}

	/**
	 * Check whether the signature matches one pattern.
	 *
	 * @param signature
	 *            signature to be matched
	 * @param patterns
	 *            patterns to be used
	 * @return returns true if any pattern matches the signature
	 */
	private boolean match(final String signature, final List<Pattern> patterns) {
		for (final Pattern pattern : patterns) {
			if (pattern.matcher(signature).matches()) {
				return true;
			}
		}
		LOGGER.debug("Discarded signature {}", signature);
		return false;
	}

	/**
	 * Check whether the signature matches any pattern.
	 *
	 * @param signature
	 *            signature to be matched
	 * @param patterns
	 *            patterns to be used
	 * @return returns true if no pattern matched, else false
	 */
	private boolean inverseMatch(final String signature, final List<Pattern> patterns) {
		for (final Pattern pattern : patterns) {
			if (pattern.matcher(signature).matches()) {
				return false;
			}
		}
		return true;
	}
}
