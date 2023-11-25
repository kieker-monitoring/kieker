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
package kieker.analysis.code;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kieker.analysis.code.data.CallerCalleeEntry;

import teetime.framework.OutputPort;
import teetime.stage.basic.AbstractFilter;

/**
 * This stage receives an {@link CallerCalleeEntry} object and checks whether the file path for
 * caller and callee operation are specified. In case they are missing, the stage sets them based on
 * its operation to file lookup table. In case the operation is not listed, it collects all
 * operations which do not have a file name.
 *
 * <ul>
 * <li>outputPort sends out {@link CallerCalleeEntry} objects with all 4 values set.</li>
 * <li>missingOperationOutputPort sends out each newly found operation which does not have a
 * associated file path.</li>
 * </ul>
 *
 * @author Reiner Jung
 * @since 1.1
 */
// TODO adapt to csveed
public class CallerCalleeFixPathStage extends AbstractFilter<CallerCalleeEntry> {

	private final Map<String, String> operationToFileMap = new HashMap<>();
	private final OutputPort<String> missingOperationOutputPort = this.createOutputPort(String.class);
	private final List<String> missingOperationNames = new ArrayList<>();

	public CallerCalleeFixPathStage(final List<Path> functionMapPaths, final String splitSymbol) throws IOException {
		for (final Path functionMapPath : functionMapPaths) {
			try (BufferedReader reader = Files.newBufferedReader(functionMapPath)) {
				String line;
				reader.readLine(); // skip header
				while ((line = reader.readLine()) != null) {
					final String[] values = line.split(splitSymbol);
					if (values.length >= 2) {
						this.operationToFileMap.put(values[1].trim(), values[0].trim());
					}
				}
			}
		}
	}

	@Override
	protected void execute(final CallerCalleeEntry element) throws Exception {
		if ("".equals(element.getSourcePath())) {
			element.setSourcePath(this.findPath(element.getCaller()));
		}
		if ("".equals(element.getTargetPath())) {
			element.setTargetPath(this.findPath(element.getCallee()));
		}

		this.outputPort.send(element);
	}

	private String findPath(final String functionName) {
		final String path = this.operationToFileMap.get(functionName);
		if (path == null) {
			if (!this.missingOperationNames.contains(functionName)) {
				this.logger.warn("Missing file entry for operation: {}", functionName);
				this.missingOperationNames.add(functionName);
				this.missingOperationOutputPort.send(functionName);
			}
			return "";
		} else {
			return path;
		}
	}

	public OutputPort<String> getMissingOperationOutputPort() {
		return this.missingOperationOutputPort;
	}

}
