/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis.plugin.reader.list;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.plugin.reader.AbstractReaderPlugin;
import kieker.common.configuration.Configuration;

/**
 * Helper class that reads records added using the methods {@link #addAllObjects(List)} or {@link #addObject(Object)}.
 * Depending on the value of the {@link Configuration} variable {@value #CONFIG_PROPERTY_NAME_AWAIT_TERMINATION},
 * either the {@link #read()} method returns immediately, or awaits a termination via {@link kieker.analysis.AnalysisController#terminate()}.
 * 
 * Additions after this reader starts reading are ignored.
 * 
 * @param <T>
 * 
 * @author Andre van Hoorn, Jan Waller
 * 
 * @since 1.6
 */
@Plugin(
		programmaticOnly = true,
		description = "A reader that can be prefilled programmatically and that provides these records (mostly used in testing scenarios)",
		outputPorts = @OutputPort(name = ListReader.OUTPUT_PORT_NAME, eventTypes = { Object.class }),
		configuration = {
			@Property(name = ListReader.CONFIG_PROPERTY_NAME_AWAIT_TERMINATION, defaultValue = "false",
					description = "Determines whether the read()-method returns immediately or whether it awaits the termination via AnalysisController.terminate()")
		})
public class ListReader<T> extends AbstractReaderPlugin {

	/** The name of the output port delivering the read objects. */
	public static final String OUTPUT_PORT_NAME = "defaultOutput";

	/** The name of the configuration determining whether the reader terminates after all objects have been delivered of whether it waits for a terminate signal. */
	public static final String CONFIG_PROPERTY_NAME_AWAIT_TERMINATION = "awaitTermination";

	private final boolean awaitTermination;
	private final CountDownLatch terminationLatch = new CountDownLatch(1);

	private final List<T> objects = new CopyOnWriteArrayList<T>();

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param configuration
	 *            The configuration for this component.
	 * @param projectContext
	 *            The project context for this component.
	 */
	public ListReader(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);

		this.awaitTermination = this.configuration.getBooleanProperty(CONFIG_PROPERTY_NAME_AWAIT_TERMINATION);
		if (!this.awaitTermination) {
			this.terminationLatch.countDown(); // just to be sure that a call to await() would return immediately
		}
	}

	/**
	 * This method adds all given records to our list.
	 * 
	 * @param records
	 *            The records to be added.
	 */
	public void addAllObjects(final List<T> records) {
		this.objects.addAll(records);
	}

	/**
	 * This method adds the given object to our list.
	 * 
	 * @param object
	 *            The object to be added.
	 */
	public void addObject(final T object) {
		this.objects.add(object);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean read() {
		for (final T obj : this.objects) {
			super.deliver(ListReader.OUTPUT_PORT_NAME, obj);
		}
		try {
			if (this.awaitTermination) {
				this.logger.info("Awaiting termination latch to count down ...");
				this.terminationLatch.await();
				this.logger.info("Passed termination latch");
			}
		} catch (final InterruptedException e) {
			this.logger.error("Reader interrupted while awaiting termination", e);
			return false;
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void terminate(final boolean error) {
		this.terminationLatch.countDown();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();
		configuration.setProperty(CONFIG_PROPERTY_NAME_AWAIT_TERMINATION, Boolean.toString(this.awaitTermination));
		return configuration;
	}
}
