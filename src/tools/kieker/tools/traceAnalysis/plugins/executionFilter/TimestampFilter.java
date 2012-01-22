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

import kieker.analysis.plugin.AbstractAnalysisPlugin;
import kieker.analysis.plugin.port.InputPort;
import kieker.analysis.plugin.port.OutputPort;
import kieker.analysis.plugin.port.Plugin;
import kieker.analysis.repository.AbstractRepository;
import kieker.common.configuration.Configuration;
import kieker.tools.traceAnalysis.systemModel.Execution;

/**
 * Allows to filter Execution objects based on their given tin and tout
 * timestamps. <br>
 * 
 * This class has exactly one input port and one output port. It receives only objects inheriting from the class {@link Execution}. If the received object is within
 * the defined timestamps, the object is delivered unmodified to the output port.
 * 
 * @author Andre van Hoorn
 */
@Plugin(
		outputPorts = {
			@OutputPort(name = TimestampFilter.OUTPUT_PORT_NAME, description = "Execution output", eventTypes = { Execution.class })
		})
public class TimestampFilter extends AbstractAnalysisPlugin {

	public static final String INPUT_PORT_NAME = "newExecution";
	public static final String OUTPUT_PORT_NAME = "defaultOutput";
	public static final String CONFIG_IGNORE_EXECUTIONS_BEFORE_TIMESTAMP = TimestampFilter.class.getName() + ".ignoreExecutionsBeforeTimestamp";
	public static final String CONFIG_IGNORE_EXECUTIONS_AFTER_TIMESTAMP = TimestampFilter.class.getName() + ".ignorExecutionsAfterTimestamp";
	public static final long MAX_TIMESTAMP = Long.MAX_VALUE;
	public static final long MIN_TIMESTAMP = 0;

	private final long ignoreExecutionsBeforeTimestamp;
	private final long ignoreExecutionsAfterTimestamp;

	/**
	 * Creates a new instance of the class {@link TimestampFilter} with the
	 * given parameters.
	 * 
	 * @param configuration
	 *            The configuration used to initialize this instance. It should
	 *            contain the properties for the minimum and maximum timestamp.
	 */
	public TimestampFilter(final Configuration configuration, final AbstractRepository repositories[]) {
		super(configuration, repositories);

		/* Load the content for the fields from the given configuration. */
		this.ignoreExecutionsBeforeTimestamp = configuration.getLongProperty(TimestampFilter.CONFIG_IGNORE_EXECUTIONS_BEFORE_TIMESTAMP);
		this.ignoreExecutionsAfterTimestamp = configuration.getLongProperty(TimestampFilter.CONFIG_IGNORE_EXECUTIONS_AFTER_TIMESTAMP);
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
		super(new Configuration(null), new AbstractRepository[0]);

		this.ignoreExecutionsBeforeTimestamp = ignoreExecutionsBeforeTimestamp;
		this.ignoreExecutionsAfterTimestamp = ignoreExecutionsAfterTimestamp;
	}

	@InputPort(description = "Execution input", eventTypes = { Execution.class })
	public void newExecution(final Object data) {
		final Execution execution = (Execution) data;
		if ((execution.getTin() < this.ignoreExecutionsBeforeTimestamp) || (execution.getTout() > this.ignoreExecutionsAfterTimestamp)) {
			return;
		}
		super.deliver(TimestampFilter.OUTPUT_PORT_NAME, execution);
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
	protected Configuration getDefaultConfiguration() {
		final Configuration configuration = new Configuration();

		configuration.setProperty(TimestampFilter.CONFIG_IGNORE_EXECUTIONS_AFTER_TIMESTAMP, Long.toString(TimestampFilter.MAX_TIMESTAMP));
		configuration.setProperty(TimestampFilter.CONFIG_IGNORE_EXECUTIONS_BEFORE_TIMESTAMP, Long.toString(TimestampFilter.MIN_TIMESTAMP));

		return configuration;
	}

	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration(null);

		configuration.setProperty(TimestampFilter.CONFIG_IGNORE_EXECUTIONS_AFTER_TIMESTAMP, Long.toString(this.ignoreExecutionsAfterTimestamp));
		configuration.setProperty(TimestampFilter.CONFIG_IGNORE_EXECUTIONS_BEFORE_TIMESTAMP, Long.toString(this.ignoreExecutionsBeforeTimestamp));

		return configuration;
	}

	@Override
	protected AbstractRepository[] getDefaultRepositories() {
		return new AbstractRepository[0];
	}

	@Override
	public AbstractRepository[] getCurrentRepositories() {
		return new AbstractRepository[0];
	}
}
