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

/**
 * In Fortran the compiler might suffix function names in the object file with an _. To remove this
 * and bring the name in alignment with the source code again, the _ is removed by the @{link
 * IValueConverter}.
 *
 * @author Reiner Jung
 * @since 1.1
 *
 */
public class FunctionNameValueConverter extends StringValueConverter {

	public FunctionNameValueConverter(final boolean caseInsenstive, final int column) {
		super(caseInsenstive, column);
	}

	@Override
	public String getColumnValue(final String[] input) {
		final String value = super.getColumnValue(input);
		if (value.endsWith("_")) {
			return value.substring(0, value.length() - 1);
		} else {
			return value;
		}
	}
}
