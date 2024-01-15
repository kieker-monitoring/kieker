/***************************************************************************
 * Copyright 2021 Kieker Project (https://kieker-monitoring.net)
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
package kieker.tools.dar.signature.processor;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.analysis.architecture.recovery.signature.AbstractSignatureProcessor;
import kieker.analysis.code.CodeUtils;
import kieker.analysis.generic.MapFileReader;
import kieker.analysis.generic.StringValueConverter;

/**
 * @author Reiner Jung
 * @since 2.0.0
 */
public class MapBasedSignatureProcessor extends AbstractSignatureProcessor {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final Map<String, String> componentMap = new HashMap<>();

	public MapBasedSignatureProcessor(final List<Path> componentMapFiles, final boolean caseInsensitive,
			final String separator) throws IOException {
		super(caseInsensitive);
		for (final Path componentMapFile : componentMapFiles) {
			this.logger.info("Reading map file {}", componentMapFile.toString());

			final MapFileReader<String, String> mapFileReader = new MapFileReader<>(componentMapFile, separator,
					this.componentMap, new StringValueConverter(caseInsensitive, 1),
					new StringValueConverter(caseInsensitive, 0));
			mapFileReader.read();
		}
	}

	@Override
	public void processSignatures(final String componentSignature, final String operationSignature) {
		if (CodeUtils.NO_FILE.equals(componentSignature)) {
			this.componentSignature = componentSignature;
		} else {
			final Path path = Paths.get(componentSignature);
			final String filename = this.convertToLowerCase(path.getName(path.getNameCount() - 1).toString());
			final String result = this.componentMap.get(filename);
			if (result != null) {
				this.componentSignature = result;
			} else {
				this.logger.warn("File '{}' has no component mapping. Signature '{}'", filename, componentSignature);
				this.componentSignature = "??" + componentSignature.toLowerCase(Locale.ROOT);
			}
		}

		this.operationSignature = this.convertToLowerCase(this.removeTrailingUnderscore(operationSignature));
	}

}
