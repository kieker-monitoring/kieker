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

package kieker.test.tools.junit.traceAnalysis.util;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.flow.trace.Trace;
import kieker.common.record.flow.trace.concurrency.SplitEvent;
import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.record.flow.trace.operation.BeforeOperationEvent;
import kieker.common.record.flow.trace.operation.CallOperationEvent;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.test.common.junit.record.BookstoreOperationExecutionRecordFactory;
import kieker.tools.traceAnalysis.filter.flow.EventRecordTrace;
import kieker.tools.traceAnalysis.filter.traceReconstruction.InvalidTraceException;

/**
 * 
 * @author Andre van Hoorn, Holger Knoche
 * 
 */
public final class BookstoreEventRecordFactory {
	// private static final Log LOG = LogFactory.getLog(BookstoreEventRecordFactory.class);

	private BookstoreEventRecordFactory() {}

	public static final long TSTAMP_OFFSET_entry0_0__bookstore_searchBook = 0; // NOPMD NOCS (VariableNamingConventions)
	public static final long TSTAMP_OFFSET_call1_1__catalog_getBook = 1; // NOPMD NOCS (VariableNamingConventions)
	public static final long TSTAMP_OFFSET_entry1_1__catalog_getBook = 2; // NOPMD NOCS (VariableNamingConventions)
	public static final long TSTAMP_OFFSET_exit1_1__catalog_getBook = 3; // NOPMD NOCS (VariableNamingConventions)
	public static final long TSTAMP_OFFSET_call2_1__crm_getOrders = 4; // NOPMD NOCS (VariableNamingConventions)
	public static final long TSTAMP_OFFSET_entry2_1__crm_getOrders = 5; // NOPMD NOCS (VariableNamingConventions)
	public static final long TSTAMP_OFFSET_call3_2__catalog_getBook = 6; // NOPMD NOCS (VariableNamingConventions)
	public static final long TSTAMP_OFFSET_entry3_2__catalog_getBook = 7; // NOPMD NOCS (VariableNamingConventions)
	public static final long TSTAMP_OFFSET_exit3_2__catalog_getBook = 8; // NOPMD NOCS (VariableNamingConventions)
	public static final long TSTAMP_OFFSET_exit2_1__crm_getOrders = 8; // NOPMD NOCS (VariableNamingConventions)
	public static final long TSTAMP_OFFSET_exit0_0__bookstore_searchBook = 11; // NOPMD NOCS VariableNamingConventions)

	private static final String MSG_INVALID_TRACE = "Test invalid (creating invalid trace): ";

