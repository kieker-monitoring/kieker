/***************************************************************************
 * Copyright 2021 Kieker Project (http://kieker-monitoring.net)
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
package kieker.analysis.signature;

/**
 * Signature cleanup facility.
 *
 * @author Reiner Jung
 * @since 1.15
 */
public abstract class AbstractSignatureCleaner {

	protected boolean caseInsensitive;

	public AbstractSignatureCleaner(final boolean caseInsensitive) {
		this.caseInsensitive = caseInsensitive;
	}

	protected String removeTrailingUnderscore(final String string) {
		if (string.endsWith("_")) {
			return string.substring(0, string.length() - 1);
		} else {
			return string;
		}
	}

	protected String convertToLowerCase(final String string) {
		return this.caseInsensitive ? string.toLowerCase() : string; // NOCS NOPMD
	}

	public abstract String processComponentSignature(final String signature);
}
