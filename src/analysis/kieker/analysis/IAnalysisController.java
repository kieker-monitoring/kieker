/***************************************************************************
 * Copyright 2012 Kieker Project (http://kieker-monitoring.net)
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
 * @author Nils Christian Ehmke
 * 
 * @since 1.7
 */
public interface IAnalysisController extends IProjectContext {

	/**
	 * Registers the given instance as a new state observer. All instances are informed when the state (Running, Terminated etc) changes and get the new state as
	 * an object.
	 * 
	 * @param stateObserver
	 *            The observer to be registered.
	 */
	public void registerStateObserver(final IStateObserver stateObserver);

	/**
	 * Unregisters the given instance from the state observers.
	 * 
	 * @param stateObserver
	 *            The observer to be unregistered.
	 */
	public void unregisterStateObserver(final IStateObserver stateObserver);

	/**
	 * Delivers the value for the given (global) property within the analysis.
	 * 
	 * @return The value for the given property if it exists, null otherwise.
	 */
	public String getProperty(final String key);

	/**
	 * This method can be used to store the current configuration of this analysis controller in a specified file.
	 * The file can later be used to initialize the analysis controller.
	 * 
	 * @see AnalysisController#saveToFile(String)
	 * 
	 * @param file
	 *            The file in which the configuration will be stored.
	 * @throws IOException
	 *             If an exception during the storage occurred.
	 * @throws AnalysisConfigurationException
	 *             If the current configuration is somehow invalid.
	 */
	public void saveToFile(final File file) throws IOException, AnalysisConfigurationException;

	/**
	 * This method can be used to store the current configuration of this analysis controller in a specified file. It is just a convenient method which does the same
	 * as {@code AnalysisController.saveToFile(new File(pathname))}.
	 * 
	 * @see AnalysisController#saveToFile(File)
	 * 
	 * @param pathname
	 *            The pathname of the file in which the configuration will be stored.
	 * @throws IOException
	 *             If an exception during the storage occurred.
	 * @throws AnalysisConfigurationException
	 *             If the current configuration is somehow invalid.
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
	 * @throws IllegalStateException
	 *             If this instance has already been started or has already been terminated.
	 * @throws AnalysisConfigurationException
	 *             If the port names or the given plugins are invalid or not compatible.
	 */
	public void connect(final AbstractPlugin src, final String outputPortName, final AbstractPlugin dst, final String inputPortName)
			throws IllegalStateException, AnalysisConfigurationException;

	/**
	 * Connects the given repository to this plugin via the given name.
	 * 
	 * @param plugin
	 *            The plugin to be connected.
	 * @param repositoryPort
	 *            The name of the port to connect the repository.
	 * @param repository
	 *            The repository which should be used.
	 * @throws IllegalStateException
	 *             If this instance has already been started or has already been terminated.
	 * @throws AnalysisConfigurationException
	 *             If the port names or the given objects are invalid or not compatible.
	 */
	public void connect(final AbstractPlugin plugin, final String repositoryPort, final AbstractRepository repository) throws IllegalStateException,
			AnalysisConfigurationException;

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
	 */
	public void run() throws IllegalStateException, AnalysisConfigurationException;

	/**
	 * Initiates a termination of the analysis.
	 */
	public void terminate();

	/**
	 * Initiates a termination of the analysis.
	 * 
	 * @param error
	 *            Determines whether this is a normal termination or an termination due to an error during the analysis.
	 */
	public void terminate(final boolean error);

	/**
	 * Registers a log reader used as a source for monitoring records.
	 * 
	 * @param reader
	 *            The reader to be registered.
	 * @throws IllegalStateException
	 *             If the controller is already running or has already been terminated.
	 * @deprecated
	 */
	@Deprecated
	public void registerReader(final AbstractReaderPlugin reader) throws IllegalStateException;

	/**
	 * Registers the passed plugin.
	 * 
	 * All plugins which have been registered before calling the <i>run</i>-method, will be started once the analysis is started.
	 * 
	 * @param filter
	 *            The filter to be registered.
	 * @throws IllegalStateException
	 *             If the controller is already running or has already been terminated.
	 * @deprecated
	 */
	@Deprecated
	public void registerFilter(final AbstractFilterPlugin filter) throws IllegalStateException;

	/**
	 * Registers the passed repository.
	 * 
	 * @param repository
	 *            The repository to be registered.
	 * @throws IllegalStateException
	 *             If the controller is already running or has already been terminated.
	 * @deprecated
	 */
	@Deprecated
	public void registerRepository(final AbstractRepository repository) throws IllegalStateException;

	/**
	 * Delivers an unmodifiable collection of all readers.
	 * 
	 * @return All registered readers.
	 */
	public Collection<AbstractReaderPlugin> getReaders();

	/**
	 * Delivers an unmodifiable collection of all filters.
	 * 
	 * @return All registered filters.
	 */
	public Collection<AbstractFilterPlugin> getFilters();

	/**
	 * Delivers an unmodifiable collection of all repositories.
	 * 
	 * @return All registered repositories.
	 */
	public Collection<AbstractRepository> getRepositories();

}
