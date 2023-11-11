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
package kieker.analysis.generic.source;

import java.nio.file.Path;
import java.util.List;

import teetime.framework.AbstractProducerStage;

/**
 * Send out all directories specified as paths or files.
 *
 * @author Reiner Jung
 * @since 1.3.0
 */
public class DirectoryProducer extends AbstractProducerStage<Path> {

	private final Path[] paths;

	/**
	 * Create a directory producer using paths as input.
	 *
	 * @param paths
	 *            paths to be converted to files and checked whether they are directories
	 */
	public DirectoryProducer(final Path... paths) { // NOPMD
		this.paths = paths;
	}

	public DirectoryProducer(final List<Path> paths) {
		this.paths = paths.toArray(new Path[paths.size()]);
	}

	@Override
	protected void execute() throws Exception {
		for (final Path path : this.paths) {
			if (path.toFile().isDirectory()) {
				this.outputPort.send(path);
			} else {
				this.logger.warn("{} is not a directory", path.toString());
			}
		}
		this.workCompleted();
	}

}