	/**
	 * Returns the "well-known" Bookstore trace as a list of {@link BeforeOperationEvent} and {@link AfterOperationEvent} events, ordered by its
	 * {@link kieker.common.record.flow.trace.AbstractTraceEvent#getOrderIndex()}es.
	 * 
	 * @param firstTimestamp
	 *            timestamp of the earliest event, incremented by 1 for each subsequent event
	 * @param traceId
	 * @return
	 */
	public static EventRecordTrace validSyncTraceBeforeAfterEvents(final long firstTimestamp, final long traceId, final String sessionId,
			final String hostname) {
		int curOrderIndex = 0;

		final BeforeOperationEvent entry0_0__bookstore_searchBook; // NOCS
		final BeforeOperationEvent entry1_1__catalog_getBook; // NOCS
		final AfterOperationEvent exit1_1__catalog_getBook; // NOCS
		final BeforeOperationEvent entry2_1__crm_getOrders; // NOCS
		final BeforeOperationEvent entry3_2__catalog_getBook; // NOCS
		final AfterOperationEvent exit3_2__catalog_getBook; // NOCS
		final AfterOperationEvent exit2_1__crm_getOrders; // NOCS
		final AfterOperationEvent exit0_0__bookstore_searchBook; // NOCS

		entry0_0__bookstore_searchBook =
				new BeforeOperationEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_entry0_0__bookstore_searchBook,
						traceId, curOrderIndex++, BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_BOOKSTORE_SEARCH_BOOK);
		entry1_1__catalog_getBook =
				new BeforeOperationEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_entry1_1__catalog_getBook,
						traceId, curOrderIndex++, BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_CATALOG_GET_BOOK);
		exit1_1__catalog_getBook =
				new AfterOperationEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_exit1_1__catalog_getBook,
						traceId, curOrderIndex++, BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_CATALOG_GET_BOOK);
		entry2_1__crm_getOrders =
				new BeforeOperationEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_entry2_1__crm_getOrders,
						traceId, curOrderIndex++, BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_CRM_GET_ORDERS);
		entry3_2__catalog_getBook =
				new BeforeOperationEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_entry3_2__catalog_getBook,
						traceId, curOrderIndex++, BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_CATALOG_GET_BOOK);
		exit3_2__catalog_getBook =
				new AfterOperationEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_exit3_2__catalog_getBook,
						traceId, curOrderIndex++, BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_CATALOG_GET_BOOK);
		exit2_1__crm_getOrders =
				new AfterOperationEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_exit2_1__crm_getOrders,
						traceId, curOrderIndex++, BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_CRM_GET_ORDERS);
		exit0_0__bookstore_searchBook =
				new AfterOperationEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_exit0_0__bookstore_searchBook,
						traceId, curOrderIndex++, BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_BOOKSTORE_SEARCH_BOOK);

		final EventRecordTrace retTrace = new EventRecordTrace(traceId, sessionId, hostname);

		try {
			retTrace.add(entry0_0__bookstore_searchBook);
			retTrace.add(entry1_1__catalog_getBook);
			retTrace.add(exit1_1__catalog_getBook);
			retTrace.add(entry2_1__crm_getOrders);
			retTrace.add(entry3_2__catalog_getBook);
			retTrace.add(exit3_2__catalog_getBook);
			retTrace.add(exit2_1__crm_getOrders);
			retTrace.add(exit0_0__bookstore_searchBook);
		} catch (final InvalidTraceException e) {
			Assert.fail(BookstoreEventRecordFactory.MSG_INVALID_TRACE + e.getMessage());
		}

		return retTrace;
	}

	/**
	 * Returns the "well-known" Bookstore trace as a list of {@link CallOperationEvent}, {@link BeforeOperationEvent} and {@link AfterOperationEvent} events, ordered
	 * by
	 * its {@link kieker.common.record.flow.trace.AbstractTraceEvent#getOrderIndex()}es.
	 * 
	 * @param firstTimestamp
	 *            timestamp of the earliest event, incremented by 1 for each subsequent event
	 * @param traceId
	 * @return
	 */
	public static EventRecordTrace validSyncTraceAdditionalCallEvents(final long firstTimestamp, final long traceId, final String sessionId,
			final String hostname) {
		int curOrderIndex = 0;

		final BeforeOperationEvent entry0_0__bookstore_searchBook; // NOCS
		final CallOperationEvent call1_1__catalog_getBook; // NOCS
		final BeforeOperationEvent entry1_1__catalog_getBook; // NOCS
		final AfterOperationEvent exit1_1__catalog_getBook; // NOCS
		final CallOperationEvent call2_1__crm_getOrders; // NOCS
		final BeforeOperationEvent entry2_1__crm_getOrders; // NOCS
		final CallOperationEvent call3_2__catalog_getBook; // NOCS
		final BeforeOperationEvent entry3_2__catalog_getBook; // NOCS
		final AfterOperationEvent exit3_2__catalog_getBook; // NOCS
		final AfterOperationEvent exit2_1__crm_getOrders; // NOCS
		final AfterOperationEvent exit0_0__bookstore_searchBook; // NOCS

		entry0_0__bookstore_searchBook =
				new BeforeOperationEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_entry0_0__bookstore_searchBook,
						traceId, curOrderIndex++, BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_BOOKSTORE_SEARCH_BOOK);
		call1_1__catalog_getBook =
				new CallOperationEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_call1_1__catalog_getBook,
						traceId, curOrderIndex++, BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_BOOKSTORE_SEARCH_BOOK,
						BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_CATALOG_GET_BOOK);
		entry1_1__catalog_getBook =
				new BeforeOperationEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_entry1_1__catalog_getBook,
						traceId, curOrderIndex++, BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_CATALOG_GET_BOOK);
		exit1_1__catalog_getBook =
				new AfterOperationEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_exit1_1__catalog_getBook,
						traceId, curOrderIndex++, BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_CATALOG_GET_BOOK);
		call2_1__crm_getOrders =
				new CallOperationEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_call2_1__crm_getOrders,
						traceId, curOrderIndex++,
						BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_BOOKSTORE_SEARCH_BOOK,
						BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_CRM_GET_ORDERS);
		entry2_1__crm_getOrders =
				new BeforeOperationEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_entry2_1__crm_getOrders,
						traceId, curOrderIndex++, BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_CRM_GET_ORDERS);
		call3_2__catalog_getBook =
				new CallOperationEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_call3_2__catalog_getBook,
						traceId, curOrderIndex++, BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_CRM_GET_ORDERS,
						BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_CATALOG_GET_BOOK);
		entry3_2__catalog_getBook =
				new BeforeOperationEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_entry3_2__catalog_getBook,
						traceId, curOrderIndex++, BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_CATALOG_GET_BOOK);
		exit3_2__catalog_getBook =
				new AfterOperationEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_exit3_2__catalog_getBook,
						traceId, curOrderIndex++, BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_CATALOG_GET_BOOK);
		exit2_1__crm_getOrders =
				new AfterOperationEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_exit2_1__crm_getOrders,
						traceId, curOrderIndex++, BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_CRM_GET_ORDERS);
		exit0_0__bookstore_searchBook = new AfterOperationEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_exit0_0__bookstore_searchBook,
				traceId, curOrderIndex++, BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_BOOKSTORE_SEARCH_BOOK);

		final EventRecordTrace retTrace =
				new EventRecordTrace(traceId, sessionId, hostname);

		try {
			retTrace.add(entry0_0__bookstore_searchBook);
			retTrace.add(call1_1__catalog_getBook);
			retTrace.add(entry1_1__catalog_getBook);
			retTrace.add(exit1_1__catalog_getBook);
			retTrace.add(call2_1__crm_getOrders);
			retTrace.add(entry2_1__crm_getOrders);
			retTrace.add(call3_2__catalog_getBook);
			retTrace.add(entry3_2__catalog_getBook);
			retTrace.add(exit3_2__catalog_getBook);
			retTrace.add(exit2_1__crm_getOrders);
			retTrace.add(exit0_0__bookstore_searchBook);
		} catch (final InvalidTraceException e) {
			Assert.fail(BookstoreEventRecordFactory.MSG_INVALID_TRACE + e.getMessage());
		}

		return retTrace;
	}

	/**
	 * Returns the "well-known" Bookstore trace as a list of {@link CallOperationEvent}, {@link BeforeOperationEvent} and {@link AfterOperationEvent} events, ordered
	 * by
	 * its {@link kieker.common.record.flow.trace.AbstractTraceEvent#getOrderIndex()}es. In this trace, <i>CRM.getOrders</i> is assumed not to be instrumented.
	 * 
	 * @param firstTimestamp
	 *            timestamp of the earliest event, incremented by 1 for each subsequent event
	 * @param traceId
	 * @return
	 */
	public static EventRecordTrace validSyncTraceAdditionalCallEventsGap(final long firstTimestamp, final long traceId, final String sessionId,
			final String hostname) {
		int curOrderIndex = 0;

		final BeforeOperationEvent entry0_0__bookstore_searchBook; // NOCS
		final CallOperationEvent call1_1__catalog_getBook; // NOCS
		final BeforeOperationEvent entry1_1__catalog_getBook; // NOCS
		final AfterOperationEvent exit1_1__catalog_getBook; // NOCS
		final CallOperationEvent call2_1__crm_getOrders; // NOCS
		// assumed to be uninstrumented: final BeforeOperationEvent entry2_1__crm_getOrders; // NOCS
		final CallOperationEvent call3_2__catalog_getBook; // NOCS
		final BeforeOperationEvent entry3_2__catalog_getBook; // NOCS
		final AfterOperationEvent exit3_2__catalog_getBook; // NOCS
		// assumed to be uninstrumented: final AfterOperationEvent exit2_1__crm_getOrders; // NOCS
		final AfterOperationEvent exit0_0__bookstore_searchBook; // NOCS

		entry0_0__bookstore_searchBook =
				new BeforeOperationEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_entry0_0__bookstore_searchBook,
						traceId, curOrderIndex++, BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_BOOKSTORE_SEARCH_BOOK);
		call1_1__catalog_getBook =
				new CallOperationEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_call1_1__catalog_getBook,
						traceId, curOrderIndex++,
						BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_BOOKSTORE_SEARCH_BOOK,
						BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_CATALOG_GET_BOOK);
		entry1_1__catalog_getBook =
				new BeforeOperationEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_entry1_1__catalog_getBook,
						traceId, curOrderIndex++, BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_CATALOG_GET_BOOK);
		exit1_1__catalog_getBook =
				new AfterOperationEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_exit1_1__catalog_getBook,
						traceId, curOrderIndex++, BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_CATALOG_GET_BOOK);
		call2_1__crm_getOrders =
				new CallOperationEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_call2_1__crm_getOrders,
						traceId, curOrderIndex++,
						BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_BOOKSTORE_SEARCH_BOOK,
						BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_CRM_GET_ORDERS);
		// assumed to be uninstrumented: entry2_1__crm_getOrders = new BeforeOperationEvent(curTime++, traceId, curOrderIndex++,
		// BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_CRM_GET_ORDERS);
		call3_2__catalog_getBook =
				new CallOperationEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_call3_2__catalog_getBook,
						traceId, curOrderIndex++,
						BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_CRM_GET_ORDERS,
						BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_CATALOG_GET_BOOK);
		entry3_2__catalog_getBook =
				new BeforeOperationEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_entry3_2__catalog_getBook,
						traceId, curOrderIndex++, BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_CATALOG_GET_BOOK);
		exit3_2__catalog_getBook =
				new AfterOperationEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_exit3_2__catalog_getBook,
						traceId, curOrderIndex++, BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_CATALOG_GET_BOOK);
		// assumed to be uninstrumented: exit2_1__crm_getOrders = new AfterOperationEvent(curTime++, traceId, curOrderIndex++,
		// BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_CRM_GET_ORDERS);

		exit0_0__bookstore_searchBook =
				new AfterOperationEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_exit0_0__bookstore_searchBook,
						traceId, curOrderIndex++, BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_BOOKSTORE_SEARCH_BOOK);

		final EventRecordTrace retTrace = new EventRecordTrace(traceId, sessionId, hostname);

		try {
			retTrace.add(entry0_0__bookstore_searchBook);
			retTrace.add(call1_1__catalog_getBook);
			retTrace.add(entry1_1__catalog_getBook);
			retTrace.add(exit1_1__catalog_getBook);
			retTrace.add(call2_1__crm_getOrders);
			// assumed to be uninstrumented: retTrace.add(entry2_1__crm_getOrders);
			retTrace.add(call3_2__catalog_getBook);
			retTrace.add(entry3_2__catalog_getBook);
			retTrace.add(exit3_2__catalog_getBook);
			// assumed to be uninstrumented: retTrace.add(exit2_1__crm_getOrders);
			retTrace.add(exit0_0__bookstore_searchBook);
		} catch (final InvalidTraceException e) {
			Assert.fail(BookstoreEventRecordFactory.MSG_INVALID_TRACE + e.getMessage());
		}

		return retTrace;
	}

	/**
	 * Returns a variant of the "well-known" Bookstore trace as a list of {@link CallOperationEvent}, {@link BeforeOperationEvent} and {@link AfterOperationEvent}
	 * events, ordered by its {@link kieker.common.record.flow.trace.AbstractTraceEvent#getOrderIndex()}es. In this trace, <i>CRM.getOrders</i> and
	 * <i>Catalog.getBook</i> are assumed not to be instrumented.
	 * 
	 * @param firstTimestamp
	 *            timestamp of the earliest event, incremented by 1 for each subsequent event
	 * @param traceId
	 * @return
	 */
	public static EventRecordTrace validSyncTraceSimpleEntryCallExit(final long firstTimestamp, final long traceId, final String sessionId,
			final String hostname) {
		int curOrderIndex = 0;

		final BeforeOperationEvent entry0_0__bookstore_searchBook; // NOCS
		final CallOperationEvent call1_1__catalog_getBook; // NOCS
		// assumed to be uninstrumented: final BeforeOperationEvent entry1_1__catalog_getBook; // NOCS
		// assumed to be uninstrumented: final AfterOperationEvent exit1_1__catalog_getBook; // NOCS
		// assumed to be uninstrumented: final CallOperationEvent call2_1__crm_getOrders; // NOCS
		// assumed to be uninstrumented: final BeforeOperationEvent entry2_1__crm_getOrders; // NOCS
		// assumed to be uninstrumented: final CallOperationEvent call3_2__catalog_getBook; // NOCS
		// assumed to be uninstrumented: final BeforeOperationEvent entry3_2__catalog_getBook; // NOCS
		// assumed to be uninstrumented: final AfterOperationEvent exit3_2__catalog_getBook; // NOCS
		// assumed to be uninstrumented: final AfterOperationEvent exit2_1__crm_getOrders; // NOCS
		final AfterOperationEvent exit0_0__bookstore_searchBook; // NOCS

		entry0_0__bookstore_searchBook =
				new BeforeOperationEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_entry0_0__bookstore_searchBook,
						traceId, curOrderIndex++, BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_BOOKSTORE_SEARCH_BOOK);
		call1_1__catalog_getBook =
				new CallOperationEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_call1_1__catalog_getBook,
						traceId, curOrderIndex++,
						BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_BOOKSTORE_SEARCH_BOOK,
						BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_CATALOG_GET_BOOK);
		exit0_0__bookstore_searchBook =
				new AfterOperationEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_exit0_0__bookstore_searchBook,
						traceId, curOrderIndex++, BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_BOOKSTORE_SEARCH_BOOK);

		final EventRecordTrace retTrace = new EventRecordTrace(traceId, sessionId, hostname);

		try {
			retTrace.add(entry0_0__bookstore_searchBook);
			retTrace.add(call1_1__catalog_getBook);
			retTrace.add(exit0_0__bookstore_searchBook);
		} catch (final InvalidTraceException e) {
			Assert.fail(BookstoreEventRecordFactory.MSG_INVALID_TRACE + e.getMessage());
		}

		return retTrace;
	}

	/**
	 * Returns a variant the of "well-known" Bookstore trace as a list of {@link CallOperationEvent}, {@link BeforeOperationEvent} and {@link AfterOperationEvent}
	 * events, ordered by its {@link kieker.common.record.flow.trace.AbstractTraceEvent#getOrderIndex()}es. In this trace, <i>CRM.getOrders</i> and
	 * <i>Catalog.getBook</i> are assumed not to be instrumented.
	 * 
	 * @param firstTimestamp
	 *            timestamp of the earliest event, incremented by 1 for each subsequent event
	 * @param traceId
	 * @return
	 */
	public static EventRecordTrace validSyncTraceSimpleEntryCallReturnCallCallExit(final long firstTimestamp, final long traceId, final String sessionId,
			final String hostname) {
		int curOrderIndex = 0;

		final BeforeOperationEvent entry0_0__bookstore_searchBook; // NOCS
		final CallOperationEvent call1_1__catalog_getBook; // NOCS
		// assumed to be uninstrumented: final BeforeOperationEvent entry1_1__catalog_getBook; // NOCS
		// assumed to be uninstrumented: final AfterOperationEvent exit1_1__catalog_getBook; // NOCS
		final CallOperationEvent call2_1__crm_getOrders; // NOCS
		final SplitEvent disturbEvent; // this caused to break the reconstruction once ...
		// assumed to be uninstrumented: final BeforeOperationEvent entry2_1__crm_getOrders; // NOCS
		// assumed to be uninstrumented: final CallOperationEvent call3_2__catalog_getBook; // NOCS
		// assumed to be uninstrumented: final BeforeOperationEvent entry3_2__catalog_getBook; // NOCS
		// assumed to be uninstrumented: final AfterOperationEvent exit3_2__catalog_getBook; // NOCS
		// assumed to be uninstrumented: final AfterOperationEvent exit2_1__crm_getOrders; // NOCS
		final AfterOperationEvent exit0_0__bookstore_searchBook; // NOCS

		entry0_0__bookstore_searchBook =
				new BeforeOperationEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_entry0_0__bookstore_searchBook,
						traceId, curOrderIndex++, BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_BOOKSTORE_SEARCH_BOOK);
		call1_1__catalog_getBook =
				new CallOperationEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_call1_1__catalog_getBook,
						traceId, curOrderIndex++,
						BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_BOOKSTORE_SEARCH_BOOK,
						BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_CATALOG_GET_BOOK);
		disturbEvent = new SplitEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_call3_2__catalog_getBook, traceId, curOrderIndex++);
		call2_1__crm_getOrders =
				new CallOperationEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_call2_1__crm_getOrders,
						/* note that we are using the timestamp of the omitted event here! */
						traceId, curOrderIndex++,
						BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_BOOKSTORE_SEARCH_BOOK,
						BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_CRM_GET_ORDERS);
		exit0_0__bookstore_searchBook =
				new AfterOperationEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_exit0_0__bookstore_searchBook,
						traceId, curOrderIndex++, BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_BOOKSTORE_SEARCH_BOOK);

		final EventRecordTrace retTrace = new EventRecordTrace(traceId, sessionId, hostname);

		try {
			retTrace.add(entry0_0__bookstore_searchBook);
			retTrace.add(call1_1__catalog_getBook);
			retTrace.add(disturbEvent);
			retTrace.add(call2_1__crm_getOrders);
			retTrace.add(exit0_0__bookstore_searchBook);
		} catch (final InvalidTraceException e) {
			Assert.fail(BookstoreEventRecordFactory.MSG_INVALID_TRACE + e.getMessage());
		}

		return retTrace;
	}

	/**
	 * Returns a variant of "well-known" Bookstore trace as a list of {@link CallOperationEvent}, {@link BeforeOperationEvent} and {@link AfterOperationEvent}
	 * events, ordered by its {@link kieker.common.record.flow.trace.AbstractTraceEvent#getOrderIndex()}es. In this trace, <i>Catalog.getBook</i> is assumed
	 * not to be instrumented.
	 * 
	 * @param firstTimestamp
	 *            timestamp of the earliest event, incremented by 1 for each subsequent event
	 * @param traceId
	 * @return
	 */
	public static EventRecordTrace validSyncTraceSimpleEntryCallCallExit(final long firstTimestamp, final long traceId, final String sessionId,
			final String hostname) {
		int curOrderIndex = 0;

		final BeforeOperationEvent entry0_0__bookstore_searchBook; // NOCS
		// assumed to be uninstrumented: final CallOperationEvent call1_1__catalog_getBook; // NOCS
		// assumed to be uninstrumented: final BeforeOperationEvent entry1_1__catalog_getBook; // NOCS
		// assumed to be uninstrumented: final AfterOperationEvent exit1_1__catalog_getBook; // NOCS
		final CallOperationEvent call2_1__crm_getOrders; // NOCS
		// assumed to be uninstrumented: final BeforeOperationEvent entry2_1__crm_getOrders; // NOCS
		final CallOperationEvent call3_2__catalog_getBook; // NOCS
		// assumed to be uninstrumented: final BeforeOperationEvent entry3_2__catalog_getBook; // NOCS
		// assumed to be uninstrumented: final AfterOperationEvent exit3_2__catalog_getBook; // NOCS
		// assumed to be uninstrumented: final AfterOperationEvent exit2_1__crm_getOrders; // NOCS
		final AfterOperationEvent exit0_0__bookstore_searchBook; // NOCS

		entry0_0__bookstore_searchBook =
				new BeforeOperationEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_entry0_0__bookstore_searchBook,
						traceId, curOrderIndex++, BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_BOOKSTORE_SEARCH_BOOK);
		call2_1__crm_getOrders =
				new CallOperationEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_call2_1__crm_getOrders,
						/* note that we are using the timestamp of the omitted event here! */
						traceId, curOrderIndex++,
						BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_BOOKSTORE_SEARCH_BOOK,
						BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_CRM_GET_ORDERS);
		call3_2__catalog_getBook =
				new CallOperationEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_call3_2__catalog_getBook,
						traceId, curOrderIndex++, BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_CRM_GET_ORDERS,
						BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_CATALOG_GET_BOOK);
		exit0_0__bookstore_searchBook =
				new AfterOperationEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_exit0_0__bookstore_searchBook,
						traceId, curOrderIndex++, BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_BOOKSTORE_SEARCH_BOOK);

		final EventRecordTrace retTrace = new EventRecordTrace(traceId, sessionId, hostname);

		try {
			retTrace.add(entry0_0__bookstore_searchBook);
			retTrace.add(call2_1__crm_getOrders);
			retTrace.add(call3_2__catalog_getBook);
			retTrace.add(exit0_0__bookstore_searchBook);
		} catch (final InvalidTraceException e) {
			Assert.fail(BookstoreEventRecordFactory.MSG_INVALID_TRACE + e.getMessage());
		}

		return retTrace;
	}

	public static void main(final String[] args) {
		final IMonitoringController ctrl = MonitoringController.getInstance();

		long firstTimestamp = 7676876;
		final long firstTimestampDelta = 1000;
		final String sessionId = "BwvCqdyhw2";
		final String hostname = "srv0";
		long traceId = 688434;

		final List<IMonitoringRecord> allRecords = new ArrayList<IMonitoringRecord>();

		final EventRecordTrace validSyncTraceBeforeAfterEvents =
				BookstoreEventRecordFactory.validSyncTraceBeforeAfterEvents(firstTimestamp, traceId, sessionId, hostname);
		allRecords.add(new Trace(traceId, traceId, sessionId, hostname, Trace.NO_PARENT_TRACEID, Trace.NO_PARENT_ORDER_INDEX));
		allRecords.addAll(validSyncTraceBeforeAfterEvents.eventList());
		firstTimestamp += firstTimestampDelta;
		traceId++;
		final EventRecordTrace validSyncTraceAdditionalCallEvents =
				BookstoreEventRecordFactory.validSyncTraceAdditionalCallEvents(firstTimestamp, traceId, sessionId, hostname);
		allRecords.add(new Trace(traceId, traceId, sessionId, hostname, Trace.NO_PARENT_TRACEID, Trace.NO_PARENT_ORDER_INDEX));
		allRecords.addAll(validSyncTraceAdditionalCallEvents.eventList());
		firstTimestamp += firstTimestampDelta;
		traceId++;
		final EventRecordTrace validSyncTraceAdditionalCallEventsGap =
				BookstoreEventRecordFactory.validSyncTraceAdditionalCallEventsGap(firstTimestamp, traceId, sessionId, hostname);
		allRecords.add(new Trace(traceId, traceId, sessionId, hostname, Trace.NO_PARENT_TRACEID, Trace.NO_PARENT_ORDER_INDEX));
		allRecords.addAll(validSyncTraceAdditionalCallEventsGap.eventList());

		// TODO: currently not all of the trace generation methods in this class are used

		for (final IMonitoringRecord r : allRecords) {
			ctrl.newMonitoringRecord(r);
		}

		// ctrl.terminateMonitoring();
	}
}
