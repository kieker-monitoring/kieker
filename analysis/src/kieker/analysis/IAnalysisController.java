/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import kieker.analysis.AnalysisController.IStateObserver;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.AbstractPlugin;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.analysis.plugin.reader.AbstractReaderPlugin;
import kieker.analysis.repository.AbstractRepository;

/**
 * This is the interface to {@link kieker.analysis.AnalysisController}, allowing not only to access read-methods but also to modify the analysis itself.
 *
 * @author Nils Christian Ehmke
 *
 * @since 1.7
 * @deprecated 1.15 can be removed when all tools are ported to TeeTime
 */
@Deprecated
public interface IAnalysisController extends IProjectContext {

	/**
	 * Registers the given instance as a new state observer. All instances are informed when the state (Running, Terminated etc) changes and get the new state as
	 * an object.
	 *
	 * @param stateObserver
	 *            The observer to be registered.
	 *
	 * @since 1.7
	 */
	public void registerStateObserver(final IStateObserver stateObserver);

	/**
	 * Unregisters the given instance from the state observers.
	 *
	 * @param stateObserver
	 *            The observer to be unregistered.
	 *
	 * @since 1.7
	 */
	public void unregisterStateObserver(final IStateObserver stateObserver);

	/**
	 * This method can be used to store the current configuration of this analysis controller in a specified file.
	 * The file can later be used to initialize the analysis controller.
	 *
	 * @see #saveToFile(String)
	 *
	 * @param file
	 *            The file in which the configuration will be stored.
	 * @throws IOException
	 *             If an exception during the storage occurred.
	 * @throws AnalysisConfigurationException
	 *             If the current configuration is somehow invalid.
	 *
	 * @since 1.7
	 */
	public void saveToFile(final File file) throws IOException, AnalysisConfigurationException;

	/**
	 * This method can be used to store the current configuration of this analysis controller in a specified file. It is just a convenient method which does the same
	 * as {@code #saveToFile(File)}.
	 *
	 * @see #saveToFile(File)
	 *
	 * @param pathname
	 *            The pathname of the file in which the configuration will be stored.
	 * @throws IOException
	 *             If an exception during the storage occurred.
	 * @throws AnalysisConfigurationException
	 *             If the current configuration is somehow invalid.
	 *
	 * @since 1.7
	 */
	public void saveToFile(final String pathname) throws IOException, AnalysisConfigurationException;

	/**
	 * This method should be used to connect two plugins. The plugins have to be registered within this controller instance.
	 *
	 * @param src
	 *            The source plugin.
	 * @param outputPortName
	 *            The output port of the source plugin.
	 * @param dst
	 *            The destination plugin.
	 * @param inputPortName
	 *            The input port of the destination port.
	 *
	 * @throws IllegalStateException
	 *             If this instance has already been started or has already been terminated.
	 * @throws AnalysisConfigurationException
	 *             If the port names or the given plugins are invalid or not compatible.
	 * @since 1.7
	 */
	public void connect(final AbstractPlugin src, final String outputPortName, final AbstractPlugin dst,
			final String inputPortName) throws AnalysisConfigurationException;

	/**
	 * Connects the given repository to this plugin via the given name.
	 *
	 * @param plugin
	 *            The plugin to be connected.
	 * @param repositoryPort
	 *            The name of the port to connect the repository.
	 * @param repository
	 *            The repository which should be used.
	 *
	 * @throws IllegalStateException
	 *             If this instance has already been started or has already been terminated.
	 * @throws AnalysisConfigurationException
	 *             If the port names or the given objects are invalid or not compatible.
	 *
	 * @since 1.7
	 */
	public void connect(final AbstractPlugin plugin, final String repositoryPort, final AbstractRepository repository) throws AnalysisConfigurationException;

	/**
	 * Starts an {@link AnalysisController} instance.
	 * The method returns after all configured readers finished reading and all analysis plug-ins terminated
	 *
	 * On errors during the initialization, Exceptions are thrown.
	 *
	 * @throws IllegalStateException
	 *             If the current instance has already been started or already been terminated.
	 * @throws AnalysisConfigurationException
	 *             If plugins with mandatory repositories have not been connected properly or couldn't be initialized.
	 *
	 * @since 1.7
	 */
	public void run() throws AnalysisConfigurationException;

	/**
	 * Initiates a termination of the analysis.
	 *
	 * @since 1.7
	 */
	public void terminate();

	/**
	 * Initiates a termination of the analysis.
	 *
	 * @param error
	 *            Determines whether this is a normal termination or an termination due to an error during the analysis.
	 *
	 * @since 1.7
	 */
	public void terminate(final boolean error);

	/**
	 * Delivers an unmodifiable collection of all readers.
	 *
	 * @return All registered readers.
	 *
	 * @since 1.7
	 */
	public Collection<AbstractReaderPlugin> getReaders();

	/**
	 * Delivers an unmodifiable collection of all filters.
	 *
	 * @return All registered filters.
	 *
	 * @since 1.7
	 */
	public Collection<AbstractFilterPlugin> getFilters();

	/**
	 * Delivers an unmodifiable collection of all repositories.
	 *
	 * @return All registered repositories.
	 *
	 * @since 1.7
	 */
	public Collection<AbstractRepository> getRepositories();

}
