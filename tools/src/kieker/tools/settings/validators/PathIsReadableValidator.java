/***************************************************************************
 * Copyright (C) 2023 Kieker Project (https://kieker-monitoring.net)
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
package kieker.tools.settings.validators;

import java.io.File;
import java.nio.file.Paths;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

/**
 * Check whether the path is readable.
 *
 * @author Reiner Jung
 * @since 1.3.0
 */
public class PathIsReadableValidator implements IParameterValidator {

	@Override
	public void validate(final String name, final String value) throws ParameterException { // NOPMD
		final File file = Paths.get(value).toFile();
		if (!file.canRead()) {
			throw new ParameterException(String.format("Parameter %s: path %s is not readable.", name, value));
		}
	}

}
