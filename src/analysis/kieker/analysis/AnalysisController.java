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

package kieker.analysis;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;

import kieker.analysis.model.analysisMetaModel.IAnalysisPlugin;
import kieker.analysis.model.analysisMetaModel.IConfigurable;
import kieker.analysis.model.analysisMetaModel.IConnector;
import kieker.analysis.model.analysisMetaModel.IInputPort;
import kieker.analysis.model.analysisMetaModel.IOutputPort;
import kieker.analysis.model.analysisMetaModel.IPlugin;
import kieker.analysis.model.analysisMetaModel.IPort;
import kieker.analysis.model.analysisMetaModel.IProject;
import kieker.analysis.model.analysisMetaModel.IReader;
import kieker.analysis.model.analysisMetaModel.impl.AnalysisMetaModelPackage;
import kieker.analysis.plugin.AbstractAnalysisPlugin;
import kieker.analysis.plugin.AbstractMonitoringReader;
import kieker.analysis.plugin.AbstractPlugin;
import kieker.analysis.plugin.IMonitoringRecordConsumerPlugin;
import kieker.analysis.reader.IMonitoringReader;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.IMonitoringRecordReceiver;

/**
 * 
 * 2010-03-06 Matthias: Added exception handling for the case that the log
 * reader is missing. (I had lot of pain because of this).
 * 
 * 2010-03-04 Andre: Release of Kieker 1.1
 * 
 * 
 * TODOs: - In the context of realizing a event driven architecture for the
 * model synthesis layer, it makes sense to refactor the KiekerRecordConsumers
 * to KiekerRecordFilters. Consumers are only about how data goes in - but we
 * also have now a concept what should happen if the data goes out: its again a
 * publisher, to which other filters or plugins can subscribe to.
 * 
 * @author Andre van Hoorn, Matthias Rohr
 */
public class AnalysisController {

	private static final Log LOG = LogFactory.getLog(AnalysisController.class);
	private IMonitoringReader logReader;
	/**
	 * this are the consumers for data that are coming into Kieker by readers
	 * (files or system under monitoring)
	 */
	private final Collection<IMonitoringRecordConsumerPlugin> consumers = new CopyOnWriteArrayList<IMonitoringRecordConsumerPlugin>();
	/** Contains all consumers which consume records of any type */
	private final Collection<IMonitoringRecordConsumerPlugin> anyTypeConsumers = new CopyOnWriteArrayList<IMonitoringRecordConsumerPlugin>();
	/** Contains mapping of record types to subscribed consumers */
	private final ConcurrentMap<Class<? extends IMonitoringRecord>, Collection<IMonitoringRecordConsumerPlugin>> specificTypeConsumers = new ConcurrentHashMap<Class<? extends IMonitoringRecord>, Collection<IMonitoringRecordConsumerPlugin>>();
	private final Collection<kieker.analysis.plugin.IAnalysisPlugin> plugins = new CopyOnWriteArrayList<kieker.analysis.plugin.IAnalysisPlugin>();

	/**
	 * Will be count down after the analysis is set-up.
	 */
	private final CountDownLatch initializationLatch = new CountDownLatch(1);

	/**
	 * Constructs an {@link AnalysisController} instance.
	 */
	public AnalysisController() {
		// do nothing
	}

