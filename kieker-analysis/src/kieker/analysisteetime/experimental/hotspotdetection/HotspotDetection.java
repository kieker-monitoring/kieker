/**
 *
 */
package kieker.analysisteetime.experimental.hotspotdetection;

import java.io.File;

import teetime.framework.Execution;

/**
 * This class executes a {@link HotspotDetectionConfiguration}.
 *
 * @author Sören Henning, Stephan Lenga
 *
 */
public final class HotspotDetection {

	private final Execution<HotspotDetectionConfiguration> execution;

	public HotspotDetection(final File importDirectory) {
		final HotspotDetectionConfiguration configuration = new HotspotDetectionConfiguration(importDirectory);
		this.execution = new Execution<>(configuration);
	}

	public void run() {
		this.execution.executeBlocking();
	}

	public static void main(final String[] args) {
		if (args.length < 1) {
			throw new IllegalArgumentException("No input directory specified.");
		}

		final File path = new File(args[0]);
		new HotspotDetection(path).run();
	}

}
