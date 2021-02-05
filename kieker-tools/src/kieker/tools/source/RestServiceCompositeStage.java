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
package kieker.tools.source;

import kieker.analysis.source.IAccessHandler;
import kieker.analysis.source.ISourceCompositeStage;
import kieker.analysis.source.rest.RestServiceStage;
import kieker.common.configuration.Configuration;
import kieker.common.exception.ConfigurationException;
import kieker.common.record.IMonitoringRecord;
import kieker.common.util.classpath.InstantiationFactory;

import teetime.framework.CompositeStage;
import teetime.framework.OutputPort;

/**
 * Multiple TCP input composite stage. This wrapper composite stage allows to configure the
 * {@link RestServiceStage} via a configuration object.
 *
 * Configuration parameters are:
 * <dl>
 * <dt>port</dt>
 * <dd>port where the service is listening on</dd>
 * <dt>hostname</dt>
 * <dd>virtual host name to expect in URI (optional)</dd>
 * <dt>accessHandler</dt>
 * <dd>Not all hosts should be allowed to send monitoring data, the access handler limits access to the service</dd>
 * </dl>
 * All names are prefixed with kieker.tools.source.RestServiceCompositeStage.
 *
 * @author Reiner Jung
 * @since 1.15
 */
public class RestServiceCompositeStage extends CompositeStage implements ISourceCompositeStage {

	private static final String PREFIX = RestServiceCompositeStage.class.getCanonicalName();

	private static final String SERVICE_HOSTNAME = PREFIX + ".hostname";
	private static final String SERVICE_PORT = PREFIX + ".port";
	private static final String ACCESS_HANDLER = PREFIX + ".accessHandler";

	private final RestServiceStage serviceStage;

	/**
	 * Create a source composite stage for rest services.
	 *
	 * @param configuration
	 *            configuration for the service
	 * @throws ConfigurationException
	 *             on configuration errors
	 */
	public RestServiceCompositeStage(final Configuration configuration) throws ConfigurationException {
		final String hostname = configuration.getStringProperty(RestServiceCompositeStage.SERVICE_HOSTNAME);
		final int port = configuration.getIntProperty(RestServiceCompositeStage.SERVICE_PORT);
		final IAccessHandler accessRestrictionHandler = InstantiationFactory.createWithConfiguration(IAccessHandler.class,
				configuration.getStringProperty(RestServiceCompositeStage.ACCESS_HANDLER), configuration);

		this.serviceStage = new RestServiceStage(hostname, port, accessRestrictionHandler);
	}

	/**
	 * Create a source composite stage for rest services.
	 *
	 * @param hostname
	 *            hostname to listen to
	 * @param port
	 *            port to listen on
	 * @param accessHandler
	 *            controls access to the rest service
	 */
	public RestServiceCompositeStage(final String hostname, final int port, final IAccessHandler accessHandler) {
		this.serviceStage = new RestServiceStage(hostname, port, accessHandler);
	}

	@Override
	public OutputPort<IMonitoringRecord> getOutputPort() {
		return this.serviceStage.getOutputPort();
	}

}
