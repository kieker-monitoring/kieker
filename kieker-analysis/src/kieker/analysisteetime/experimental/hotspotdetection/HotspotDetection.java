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

	public static void main(final String[] args) {

		// TODO Temp
		final String path = "C:/Users/Soeren/Desktop/jedit-records/kieker-20170115-163405515-UTC-Leonard-KIEKER";
		final File importDirectory = new File(path);

		final HotspotDetectionConfiguration configuration = new HotspotDetectionConfiguration(importDirectory);
		final Execution<HotspotDetectionConfiguration> analysis = new Execution<>(configuration);
		analysis.executeBlocking();

	}

}
