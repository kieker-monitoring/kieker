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

package kieker.analysisteetime.plugin.reader.filesystem.format.text.file;

import java.util.Map;

import kieker.common.util.filesystem.FSUtil;

import teetime.framework.AbstractConsumerStage;

/**
 * @author Christian Wulf
 *
 * @since 1.10
 *
 * @deprecated since 1.15 removed 1.16
 */
@Deprecated
public class TextLine2MappingRegistryFilter extends AbstractConsumerStage<String> {

	private final Map<Integer, String> stringRegistry;

	public TextLine2MappingRegistryFilter(final Map<Integer, String> stringRegistry) {
		this.stringRegistry = stringRegistry;
	}

	@Override
	protected void execute(final String textLine) {
		final int split = textLine.indexOf('=');
		if (split == -1) {
			this.logger.error("Failed to find character '=' in line: {" + textLine + "}. It must consist of a ID=VALUE pair.");
			return;
		}
		final String key = textLine.substring(0, split);
		// BETTER execute split instead of checking it before with multiple string operations
		// the leading $ is optional
		final Integer id;
		try {
			id = Integer.valueOf((key.charAt(0) == '$') ? key.substring(1) : key); // NOCS
		} catch (final NumberFormatException ex) {
			this.logger.error("Error reading mapping file, id must be integer", ex);
			return; // continue on errors
		}
		final String value = FSUtil.decodeNewline(textLine.substring(split + 1));
		final String prevVal = this.stringRegistry.put(id, value);
		if (prevVal != null) {
			this.logger.error("Found additional entry for id='" + id + "', old value was '" + prevVal + "' new value is '" + value + "'");
			return;
		}
	}

}