	/**
	 * This method loads the model from the given file and creates an instance
	 * of this class for every single "Project" within this model. The file
	 * should therefore be an instance of the analysis meta model.
	 * 
	 * @param file
	 *            The configuration file for the analysis.
	 * 
	 * @return A map containing the sub projects. The key is the name of the
	 *         project, the value is the instance of this class. If something
	 *         went wrong, null will be returned.
	 * 
	 * @throws Exception
	 *             If something went wrong.
	 */
	static public Map<String, AnalysisController> loadFromFile(File file)
			throws Exception {
		/* Try to load everything. */
		EList<EObject> content = openModelFile(file);
		if (!content.isEmpty()) {
			/* The first (and only) element should be the "parent project" */
			IProject project = (IProject) content.get(0);

			/* Now find all "sub projects" */
			List<IProject> projects = new ArrayList<IProject>();
			List<IProject> toExpand = new ArrayList<IProject>();
			toExpand.add(project);
			while (!toExpand.isEmpty()) {
				IProject currProj = toExpand.remove(0);
				projects.add(currProj);
				EList<IConfigurable> configs = currProj.getConfigurables();
				for (IConfigurable c : configs) {
					if (c instanceof IProject) {
						toExpand.add((IProject) c);
					}
				}
			}

			/* Configure all projects. */
			Map<String, AnalysisController> map = new HashMap<String, AnalysisController>();

			for (IProject currProject : projects) {
				map.put(currProject.getName(), new AnalysisController(
						currProject));
			}
			return map;
		}
		return null;
	}

	/**
	 * Creates a new instance of the class {@link AnalysisController} but uses
	 * the given instance of @link{Project} to construct the analysis.
	 * 
	 * @param project
	 *            The project instance for the analysis.
	 * @throws Exception
	 */
	private AnalysisController(IProject project) throws Exception {
		System.out.println(project.getName());

		/*
		 * While we run through a project, we have to remember different things:
		 * 1) The connection between the ports and their "parent" plugins (as a
		 * map). 2) The connection between the plugins and their created
		 * counterpart (as a map). 3) The "real" connection within the model.
		 */
		EList<IConfigurable> configs = project.getConfigurables();
		List<IConnector> connectors = new ArrayList<IConnector>();
		Map<IPort, IPlugin> portPluginMap = new HashMap<IPort, IPlugin>();
		Map<IPlugin, Object> pluginObjMap = new HashMap<IPlugin, Object>();

		/* Run through the "configurables" to extract all plugins. */
		for (IConfigurable c : configs) {

			if (c instanceof IPlugin) {
				/*
				 * We found a plugin. Not we have to determine whether this is a
				 * reader or a normal plugin.
				 */
				IPlugin p = (IPlugin) c;
				if (p instanceof IReader) {
					System.out.println("Reader gefunden: " + p.getName());
					IMonitoringReader reader = (IMonitoringReader) p.getClassname().newInstance();
					reader.init(((IReader) p).getInitString());
					this.setReader(reader);
					pluginObjMap.put(p, reader);
				} else {
					System.out.println("Plugin gefunden: " + p.getName());
					Object plugin = p.getClassname().newInstance();
					pluginObjMap.put(p, plugin);
				}

				/*
				 * Now we run through all ports of the current plugin. We
				 * remember them to have a connection between the ports and
				 * their parent. We will also accumulate all Connectors.
				 */

				for (IOutputPort oPort : p.getOutputPorts()) {
					connectors.addAll(oPort.getOutConnector());

					portPluginMap.put(oPort, p);
				}

				if (p instanceof IAnalysisPlugin) {
					for (IInputPort iPort : ((IAnalysisPlugin) p).getInputPorts()) {
						portPluginMap.put(iPort, p);
					}
				}
			}
		}

		/*
		 * Now we should have initialized all plugins. We can start to assemble
		 * the structure.
		 */
		for (IConnector c : connectors) {
			/* We can get the plugins via the map. */
			IPlugin in = portPluginMap.get(c.getDstInputPort());
			IPlugin out = portPluginMap.get(c.getSicOutputPort());
			System.out.format("Connector gefunden. Verbinde Output von "
					+ "%s mit Input von %s\n", out.getName(), in.getName());

			AbstractPlugin outObj = (AbstractPlugin) pluginObjMap
					.get(out);
			AbstractAnalysisPlugin inObj = (AbstractAnalysisPlugin) pluginObjMap
					.get(in);

			kieker.analysis.plugin.configuration.IOutputPort outPort = outObj.getOutputPort(c.getSicOutputPort().getName());
			kieker.analysis.plugin.configuration.IInputPort inPort = inObj.getInputPort(c.getDstInputPort().getName());

			outPort.subscribe(inPort);
		}
	}

