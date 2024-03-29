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
package kieker.analysis.util.debug.hotspotdetection;

import java.io.PrintStream;
import java.time.Duration;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import kieker.analysis.architecture.trace.traversal.IOperationCallVisitor;
import kieker.analysis.architecture.trace.traversal.TraceTraverser;
import kieker.model.analysismodel.trace.OperationCall;
import kieker.model.analysismodel.trace.Trace;

import teetime.framework.AbstractConsumerStage;

/**
 * This stage excepts traces at its input port and prints their operation calls
 * to a given {@link PrintStream}, which have the longest execution time without
 * their children.
 *
 * The number of operation call that will be printed can be configured. The
 * default print stream is System.out.
 *
 * @author Sören Henning, Stephan Lenga
 *
 * @since 1.14
 */
public class HotspotDetectionStage extends AbstractConsumerStage<Trace> {

	private static final int DEFAULT_MAX_OUTPUT = 10;
	private static final PrintStream DEFAULT_PRINT_STREAM = System.out;

	private final Map<OperationCall, Duration> durationsWithoutChild = new HashMap<>(); // NOPMD (no concurrent access)

	private final TraceTraverser traceTraverser = new TraceTraverser();
	private final int maxOutput;
	private final PrintStream printStream;

	public HotspotDetectionStage() {
		this.maxOutput = HotspotDetectionStage.DEFAULT_MAX_OUTPUT;
		this.printStream = HotspotDetectionStage.DEFAULT_PRINT_STREAM;
	}

	public HotspotDetectionStage(final int maxOutput, final PrintStream printStream) {
		this.maxOutput = maxOutput;
		this.printStream = printStream;
	}

	@Override
	protected void execute(final Trace trace) {
		final IOperationCallVisitor visitor = new DurationCollector(this.durationsWithoutChild);
		this.traceTraverser.traverse(trace, visitor);
	}

	@Override
	protected void onTerminating() {
		this.printHotspots();

		super.onTerminating();
	}

	private void printHotspots() {
		final Map<OperationCall, Duration> sortedMap = HotspotDetectionStage.sortMapByValue(this.durationsWithoutChild);
		sortedMap.entrySet().stream().limit(this.maxOutput).map(
				e -> e.getKey().getOperation().getComponent().getAssemblyComponent().getComponentType().getSignature()
						+ " " + e.getKey().getOperation().getAssemblyOperation().getOperationType().getSignature()
						+ ": " + e.getValue().toString())
				.forEach(this.printStream::println);
	}

	// BETTER Put to some kind of MapUtil class
	public static <K, V extends Comparable<? super V>> Map<K, V> sortMapByValue(final Map<K, V> map) {
		final List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
			@Override
			public int compare(final Map.Entry<K, V> o1, final Map.Entry<K, V> o2) {
				return o2.getValue().compareTo(o1.getValue());
			}
		});

		final Map<K, V> result = new LinkedHashMap<>(); // NOPMD (no concurrent access intended)
		for (final Map.Entry<K, V> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}

	/**
	 * Collects the durations of operation calls without children.
	 */
	private static class DurationCollector implements IOperationCallVisitor {

		@SuppressWarnings("hiding")
		private final Map<OperationCall, Duration> durationsWithoutChild;

		/* default */
		DurationCollector(final Map<OperationCall, Duration> durationsWithoutChild) {
			this.durationsWithoutChild = durationsWithoutChild;
		}

		@Override
		public void visit(final OperationCall operationCall) {
			final Duration duration = operationCall.getDuration();
			final Duration durationsOfChildren = operationCall.getChildren().stream().map(c -> c.getDuration())
					.reduce(Duration.ZERO, (r, d) -> r.plus(d));
			final Duration durationWithoutChildren = duration.minus(durationsOfChildren);

			this.durationsWithoutChild.put(operationCall, durationWithoutChildren);
		}

	}

}
