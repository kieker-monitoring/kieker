/**
 *
 */
package kieker.analysis.sink;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.util.Map;
import java.util.Map.Entry;

import kieker.model.system.model.ExecutionTrace;

import teetime.framework.AbstractConsumerStage;

/**
 * Write trace equivalence class map to a file.
 *
 * @author Reiner Jung
 * @since 1.15
 */
public class EquivalenceClassWriter extends AbstractConsumerStage<Map<ExecutionTrace, Integer>> {

	private static final String ENCODING = "UTF-8";

	private final File outputFile;

	public EquivalenceClassWriter(final File outputFile) {
		this.outputFile = outputFile;
	}

	@Override
	protected void onTerminating() {
		this.logger.debug("Terminatiing {}", this.getClass().getCanonicalName());
		super.onTerminating();
	}

	@Override
	protected void execute(final Map<ExecutionTrace, Integer> element) throws Exception {
		PrintStream ps = null;
		try {
			ps = new PrintStream(Files.newOutputStream(this.outputFile.toPath()), false, ENCODING);
			int numClasses = 0;
			for (final Entry<ExecutionTrace, Integer> e : element.entrySet()) {
				final ExecutionTrace t = e.getKey();
				ps.println("Class " + numClasses++ + " ; cardinality: " + e.getValue() + "; # executions: " + t.getLength() + "; representative: " + t.getTraceId()
						+ "; max. stack depth: " + t.getMaxEss());
			}
			this.logger.debug("");
			this.logger.debug("#");
			this.logger.debug("# Plugin: Trace equivalence report");
			this.logger.debug("Wrote {} equivalence class{} to file '{}'", numClasses, (numClasses > 1 ? "es" : ""), this.outputFile.getCanonicalFile()); // NOCS
		} catch (final FileNotFoundException e) {
			this.logger.error("File not found", e);
		} finally {
			if (ps != null) {
				ps.close();
			}
		}
	}

}
