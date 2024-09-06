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
package kieker.tools.settings.splitters;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import com.beust.jcommander.converters.IParameterSplitter;

/**
 * Split a parameter value using the path separator symbol. This varies based on the operating system.
 *
 * @author Reiner Jung
 * @since 2.0.0
 */
public class PathParameterSplitter implements IParameterSplitter {

	@Override
	public List<String> split(final String value) {
		return Arrays.asList(value.split(File.pathSeparator));
	}

}