	/**
	 * Opens a given file which should contain an instance of the analysis meta
	 * modell and delivers a list with the whole content.
	 * 
	 * @param file
	 *            The file to be opened.
	 * @return A list with the content of the file.
	 */
	static EList<EObject> openModelFile(File file) {
		/* Create a resource set to work with. */
		ResourceSet resourceSet = new ResourceSetImpl();

		/* Initialize the package information */
		AnalysisMetaModelPackage.init();

		/* Set OPTION_RECORD_UNKNOWN_FEATURE prior to calling getResource. */
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("*",
				new EcoreResourceFactoryImpl() {
					@Override
					public Resource createResource(URI uri) {
						XMIResourceImpl resource = (XMIResourceImpl) super
								.createResource(uri);
						resource.getDefaultLoadOptions().put(
								XMLResource.OPTION_RECORD_UNKNOWN_FEATURE,
								Boolean.TRUE);
						return resource;
					}
				});

		/* Try to load the ressource. */
		XMIResource resource = (XMIResource) resourceSet.getResource(
				URI.createFileURI(file.toString()), true);

		try {
			resource.load(Collections.EMPTY_MAP);
		} catch (IOException e) {
			AnalysisController.LOG.error("Could not open the given file.");
		}

		return resource.getContents();
	}

	/**
	 * Starts an {@link AnalysisController} instance and returns after the
	 * configured reader finished reading and all analysis plug-ins terminated;
	 * or immediately, if an error occurs.
	 * 
	 * @return true on success; false if an error occurred
	 */
	public boolean run() {
		boolean success = true;

		try {
			/**
			 * Call execute() method of all plug-ins.
			 */
			for (final kieker.analysis.plugin.IAnalysisPlugin c : this.plugins) {
				if (!c.execute()) {
					AnalysisController.LOG
							.error("A plug-in's execute message failed");
					success = false;
				}
			}

			/**
			 * Make sure that log reader is not null
			 */
			if (this.logReader == null) {
				AnalysisController.LOG.error("No log reader registered.");
				success = false;
			}

			/**
			 * Add delegation receiver to reader.
			 */
			if (success) {
				/*
				 * this.logReader
				 * .addRecordReceiver(new IMonitoringRecordReceiver() {
				 * 
				 * /**
				 * Delegates the records provided by the reader to
				 * the registered record consumers
				 */
				/*
				 * @Override
				 * public boolean newMonitoringRecord(
				 * final IMonitoringRecord monitoringRecord) {
				 * // abort on consumer error
				 * return AnalysisController.this
				 * .deliverRecordToConsumers(
				 * monitoringRecord, true);
				 * }
				 * });
				 */
			}

			/**
			 * Start reading
			 */
			if (success) {
				// notify threads waiting for the initialization to be done
				this.initializationLatch.countDown();
				if (!this.logReader.read()) {
					AnalysisController.LOG
							.error("Calling execute() on logReader returned false");
					success = false;
				}
			}
		} catch (final Exception exc) { // NOCS // NOPMD
			AnalysisController.LOG.error("Error occurred: " + exc.getMessage());
			success = false;
		} finally {
			// to make sure that all waiting threads are released
			this.initializationLatch.countDown();
			try {
				for (final kieker.analysis.plugin.IAnalysisPlugin c : this.plugins) {
					c.terminate(!success); // normal termination (w/o error)
				}
			} catch (final Exception e) { // NOCS // NOPMD
				AnalysisController.LOG.error(
						"Error during termination: " + e.getMessage(), e);
			}
		}

		return success;
	}

