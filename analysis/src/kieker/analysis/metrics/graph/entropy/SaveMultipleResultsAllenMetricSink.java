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
package kieker.analysis.metrics.graph.entropy;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import org.mosim.refactorlizar.architecture.evaluation.codemetrics.CodeMetric;
import org.mosim.refactorlizar.architecture.evaluation.codemetrics.LinesOfCode;

import teetime.framework.AbstractConsumerStage;

/**
 * Save multiple Allen metric result sets into a file. The file is closed the completion of the
 * analysis.
 *
 * @author Reiner Jung
 * @since 2.0.0
 */
public class SaveMultipleResultsAllenMetricSink
		extends AbstractConsumerStage<Map<Class<? extends CodeMetric>, CodeMetric>> {

	private final BufferedWriter writer;
	private int counter;
	private final Class<? extends CodeMetric>[] metricClasses;
	private final String valueSeparator;
	private final String newline;

	/**
	 * Create a Allen metric save routine.
	 *
	 * @param outputPath
	 *            path of the output file
	 * @param newline
	 *            newline string
	 * @param valueSeparator
	 *            value separator string
	 * @param metrics
	 *            set of metrics
	 * @throws IOException
	 *             on io error
	 */
	@SafeVarargs
	public SaveMultipleResultsAllenMetricSink(final Path outputPath, final String newline, final String valueSeparator,
			final Class<? extends CodeMetric>... metrics) throws IOException { // NOPMD
		this.newline = newline;
		this.valueSeparator = valueSeparator;
		this.writer = Files.newBufferedWriter(outputPath);
		this.metricClasses = metrics; // NOPMD its ok to save an array
		for (int i = 0; i < metrics.length; i++) {
			if (i > 0) {
				this.writer.write(valueSeparator);
			}
			this.writer.write(metrics[i].getSimpleName());
		}
		this.writer.write(newline);
	}

	@Override
	protected void execute(final Map<Class<? extends CodeMetric>, CodeMetric> valueMap) throws IOException {
		this.logger.info("Output line {}", ++this.counter);
		boolean separator = false;
		for (final Class<? extends CodeMetric> metricClass : this.metricClasses) {
			if (separator) {
				this.writer.write(this.valueSeparator);
			} else {
				separator = true;
			}
			if (metricClass.equals(LinesOfCode.class)) {
				this.findElementInteger(LinesOfCode.class, valueMap);
			} else {
				this.findElement(metricClass, valueMap);
			}
		}
		this.writer.write(this.newline);
		this.writer.flush();
	}

	private void findElement(final Class<? extends CodeMetric> clazz,
			final Map<Class<? extends CodeMetric>, CodeMetric> metrics) throws IOException {
		final CodeMetric metric = metrics.get(clazz);
		if (metric != null) {
			this.writer.write(String.format("%f", metric.getValue()));
		}
	}

	private void findElementInteger(final Class<? extends CodeMetric> clazz,
			final Map<Class<? extends CodeMetric>, CodeMetric> metrics) throws IOException {
		final CodeMetric metric = metrics.get(clazz);
		if (metric != null) {
			this.writer.write(String.format("%d", (int) metric.getValue()));
		}
	}

	@Override
	protected void onTerminating() {
		try {
			this.writer.close();
		} catch (final IOException e) {
			this.logger.error("Could not close writer.");
		}
		super.onTerminating();
	}

}
