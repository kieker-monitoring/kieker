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
package kieker.tools.settings;

import com.beust.jcommander.converters.BaseConverter;

/**
 * Convert string to short value.
 *
 * @author Reiner Jung
 * @since 2.0.0
 */
public class CharConverter extends BaseConverter<Character> {

	public CharConverter(final String arg0) {
		super(arg0);
	}

	@Override
	public Character convert(final String arg0) {
		if (arg0.length() > 0) {
			return arg0.charAt(0);
		} else {
			return null;
		}
	}

}
