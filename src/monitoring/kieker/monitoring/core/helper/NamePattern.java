/***************************************************************************
 * Copyright 2012 Kieker Project (http://kieker-monitoring.net)
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

package kieker.monitoring.core.helper;

/**
 * Based upon org.aspectj.weaver.patterns.NamePattern.
 * Contributors: Palo Alto Research Center
 * 
 * @author Björn Weißenfels
 */

public class NamePattern {
	char[] pattern;
	int starCount = 0;
	private int hashcode = -1;
	private boolean include;

	public static final NamePattern ELLIPSIS = new NamePattern("");
	public static final NamePattern ANY = new NamePattern("*");

	public NamePattern(final String name) {
		this(name.toCharArray());
	}

	public NamePattern(final String name, final boolean include) {
		this(name.toCharArray());
		this.include = include;
	}

	public NamePattern(final char[] pattern) {
		this.pattern = pattern;

		for (final char element : pattern) {
			if (element == '*') {
				this.starCount++;
			}
		}
		this.hashcode = new String(pattern).hashCode();
	}

	public boolean matches(final char[] a2) {
		final char[] a1 = this.pattern;
		final int len1 = a1.length;
		final int len2 = a2.length;
		if (this.starCount == 0) {
			if (len1 != len2) {
				return false;
			}
			for (int i = 0; i < len1; i++) {
				if (a1[i] != a2[i]) {
					return false;
				}
			}
			return true;
		} else if (this.starCount == 1) {
			// just '*' matches anything
			if (len1 == 1) {
				return true;
			}
			if (len1 > (len2 + 1)) {
				return false;
			}
			int i2 = 0;
			for (int i1 = 0; i1 < len1; i1++) {
				final char c1 = a1[i1];
				if (c1 == '*') {
					i2 = len2 - (len1 - (i1 + 1));
				} else if (c1 != a2[i2++]) {
					return false;
				}
			}
			return true;
		} else {
			// System.err.print("match(\"" + pattern + "\", \"" + target + "\") -> ");
			final boolean b = NamePattern.outOfStar(a1, a2, 0, 0, len1 - this.starCount, len2, this.starCount);
			// System.err.println(b);
			return b;
		}
	}

	private static boolean outOfStar(final char[] pattern, final char[] target, int pi, int ti, int pLeft, int tLeft,
			final int starsLeft) {
		if (pLeft > tLeft) {
			return false;
		}
		while (true) {
			// invariant: if (tLeft > 0) then (ti < target.length && pi < pattern.length)
			if (tLeft == 0) {
				return true;
			}
			if (pLeft == 0) {
				return (starsLeft > 0);
			}
			if (pattern[pi] == '*') {
				return NamePattern.inStar(pattern, target, pi + 1, ti, pLeft, tLeft, starsLeft - 1);
			}
			if (target[ti] != pattern[pi]) {
				return false;
			}
			pi++;
			ti++;
			pLeft--;
			tLeft--;
		}
	}

	private static boolean inStar(final char[] pattern, final char[] target, int pi, int ti, final int pLeft, int tLeft,
			int starsLeft) {
		// invariant: pLeft > 0, so we know we'll run out of stars and find a real char in pattern
		char patternChar = pattern[pi];
		while (patternChar == '*') {
			starsLeft--;
			patternChar = pattern[++pi];
		}
		while (true) {
			// invariant: if (tLeft > 0) then (ti < target.length)
			if (pLeft > tLeft) {
				return false;
			}
			if (target[ti] == patternChar) {
				if (NamePattern.outOfStar(pattern, target, pi + 1, ti + 1, pLeft - 1, tLeft - 1, starsLeft)) {
					return true;
				}
			}
			ti++;
			tLeft--;
		}
	}

	public boolean matches(final String other) {
		if ((this.starCount == 1) && (this.pattern.length == 1)) {
			// optimize for wildcard
			return true;
		}
		return this.matches(other.toCharArray());
	}

	@Override
	public String toString() {
		return new String(this.pattern);
	}

	@Override
	public boolean equals(final Object other) {
		if (other instanceof NamePattern) {
			final NamePattern otherPat = (NamePattern) other;
			if (otherPat.starCount != this.starCount) {
				return false;
			}
			if (otherPat.pattern.length != this.pattern.length) {
				return false;
			}
			for (int i = 0; i < this.pattern.length; i++) {
				if (this.pattern[i] != otherPat.pattern[i]) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * Method maybeGetSimpleName.
	 * 
	 * @return String
	 */
	public String maybeGetSimpleName() {
		if ((this.starCount == 0) && (this.pattern.length > 0)) {
			return new String(this.pattern);
		}
		return null;
	}

	/**
	 * Method isAny.
	 * 
	 * @return boolean
	 */
	public boolean isAny() {
		return (this.starCount == 1) && (this.pattern.length == 1);
	}

	public boolean isInclude() {
		return this.include;
	}

	public void setInclude(final boolean include) {
		this.include = include;
	}

}
