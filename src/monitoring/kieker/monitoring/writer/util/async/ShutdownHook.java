package kieker.monitoring.writer.util.async;

import java.util.Vector;

import kieker.monitoring.core.MonitoringController;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/*
 * ==================LICENCE========================= 
 * Copyright 2006-2010 Kieker
 * Project
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * ==================================================
 */

/**
 * This class ensures that virtual machine shutdown (e.g., cause by a
 * System.exit(int)) is delayed until all monitoring data is written. This is
 * important for the asynchronous writers for the files system and database,
 * since these store data with a small delay and data would be lost when
 * System.exit is not delayed.
 * 
 * When the system shutdown is initiated, the termination of the Virtual Machine
 * is delayed until all registered worker queues are empty.
 * 
 * @author Matthias Rohr
 */
public class ShutdownHook extends Thread {

	private static final Log log = LogFactory.getLog(ShutdownHook.class);

	private final MonitoringController ctrl;

	/**
	 * Must not be used for construction
	 */
	@SuppressWarnings("unused")
	private ShutdownHook() {
		this.ctrl = null;
	}

	public ShutdownHook(final MonitoringController ctrl) {
		this.ctrl = ctrl;
	}

	private final Vector<AbstractWorkerThread> workers = new Vector<AbstractWorkerThread>();

	/**
	 * Registers a worker, so that it is notified when system shutdown is
	 * initiated and to allow the TpmonShutdownHook to wait till the worker is
	 * finished.
	 * 
	 * @param newWorker
	 */

	public void registerWorker(final AbstractWorkerThread newWorker) {
		this.workers.add(newWorker);
	}

	@Override
	public void run() { // is called when VM shutdown (e.g., strg+c) is
						// initiated or when system.exit is called
		try {
			// is called when VM shutdown (e.g., strg+c) is initiated or when
			// system.exit is called
			ShutdownHook.log
					.info("ShutdownHook notifies all workers to initiate shutdown");
			this.initateShutdownForAllWorkers();
			while (!this.allWorkersFinished()) {
				Thread.sleep(500);
				ShutdownHook.log
						.info("hutdown delayed - At least one worker is busy ... waiting additional 0.5 seconds");
			}
			ShutdownHook.log
					.info("ShutdownHook can terminate since all workers are finished");
		} catch (final InterruptedException ex) {
			ShutdownHook.log.error("Interrupted Exception occured", ex);
		}
	}

	public synchronized void initateShutdownForAllWorkers() {
		for (final AbstractWorkerThread wrk : this.workers) {
			if (wrk != null) {
				wrk.initShutdown();
			}
		}
		this.ctrl.terminateMonitoring();
	}

	public synchronized boolean allWorkersFinished() {
		for (final AbstractWorkerThread wrk : this.workers) {
			if ((wrk != null) && (wrk.isFinished() == false)) {
				return false; // at least one busy worker exists
			}
		}
		return true;
	}
}
