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

package kieker.monitoring.core.signaturePattern;

import java.io.Serializable;
import java.util.regex.Pattern;

/**
 * the activated state is ignored for purposes of equals() and hashcode().
 *
 * @author Bjoern Weissenfels, Jan Waller
 *
 * @since 1.6
 */
public class PatternEntry implements Serializable {
	private static final long serialVersionUID = 6225456449265043944L;

	private final Pattern pattern;
	private final String strPattern;
	private final boolean activated;

	/**
	 * Creates a new pattern entry using the given parameters.
	 *
	 * @param strPattern
	 *            The pattern string.
	 * @param activated
	 *            Determines the activated state.
	 *
	 * @throws InvalidPatternException
	 *             If the given pattern is invalid.
	 */
	public PatternEntry(final String strPattern, final boolean activated) throws InvalidPatternException {
		this.pattern = PatternParser.parseToPattern(strPattern);
		this.strPattern = strPattern;
		this.activated = activated;
	}

	/**
	 * Creates a new pattern entry using the given parameters.
	 *
	 * @param strPattern
	 *            The pattern string.
	 * @param activated
	 *            Determines the activated state.
	 * @param pattern
	 *            The pattern.
	 */
	public PatternEntry(final String strPattern, final Pattern pattern, final boolean activated) {
		this.pattern = pattern;
		this.strPattern = strPattern;
		this.activated = activated;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((this.pattern == null) ? 0 : this.pattern.hashCode()); // NOCS
		result = (prime * result) + ((this.strPattern == null) ? 0 : this.strPattern.hashCode()); // NOCS
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if ((obj == null) || (this.getClass() != obj.getClass())) {
			return false;
		}
		final PatternEntry other = (PatternEntry) obj;
		if (this.pattern == null) {
			if (other.pattern != null) {
				return false;
			}
		} else if (!this.pattern.equals(other.pattern)) {
			return false;
		}
		if (this.strPattern == null) {
			if (other.strPattern != null) {
				return false;
			}
		} else if (!this.strPattern.equals(other.strPattern)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "PatternEntry [pattern=" + this.pattern + ", strPattern=" + this.strPattern + ", activated=" + this.activated + "]";
	}

	public String getStrPattern() {
		return this.strPattern;
	}

	public Pattern getPattern() {
		return this.pattern;
	}

	public boolean isActivated() {
		return this.activated;
	}
}
