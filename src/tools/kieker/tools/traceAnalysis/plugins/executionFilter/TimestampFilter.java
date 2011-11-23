/***************************************************************************
 * Copyright 2011 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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

package kieker.tools.traceAnalysis.plugins.executionFilter;

import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;

import kieker.analysis.configuration.Configuration;
import kieker.analysis.plugin.AbstractAnalysisPlugin;
import kieker.analysis.plugin.configuration.AbstractInputPort;
import kieker.analysis.plugin.configuration.OutputPort;
import kieker.tools.traceAnalysis.systemModel.Execution;

/**
 * Allows to filter Execution objects based on their given tin and tout
 * timestamps. <br>
 * 
 * This class has exactly one input port named "in" and one output ports named
 * "out". It receives only objects inheriting from the class {@link Execution}.
 * If the received object is within the defined timestamps, the object is
 * delivered unmodified to the output port.
 * 
 * @author Andre van Hoorn
 */
public class TimestampFilter extends AbstractAnalysisPlugin {

	public static final String CONFIG_IGNORE_EXECUTIONS_BEFORE_TIMESTAMP = TimestampFilter.class.getName() + ".ignoreExecutionsBeforeTimestamp";
	public static final String CONFIG_IGNORE_EXECUTIONS_AFTER_TIMESTAMP = TimestampFilter.class.getName() + ".ignorExecutionsAfterTimestamp";
	public static final long MAX_TIMESTAMP = Long.MAX_VALUE;
	public static final long MIN_TIMESTAMP = 0;

	private final long ignoreExecutionsBeforeTimestamp;
	private final long ignorExecutionsAfterTimestamp;

	private final OutputPort executionOutputPort = new OutputPort("Execution output", new CopyOnWriteArrayList<Class<?>>(new Class<?>[] { Execution.class }));

	/**
	 * Creates a new instance of the class {@link TimestampFilter} with the
	 * given parameters.
	 * 
	 * @param configuration
	 *            The configuration used to initialize this instance. It should
	 *            contain the properties for the minimum and maximum timestamp.
	 */
	public TimestampFilter(final Configuration configuration) {
		super(configuration);

		/* Load the content for the fields from the given configuration. */
		this.ignoreExecutionsBeforeTimestamp = configuration.getLongProperty(TimestampFilter.CONFIG_IGNORE_EXECUTIONS_BEFORE_TIMESTAMP);
		this.ignorExecutionsAfterTimestamp = configuration.getLongProperty(TimestampFilter.CONFIG_IGNORE_EXECUTIONS_AFTER_TIMESTAMP);

		super.registerOutputPort("out", this.executionOutputPort);
		super.registerInputPort("in", this.executionInputPort);
	}

	/**
	 * Creates a filter instance that only passes Execution objects <i>e</i>
	 * with the property <i>e.tin &gt;= ignoreExecutionsBeforeTimestamp</i> and
	 * <i>e.tout &lt;= ignoreExecutionsAfterTimestamp</i>.
	 * 
	 * @param ignoreExecutionsBeforeTimestamp
	 * @param ignoreExecutionsAfterTimestamp
	 */
	public TimestampFilter(final long ignoreExecutionsBeforeTimestamp, final long ignoreExecutionsAfterTimestamp) {
		super(new Configuration(null));

		this.ignoreExecutionsBeforeTimestamp = ignoreExecutionsBeforeTimestamp;
		this.ignorExecutionsAfterTimestamp = ignoreExecutionsAfterTimestamp;

		super.registerOutputPort("out", this.executionOutputPort);
		super.registerInputPort("in", this.executionInputPort);
	}

	public AbstractInputPort getExecutionInputPort() {
		return this.executionInputPort;
	}

	private final AbstractInputPort executionInputPort = new AbstractInputPort("Execution input",
			Collections.unmodifiableCollection(new CopyOnWriteArrayList<Class<?>>(new Class<?>[] { Execution.class }))) {

		@Override
		public void newEvent(final Object obj) {
			final Execution event = (Execution) obj;
			TimestampFilter.this.newExecution(event);
		}
	};

	public OutputPort getExecutionOutputPort() {
		return this.executionOutputPort;
	}

	private void newExecution(final Execution execution) {
		if ((execution.getTin() < this.ignoreExecutionsBeforeTimestamp) || (execution.getTout() > this.ignorExecutionsAfterTimestamp)) {
			return;
		}
		this.executionOutputPort.deliver(execution);
	}

	@Override
	public boolean execute() {
		return true; // do nothing
	}

	@Override
	public void terminate(final boolean error) {
		// do nothing
	}

	@Override
	protected Properties getDefaultProperties() {
		final Properties properties = new Properties();

		properties.setProperty(TimestampFilter.CONFIG_IGNORE_EXECUTIONS_AFTER_TIMESTAMP, Long.toString(TimestampFilter.MAX_TIMESTAMP));
		properties.setProperty(TimestampFilter.CONFIG_IGNORE_EXECUTIONS_BEFORE_TIMESTAMP, Long.toString(TimestampFilter.MIN_TIMESTAMP));

		return properties;
	}

	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration(null);

		// TODO: Save the current configuration

		return configuration;
	}
}
