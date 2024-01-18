/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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
package kieker.analysis.generic.sink;

import java.io.IOException;
import java.nio.file.Path;

import kieker.model.repository.SystemModelRepository;

import teetime.framework.AbstractConsumerStage;

/**
 * Print out the system model in an HTML file.
 *
 * @author Reiner Jung
 * @since 1.15
 */
public class SystemModel2FileSink extends AbstractConsumerStage<Object> {

	private final SystemModelRepository repository;
	private final Path outputFilePath;

	public SystemModel2FileSink(final SystemModelRepository repository, final Path outputFilePath) {
		this.repository = repository;
		this.outputFilePath = outputFilePath;
	}

	@Override
	protected void execute(final Object element) throws Exception {
		/**
		 * Used to keep track of whether an error occurred, regardless
		 * of whether before or during termination.
		 */

		try {
			this.repository.saveSystemToHTMLFile(this.outputFilePath);
		} catch (final IOException e) {
			this.logger.error("Failed to save system model to file {}: {}", this.outputFilePath.toString(), e.getLocalizedMessage());
		}
	}

}
