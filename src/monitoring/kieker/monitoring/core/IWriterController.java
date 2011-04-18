package kieker.monitoring.core;

import kieker.common.record.IMonitoringRecordReceiver;
import kieker.monitoring.timer.ITimeSource;
import kieker.monitoring.writer.IMonitoringWriter;

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
 * The methods must not throw any exceptions but indicate problems via its
 * respective return value.
 * 
 * @author Andre van Hoorn, Jan Waller, Robert von Massow
 */
public interface IWriterController extends IMonitoringRecordReceiver,
		IStatefulSubController {

	/**
	 * Returns the configured monitoring writer.
	 * 
	 * @return
	 */
	public IMonitoringWriter getMonitoringWriter();

	/**
	 * Shows how many inserts have been performed since last restart of the
	 * execution environment.
	 * 
	 * @return long
	 */
	public abstract long getNumberOfInserts();

	/**
	 * Returns the ITimeSource used in this controller.
	 * 
	 * @return ITimeSource
	 */
	public abstract ITimeSource getTimeSource();

	/**
	 * Returns the IController instance which is actually a configuration/state.
	 * 
	 * @return the config of the controller
	 */
	abstract public IMonitoringControllerState getControllerConfig();

	public void enableRealtimeMode();

	public void enableReplayMode();

	public boolean isRealtimeMode();

	public boolean isReplayMode();
}
