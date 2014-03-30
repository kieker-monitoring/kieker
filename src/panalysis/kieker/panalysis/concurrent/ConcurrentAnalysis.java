package kieker.panalysis.concurrent;

import java.util.LinkedList;
import java.util.List;

import kieker.panalysis.base.Analysis;
import kieker.panalysis.base.Stage;

public class ConcurrentAnalysis extends Analysis {

	private final List<Stage> stages = new LinkedList<Stage>();
	private final List<StageThread> threads = new LinkedList<StageThread>();

	@Override
	public void init() {
		// TODO add each stage to stages

		for (final Stage s : this.stages) {
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
