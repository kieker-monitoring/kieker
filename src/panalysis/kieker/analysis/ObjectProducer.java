/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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
package kieker.analysis;

import java.util.concurrent.Callable;

import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.plugin.reader.AbstractReaderPlugin;
import kieker.common.configuration.Configuration;

@Plugin(
		outputPorts = @OutputPort(name = ObjectProducer.OUTPUT_PORT_NAME),
		configuration = @Property(name = ObjectProducer.CONFIG_PROPERTY_NAME_OBJECTS_TO_CREATE, defaultValue = "100"))
public class ObjectProducer<T> extends AbstractReaderPlugin {

	public static final String CONFIG_PROPERTY_NAME_OBJECTS_TO_CREATE = "numObjectsToCreate";
	public static final String OUTPUT_PORT_NAME = "output";

	private Callable<T> objectCreator;
	private long numObjectsToCreate;

	public ObjectProducer(final Configuration configuration, final IProjectContext projectContext, final Callable<T> objectCreator) {
		super(configuration, projectContext);

		this.numObjectsToCreate = configuration.getLongProperty(CONFIG_PROPERTY_NAME_OBJECTS_TO_CREATE);
		this.objectCreator = objectCreator;
	}

	public Callable<T> getObjectCreator() {
		return this.objectCreator;
	}

	public void setObjectCreator(final Callable<T> objectCreator) {
		this.objectCreator = objectCreator;
	}

	public long getNumObjectsToCreate() {
		return this.numObjectsToCreate;
	}

	public void setNumObjectsToCreate(final long numObjectsToCreate) {
		this.numObjectsToCreate = numObjectsToCreate;
	}

	public boolean read() {
		for (int idx = 0; idx < this.numObjectsToCreate; idx++) {
			try {
				final T newObject = this.objectCreator.call();
				super.deliver(OUTPUT_PORT_NAME, newObject);
			} catch (final Exception e) {
				throw new IllegalStateException();
			}
		}

		return true;
	}

	public void terminate(final boolean error) {
		// Nothing to do
	}

	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();
		configuration.setProperty(CONFIG_PROPERTY_NAME_OBJECTS_TO_CREATE, Long.toString(this.numObjectsToCreate));
		return configuration;
	}

}
