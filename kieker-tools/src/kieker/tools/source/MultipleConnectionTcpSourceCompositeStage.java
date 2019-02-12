/***************************************************************************
 * Copyright 2019 Kieker Project (http://kieker-monitoring.net)
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

import kieker.analysis.source.ISourceCompositeStage;
import kieker.analysis.source.rewriter.ITraceMetadataRewriter;
import kieker.analysis.source.rewriter.NoneTraceMetadataRewriter;
import kieker.analysis.source.tcp.MultipleConnectionTcpSourceStage;
import kieker.common.configuration.Configuration;
import kieker.common.exception.ConfigurationException;
import kieker.common.record.IMonitoringRecord;
import kieker.common.util.classpath.InstantiationFactory;

import teetime.framework.CompositeStage;
import teetime.framework.OutputPort;

/**
 * Multiple TCP input composite stage. This wrapper composite stage allows to configure the
 * {@link MultipleConnectionTcpSourceStage} via a configuration object.
 *
 * Configuration parameters are:
 * <dl>
 * <dt>port</dt>
 * <dd>port where the service is listening on</dd>
 * <dt>capacity</dt>
 * <dd>capacity of the receiving buffer</dd>
 * <dt>recordRewriter</dt>
 * <dd>the record rewriter used to rewrite trace ids</dd>
 * </dl>
 * All names are prefixed with kieker.tools.source.MultipleConnectionTcpSourceCompositeStage.
 *
 * @author Reiner Jung
 * @since 1.15
 */
public class MultipleConnectionTcpSourceCompositeStage extends CompositeStage implements ISourceCompositeStage {

	private static final String PREFIX = MultipleConnectionTcpSourceCompositeStage.class.getCanonicalName();

	public static final String SOURCE_PORT = MultipleConnectionTcpSourceCompositeStage.PREFIX + ".port";
	private static final int DEFAULT_SOURCE_PORT = 9876;
	private static final String CAPACITY = MultipleConnectionTcpSourceCompositeStage.PREFIX + ".capacity";
	private static final int DEFAULT_CAPACITY = 1024 * 1024;

	private static final String REWRITER = MultipleConnectionTcpSourceCompositeStage.PREFIX + ".recordRewriter";

	private final MultipleConnectionTcpSourceStage reader;

	/**
	 * Create a composite reader stage.
	 *
	 * @param configuration
	 *            configuration parameters
	 * @throws ConfigurationException
	 *             on configuration errors during instantiation
	 */
	public MultipleConnectionTcpSourceCompositeStage(final Configuration configuration) throws ConfigurationException {
		final int inputPort = configuration.getIntProperty(MultipleConnectionTcpSourceCompositeStage.SOURCE_PORT,
				MultipleConnectionTcpSourceCompositeStage.DEFAULT_SOURCE_PORT);
		final int capacity = configuration.getIntProperty(MultipleConnectionTcpSourceCompositeStage.CAPACITY,
				MultipleConnectionTcpSourceCompositeStage.DEFAULT_CAPACITY);
		final String rewriterClassName = configuration.getStringProperty(MultipleConnectionTcpSourceCompositeStage.REWRITER,
				NoneTraceMetadataRewriter.class.getName());
		final Class<?>[] classes = null;
		final ITraceMetadataRewriter rewriter = InstantiationFactory.getInstance(configuration).create(ITraceMetadataRewriter.class,
				rewriterClassName, classes);
		this.reader = new MultipleConnectionTcpSourceStage(inputPort, capacity, rewriter);
	}

	@Override
	public OutputPort<IMonitoringRecord> getOutputPort() {
		return this.reader.getOutputPort();
	}

}
