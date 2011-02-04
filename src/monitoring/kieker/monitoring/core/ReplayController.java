package kieker.monitoring.core;

import kieker.monitoring.core.configuration.Configuration;

public class ReplayController extends Controller implements IReplayController {

	private volatile boolean replayMode = true;

	protected ReplayController(Configuration configuration) {
		super(configuration);
		this.replayMode = configuration.getBooleanProperty(Configuration.REPLAY_MODE);
	}

	@Override
	public String getState() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.getState());
		sb.append("; ReplayMode: '");
		sb.append(replayMode);
		sb.append("'");
		return sb.toString();
	}

	@Override
	public final void enableRealtimeMode() {
		replayMode = false;
	}

	@Override
	public final void enableReplayMode() {
		replayMode = true;
	}

	@Override
	public final boolean isRealtimeMode() {
		return !replayMode;
	}
	
	@Override
	public final boolean isReplayMode() {
		return replayMode;
	}
}
