/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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

import java.io.File;

import teetime.framework.Execution;

/**
 * This class executes a {@link HotspotDetectionConfiguration}.
 *
 * @author Sören Henning, Stephan Lenga
 *
 * @since 1.14
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
