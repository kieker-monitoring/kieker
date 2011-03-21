package kieker.monitoring.core;

import kieker.monitoring.core.configuration.Configuration;

/*
 * ==================LICENCE=========================
 * Copyright 2006-2011 Kieker Project
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ==================================================
 */
/**
 * @author Andre van Hoorn, Jan Waller
 */
abstract class ReplayController extends Controller implements IReplayController {

	private volatile boolean replayMode = false;

	protected ReplayController(Configuration configuration) {
		super(configuration);
		if (isMonitoringTerminated()){
			return;
		}
		this.replayMode = configuration.getBooleanProperty(Configuration.REPLAY_MODE);
	}

	@Override
	public String getState() {
		final StringBuilder sb = new StringBuilder();
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
