/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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
package kieker.analysis.source.rest;

import kieker.analysis.source.IAccessHandler;
import kieker.common.record.IMonitoringRecord;

import fi.iki.elonen.NanoHTTPD;
import teetime.framework.AbstractProducerStage;

/**
 * REST / HTTP source stage.
 *
 * @author Reiner Jung
 *
 * @since 1.15
 */
public class RestServiceStage extends AbstractProducerStage<IMonitoringRecord> {

	private final String hostname;
	private final int port;
	private final IAccessHandler accessRestrictionHandler;

	/**
	 * Create a rest service stage.
	 *
	 * @param hostname
	 *            hostname to listen to
	 * @param port
	 *            port to listen to
	 * @param accessRestrictionHandler
	 *            access restriction handler for connections
	 */
	public RestServiceStage(final String hostname, final int port, final IAccessHandler accessRestrictionHandler) {
		this.hostname = hostname;
		this.port = port;
		this.accessRestrictionHandler = accessRestrictionHandler;
	}

	@Override
	protected void execute() throws Exception {
		final RestService restService = new RestService(this, this.hostname, this.port, this.accessRestrictionHandler);
		restService.start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
		this.workCompleted();
	}

}
