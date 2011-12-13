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

package kieker.tools.traceAnalysis.plugins.messageTraceRepository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import kieker.common.configuration.Configuration;
import kieker.tools.traceAnalysis.plugins.AbstractMessageTraceProcessingPlugin;
import kieker.tools.traceAnalysis.systemModel.AbstractTrace;
import kieker.tools.traceAnalysis.systemModel.MessageTrace;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;

/**
 * This class has exactly one input port named "in". The data which is send to
 * this plugin is not delegated in any way.
 * 
 * @author Andre van Hoorn
 */
public class MessageTraceRepositoryPlugin extends AbstractMessageTraceProcessingPlugin {

	public static final String MSG_TRACE_INPUT_PORT_NAME = "newEvent";
	// private static final Log log = LogFactory.getLog(MessageTraceRepositoryPlugin.class);

	private final Map<Long, MessageTrace> repo = new ConcurrentHashMap<Long, MessageTrace>(); // NOPMD

	// TODO: handle equivalence classes
	// See ticket http://samoa.informatik.uni-kiel.de:8000/kieker/ticket/150

	public MessageTraceRepositoryPlugin(final String name, final SystemModelRepository systemEntityFactory) {
		super(name, systemEntityFactory);
	}

	@Override
	public boolean execute() {
		return true; // no need to do anything here
	}

	@Override
	public void terminate(final boolean error) {
		// no need to do anything here
	}

	@Override
	protected Configuration getDefaultConfiguration() {
		return new Configuration();
	}

	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();

		// TODO: Save the current configuration

		return configuration;
	}

	@Override
	public void msgTraceInput(final Object mt) {
		MessageTraceRepositoryPlugin.this.repo.put(((AbstractTrace) mt).getTraceId(), (MessageTrace) mt);
	}
}
