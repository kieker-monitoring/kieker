/***************************************************************************
 * Copyright (C) 2022 OceanDSL (https://oceandsl.uni-kiel.de)
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

import java.lang.invoke.WrongMethodTypeException;
import java.util.Locale;

import com.beust.jcommander.IStringConverter;

/**
 *
 * @author Reiner Jung
 * @since 1.3.0
 *
 */
public class EModuleModeConverter implements IStringConverter<EModuleMode> {

	@Override
	public EModuleMode convert(final String value) {
		for (final EModuleMode element : EModuleMode.values()) {
			if (element.name().replace("_", "-").equalsIgnoreCase(value)) {
				return element;
			}
		}

		String options = "";
		String separator = "";
		for (final EModuleMode element : EModuleMode.values()) {
			options += separator + element.name().replace("_", "-").toLowerCase(Locale.getDefault());
			separator = ", ";
		}
		throw new WrongMethodTypeException(
				String.format("%s is not a valid mode. Allowed modes are: %s", value, options));
	}

}
