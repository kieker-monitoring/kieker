package kieker.analysisteetime.experimental.hotspotdetection;

import java.io.PrintStream;
import java.time.Duration;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import kieker.analysisteetime.model.analysismodel.trace.OperationCall;
import kieker.analysisteetime.model.analysismodel.trace.Trace;
import kieker.analysisteetime.trace.traversal.OperationCallVisitor;
import kieker.analysisteetime.trace.traversal.TraceTraverser;

import teetime.framework.AbstractConsumerStage;

/**
 *
 * This stage excepts traces at its input port and prints their operation calls to
 * a given {@link PrintStream}, which have the longest execution time without their children.
 *
 * The number of operation call that will be printed can be configured.
 * The default print stream is {@link System.out}.
 *
 * @author Sören Henning, Stephan Lenga
 *
 */
public class HotspotDetectionStage extends AbstractConsumerStage<Trace> {

	private static final int DEFAULT_MAX_OUTPUT = 10;
	private static final PrintStream DEFAULT_PRINT_STREAM = System.out;

	private final TraceTraverser traceTraverser = new TraceTraverser();
	private final Map<OperationCall, Duration> durationsWithoutChild = new HashMap<>(); // NOPMD (no concurrent access intended)
	private final int maxOutput;
	private final PrintStream printStream;

	public HotspotDetectionStage() {
		this.maxOutput = DEFAULT_MAX_OUTPUT;
		this.printStream = DEFAULT_PRINT_STREAM;
	}

	public HotspotDetectionStage(final int maxOutput, final PrintStream printStream) {
		this.maxOutput = maxOutput;
		this.printStream = printStream;
	}

	@Override
	protected void execute(final Trace trace) {
		final OperationCallVisitor visitor = new Visitor();
		this.traceTraverser.traverse(trace, visitor);
	}

	@Override
	public void onTerminating() throws Exception {

		this.printHotspots();

		super.onTerminating();
	}

	private void printHotspots() {
		final Map<OperationCall, Duration> sortedMap = HotspotDetectionStage.sortMapByValue(this.durationsWithoutChild);
		sortedMap.entrySet().stream().limit(this.maxOutput)
				.map(e -> e.getKey().getOperation().getComponent().getAssemblyComponent().getComponentType().getSignature() + " "
						+ e.getKey().getOperation().getAssemblyOperation().getOperationType().getSignature() + ": " + e.getValue().toString())
				.forEach(this.printStream::println);
	}

	private class Visitor extends OperationCallVisitor {

		@Override
		public void visit(final OperationCall operationCall) {
			final Duration duration = operationCall.getDuration();
			final Duration durationsOfChildren = operationCall.getChildren().stream().map(c -> c.getDuration()).reduce(Duration.ZERO, (r, d) -> r.plus(d));
			final Duration durationWithoutChildren = duration.minus(durationsOfChildren);

			HotspotDetectionStage.this.durationsWithoutChild.put(operationCall, durationWithoutChildren);
		}

	}

	// BETTER Put to some kind of MapUtil class
	public static <K, V extends Comparable<? super V>> Map<K, V> sortMapByValue(final Map<K, V> map) {
		final List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
			@Override
			public int compare(final Map.Entry<K, V> o1, final Map.Entry<K, V> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});

		final Map<K, V> result = new LinkedHashMap<>(); // NOPMD (no concurrent access intended)
		for (final Map.Entry<K, V> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}

}
