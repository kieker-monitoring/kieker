package kieker.monitoring.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
 * This class ensures that virtual machine shutdown (e.g., cause by a
 * System.exit(int)) is delayed until all monitoring data is written. This is
 * important for the asynchronous writers for the files system and database,
 * since these store data with a small delay and data would be lost when
 * System.exit is not delayed.
 * 
 * When the system shutdown is initiated, the termination of the Virtual Machine
 * is delayed until all registered worker queues are empty.
 * 
 * @author Matthias Rohr, Andre van Hoorn, Jan Waller
 */
class ShutdownHook extends Thread {
	private static final Log log = LogFactory.getLog(ShutdownHook.class);

	private final IController ctrl;

	public ShutdownHook(final IController ctrl) {
		this.ctrl = ctrl;
	}

	@Override
	public void run() {
		// is called when VM shutdown (e.g., strg+c) is initiated or when system.exit is called
		if (!ctrl.isMonitoringTerminated()) {
			//TODO: We can't use a logger in shutdown hooks, logger may already be down!
			ShutdownHook.log.info("ShutdownHook notifies controller to initiate shutdown");
			this.ctrl.terminateMonitoring();
		}
	}
}
