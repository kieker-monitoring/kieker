/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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
import java.io.IOException;

import com.beust.jcommander.IValueValidator;
import com.beust.jcommander.ParameterException;

/**
 * Validate whether the given directory file handle exists and is a directory.
 *
 * @author Reiner Jung
 * @since 2.0.0
 */
public class DirectoryReadValidator implements IValueValidator<File> {

	@Override
	public void validate(final String name, final File value) throws ParameterException { // NOPMD jcommander
		if (value == null) {
			throw new ParameterException(String.format("%s path not specified.", name));
		}
		try {
			if (!value.exists()) {
				throw new ParameterException(String.format("%s path %s does not exist.", name,
						value.getCanonicalPath()));
			}
			if (!value.isDirectory()) {
				throw new ParameterException(String.format("%s path %s is not a directory.", name,
						value.getCanonicalPath()));
			}
		} catch (final IOException e) {
			throw new ParameterException(String.format("%s path %s cannot be checked. Cause: %s", name, value, // NOPMD
					e.getLocalizedMessage()));
		}
	}

}
