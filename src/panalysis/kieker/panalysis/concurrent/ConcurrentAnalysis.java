/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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

package kieker.panalysis.concurrent;

import java.util.LinkedList;
import java.util.List;

import kieker.panalysis.base.Analysis;
import kieker.panalysis.base.IStage;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class ConcurrentAnalysis extends Analysis {

	private final List<IStage> stages = new LinkedList<IStage>();
	private final List<StageThread> threads = new LinkedList<StageThread>();

	@Override
	public void init() {
		// TODO add each stage to stages

		for (final IStage s : this.stages) {
			final StageThread stageThread = new StageThread(s);
			this.threads.add(stageThread);
		}
	}

	@Override
	public void start() {
		for (final StageThread stageThread : this.threads) {
			stageThread.start();
		}

		while (true) {
			try {
				Thread.sleep(1000);
			} catch (final InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			long maxDuration = -1;
			StageThread maxThread;
			for (final StageThread stageThread : this.threads) {
				final long duration = stageThread.getDuration();
				if (duration > maxDuration) {
					maxDuration = duration;
					maxThread = stageThread;
				}
			}

		}
	}
}
