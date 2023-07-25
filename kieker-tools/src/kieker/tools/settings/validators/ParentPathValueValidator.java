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

import java.nio.file.Path;

import com.beust.jcommander.IValueValidator;
import com.beust.jcommander.ParameterException;

/**
 * Evaluate whether the parent directory of a given path exists.
 *
 * @author Reiner Jung
 * @since 2.0.0
 */
public class ParentPathValueValidator implements IValueValidator<Path> { // NOPMD, NOCS

	@Override
	public void validate(final String name, final Path path) throws ParameterException { // NOPMD jcommander
		if (!path.toAbsolutePath().getParent().toFile().exists()) {
			throw new ParameterException(String.format("Path %s does not exist", path.toAbsolutePath().getParent()));
		}
	}

}
