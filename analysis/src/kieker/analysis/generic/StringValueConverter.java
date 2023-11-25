/***************************************************************************
 * Copyright (C) 2021 OceanDSL (https://oceandsl.uni-kiel.de)
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
package kieker.analysis.generic;

import java.util.Locale;

/**
 * Select and convert a string for the @{link MapFileReader}.
 *
 * @author Reiner Jung
 * @since 1.1
 */
public class StringValueConverter implements IValueConverter<String> {

	private final boolean caseInsensitive;
	private final int column;

	public StringValueConverter(final boolean caseInsenstive, final int column) {
		this.caseInsensitive = caseInsenstive;
		this.column = column;
	}

	@Override
	public String getColumnValue(final String[] input) {
		return this.caseInsensitive ? input[this.column].toLowerCase(Locale.ROOT) : input[this.column]; // NOCS
	}
}