	/**
	 * Initiates a termination of the analysis.
	 */
	public void terminate() {
		/*
		 * terminate the reader. After the reader has terminated, the run
		 * method() will terminate all plugins
		 */
		AnalysisController.LOG
				.info("Explicit termination of the analysis. Terminating the reader ...");
		this.logReader.terminate();
	}

	/**
	 * Returns a {@link CountDownLatch} which has the value 0 after the {@link AnalysisController} is initialized and the reader is running.
	 * 
	 * @return the initializationLatch
	 */
	protected final CountDownLatch getInitializationLatch() {
		return this.initializationLatch;
	}

	/**
	 * Sets the log reader used as the source for monitoring records.
	 * 
	 * @param reader
	 */
	public void setReader(final IMonitoringReader reader) {
		this.logReader = reader;
	}

	/**
	 * Adds the given consumer to the analysis.
	 * 
	 * @param consumer
	 */
	private void addRecordConsumer(
			final IMonitoringRecordConsumerPlugin consumer) {
		this.consumers.add(consumer);
		final Collection<Class<? extends IMonitoringRecord>> recordTypeSubscriptionList = consumer
				.getRecordTypeSubscriptionList();
		if (recordTypeSubscriptionList == null) {
			this.anyTypeConsumers.add(consumer);
		} else {
			for (final Class<? extends IMonitoringRecord> recordType : recordTypeSubscriptionList) {
				Collection<IMonitoringRecordConsumerPlugin> cList = this.specificTypeConsumers
						.get(recordType);
				if (cList == null) {
					cList = new CopyOnWriteArrayList<IMonitoringRecordConsumerPlugin>(); // NOPMD
					// (new
					// in
					// loops)
					this.specificTypeConsumers.put(recordType, cList);
				}
				cList.add(consumer);
			}
		}
	}

	/**
	 * Registers the passed plug-in <i>c<i>. If <i>c</i> is an instance of the
	 * interface <i>IMonitoringRecordConsumerPlugin</i> it is also registered as
	 * a record consumer.
	 */
	public void registerPlugin(final kieker.analysis.plugin.IAnalysisPlugin plugin) {
		this.plugins.add(plugin);
		AnalysisController.LOG.debug("Registered plugin " + plugin);

		if (plugin instanceof IMonitoringRecordConsumerPlugin) {
			AnalysisController.LOG.debug("Plugin " + plugin
					+ " also registered as record consumer");
			this.addRecordConsumer((IMonitoringRecordConsumerPlugin) plugin);
		}
	}

	/**
	 * Delivers the given record to the consumers that are registered for this
	 * type of records.
	 * 
	 * @param monitoringRecord
	 * @param abortOnConsumerError
	 *            if true, the method returns immediately when a consumer
	 *            reports an error
	 * @return
	 * @throws kieker.common.record.MonitoringRecordReceiverException
	 *             true if no consumer reported an error; false if at least one
	 *             consumer reported an error
	 */
	private final boolean deliverRecordToConsumers(
			final IMonitoringRecord monitoringRecord,
			final boolean abortOnConsumerError) {
		final String consumerErrorMsg = "Consumer returned false. Aborting delivery of record. ";

		boolean success = true;

		for (final IMonitoringRecordConsumerPlugin c : this.anyTypeConsumers) {
			if (!c.newMonitoringRecord(monitoringRecord)) {
				success = false;
				if (abortOnConsumerError) {
					AnalysisController.LOG.warn(consumerErrorMsg);
					return false;
				}
			}
		}
		final Collection<IMonitoringRecordConsumerPlugin> cList = this.specificTypeConsumers
				.get(monitoringRecord.getClass());
		if (cList != null) {
			for (final IMonitoringRecordConsumerPlugin c : cList) {
				if (!c.newMonitoringRecord(monitoringRecord)) {
					success = false;
					if (abortOnConsumerError) { // NOCS (NestedIf)
						AnalysisController.LOG.warn(consumerErrorMsg);
						return false;
					}
				}
			}
		}

		return success;
	}
}
