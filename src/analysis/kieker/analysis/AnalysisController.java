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

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;

import kieker.analysis.plugin.IAnalysisPlugin;
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
	 * this are the consumers for data that are coming into Kieker by readers (files or system under monitoring)
	 */
	private final Collection<IMonitoringRecordConsumerPlugin> consumers = new CopyOnWriteArrayList<IMonitoringRecordConsumerPlugin>();
	/** Contains all consumers which consume records of any type */
	private final Collection<IMonitoringRecordConsumerPlugin> anyTypeConsumers = new CopyOnWriteArrayList<IMonitoringRecordConsumerPlugin>();
	/** Contains mapping of record types to subscribed consumers */
	private final ConcurrentMap<Class<? extends IMonitoringRecord>, Collection<IMonitoringRecordConsumerPlugin>> specificTypeConsumers = new ConcurrentHashMap<Class<? extends IMonitoringRecord>, Collection<IMonitoringRecordConsumerPlugin>>();
	private final Collection<IAnalysisPlugin> plugins = new CopyOnWriteArrayList<IAnalysisPlugin>();

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
			for (final IAnalysisPlugin c : this.plugins) {
				if (!c.execute()) {
					AnalysisController.LOG.error("A plug-in's execute message failed");
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
				this.logReader.addRecordReceiver(new IMonitoringRecordReceiver() {

					/**
					 * Delegates the records provided by the reader to
					 * the registered record consumers
					 */
					@Override
					public boolean newMonitoringRecord(final IMonitoringRecord monitoringRecord) {
						// abort on consumer error
						return AnalysisController.this.deliverRecordToConsumers(monitoringRecord, true);
					}
				});
			}

			/**
			 * Start reading
			 */
			if (success) {
				// notify threads waiting for the initialization to be done
				this.initializationLatch.countDown();
				if (!this.logReader.read()) {
					AnalysisController.LOG.error("Calling execute() on logReader returned false");
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
				for (final IAnalysisPlugin c : this.plugins) {
					c.terminate(!success); // normal termination (w/o error)
				}
			} catch (final Exception e) { // NOCS // NOPMD
				AnalysisController.LOG.error("Error during termination: " + e.getMessage(), e);
			}
		}

		return success;
	}

	/**
	 * Initiates a termination of the analysis.
	 */
	public void terminate() {
		/*
		 * terminate the reader. After the reader has terminated, the run method()
		 * will terminate all plugins
		 */
		AnalysisController.LOG.info("Explicit termination of the analysis. Terminating the reader ...");
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
	private void addRecordConsumer(final IMonitoringRecordConsumerPlugin consumer) {
		this.consumers.add(consumer);
		final Collection<Class<? extends IMonitoringRecord>> recordTypeSubscriptionList = consumer.getRecordTypeSubscriptionList();
		if (recordTypeSubscriptionList == null) {
			this.anyTypeConsumers.add(consumer);
		} else {
			for (final Class<? extends IMonitoringRecord> recordType : recordTypeSubscriptionList) {
				Collection<IMonitoringRecordConsumerPlugin> cList = this.specificTypeConsumers.get(recordType);
				if (cList == null) {
					cList = new CopyOnWriteArrayList<IMonitoringRecordConsumerPlugin>(); // NOPMD (new in loops)
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
	public void registerPlugin(final IAnalysisPlugin plugin) {
		this.plugins.add(plugin);
		AnalysisController.LOG.debug("Registered plugin " + plugin);

		if (plugin instanceof IMonitoringRecordConsumerPlugin) {
			AnalysisController.LOG.debug("Plugin " + plugin + " also registered as record consumer");
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
	private final boolean deliverRecordToConsumers(final IMonitoringRecord monitoringRecord, final boolean abortOnConsumerError) {
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
		final Collection<IMonitoringRecordConsumerPlugin> cList = this.specificTypeConsumers.get(monitoringRecord.getClass());
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
