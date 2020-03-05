/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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

package kieker.common.util.dataformat;

/**
 * Format identifiers used by Kieker. 
 * @author Holger Knoche
 * @since 1.13
 */
public enum FormatIdentifier {
	/** Identifier for the container format. Reads "KIKA" in ASCII encoding. */
	CONTAINER_FORMAT(0x4B494B41),
	/** Identifier for the default binary encoding. Reads "DFLT" in ASCII encoding. */
	DEFAULT_BINARY_FORMAT(0x44464C54);

	private final int identifierValue;
	
	private FormatIdentifier(final int identifierValue) {
		this.identifierValue = identifierValue;
	}
	
	public int getIdentifierValue() {
		return this.identifierValue;
	}
	
}
