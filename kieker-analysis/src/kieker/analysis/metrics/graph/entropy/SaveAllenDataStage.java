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
package kieker.analysis.metrics.graph.entropy;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Map.Entry;

import org.mosim.refactorlizar.architecture.evaluation.codemetrics.CodeMetric;

import teetime.framework.AbstractConsumerStage;

/**
 * @author Reiner Jung
 * @since 1.1
 */
public class SaveAllenDataStage extends AbstractConsumerStage<Map<Class<? extends CodeMetric>, CodeMetric>> {

	private static final String ALLEN_METRIC_FILENAME = "model-complexity.csv";
	private final Path outputDirectory;

	public SaveAllenDataStage(final Path outputDirectory) {
		this.outputDirectory = outputDirectory;
	}

	@Override
	protected void execute(final Map<Class<? extends CodeMetric>, CodeMetric> element) throws Exception {
		try (final BufferedWriter writer = Files
				.newBufferedWriter(this.outputDirectory.resolve(SaveAllenDataStage.ALLEN_METRIC_FILENAME))) {
			for (final Entry<Class<? extends CodeMetric>, CodeMetric> value : element.entrySet()) {
				writer.write(String.format("%s, %s\n", value.getKey().getCanonicalName(), value.getValue().getValue()));
			}
		}
	}

}
