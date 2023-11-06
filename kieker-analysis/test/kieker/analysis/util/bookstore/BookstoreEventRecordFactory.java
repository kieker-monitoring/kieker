/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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
package kieker.analysis.util.bookstore;

import kieker.analysis.stage.flow.TraceEventRecords;
import kieker.common.record.flow.trace.AbstractTraceEvent;
import kieker.common.record.flow.trace.TraceMetadata;
import kieker.common.record.flow.trace.concurrency.SplitEvent;
import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.record.flow.trace.operation.BeforeOperationEvent;
import kieker.common.record.flow.trace.operation.CallOperationEvent;

import kieker.test.common.util.record.BookstoreOperationExecutionRecordFactory;

/**
 * A starter method for this factory is implemented in kieker.test.tools.junit.traceAnalysis.util.BookstoreEventRecordFactoryStarter.
 *
 * @author Andre van Hoorn, Holger Knoche, Jan Waller
 *
 * @since 1.5
 */
public final class BookstoreEventRecordFactory {
	// private static final Log LOG = LogFactory.getLog(BookstoreEventRecordFactory.class);

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

	private BookstoreEventRecordFactory() {}

	/**
	 * Returns the "well-known" Bookstore trace as a list of {@link BeforeOperationEvent} and {@link AfterOperationEvent} events, ordered by its
	 * {@link kieker.common.record.flow.trace.AbstractTraceEvent#getOrderIndex()}es.
	 *
	 * @param firstTimestamp
	 *            timestamp of the earliest event, incremented by 1 for each subsequent event
	 * @param traceId
	 *            The id of the trace.
	 * @param sessionId
	 *            The session ID.
	 * @param hostname
	 *            The name of the host to be used for the trace.
	 * @return A Bookstore trace.
	 */
	public static TraceEventRecords validSyncTraceBeforeAfterEvents(final long firstTimestamp, final long traceId, final String sessionId,
			final String hostname) {
		int curOrderIndex = 0;

		final BeforeOperationEvent entry0_0__bookstore_searchBook; // NOCS NOPMD
		final BeforeOperationEvent entry1_1__catalog_getBook; // NOCS NOPMD
		final AfterOperationEvent exit1_1__catalog_getBook; // NOCS NOPMD
		final BeforeOperationEvent entry2_1__crm_getOrders; // NOCS NOPMD
		final BeforeOperationEvent entry3_2__catalog_getBook; // NOCS NOPMD
		final AfterOperationEvent exit3_2__catalog_getBook; // NOCS NOPMD
		final AfterOperationEvent exit2_1__crm_getOrders; // NOCS NOPMD
		final AfterOperationEvent exit0_0__bookstore_searchBook; // NOCS NOPMD

		entry0_0__bookstore_searchBook = new BeforeOperationEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_entry0_0__bookstore_searchBook,
				traceId, curOrderIndex++,
				BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_BOOKSTORE_SEARCH_BOOK,
				BookstoreOperationExecutionRecordFactory.FQ_CLASS_BOOKSTORE);
		entry1_1__catalog_getBook = new BeforeOperationEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_entry1_1__catalog_getBook,
				traceId, curOrderIndex++,
				BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_CATALOG_GET_BOOK,
				BookstoreOperationExecutionRecordFactory.FQ_CLASS_CATALOG);
		exit1_1__catalog_getBook = new AfterOperationEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_exit1_1__catalog_getBook,
				traceId, curOrderIndex++,
				BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_CATALOG_GET_BOOK,
				BookstoreOperationExecutionRecordFactory.FQ_CLASS_CATALOG);
		entry2_1__crm_getOrders = new BeforeOperationEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_entry2_1__crm_getOrders,
				traceId, curOrderIndex++,
				BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_CRM_GET_ORDERS,
				BookstoreOperationExecutionRecordFactory.FQ_CLASS_CRM);
		entry3_2__catalog_getBook = new BeforeOperationEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_entry3_2__catalog_getBook,
				traceId, curOrderIndex++,
				BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_CATALOG_GET_BOOK,
				BookstoreOperationExecutionRecordFactory.FQ_CLASS_CATALOG);
		exit3_2__catalog_getBook = new AfterOperationEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_exit3_2__catalog_getBook,
				traceId, curOrderIndex++,
				BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_CATALOG_GET_BOOK,
				BookstoreOperationExecutionRecordFactory.FQ_CLASS_CATALOG);
		exit2_1__crm_getOrders = new AfterOperationEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_exit2_1__crm_getOrders,
				traceId, curOrderIndex++,
				BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_CRM_GET_ORDERS,
				BookstoreOperationExecutionRecordFactory.FQ_CLASS_CRM);
		exit0_0__bookstore_searchBook = new AfterOperationEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_exit0_0__bookstore_searchBook,
				traceId, curOrderIndex++,
				BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_BOOKSTORE_SEARCH_BOOK,
				BookstoreOperationExecutionRecordFactory.FQ_CLASS_BOOKSTORE);

		final TraceMetadata trace = new TraceMetadata(traceId, -1, sessionId, hostname, -1, -1);
		final AbstractTraceEvent[] events = {
			entry0_0__bookstore_searchBook,
			entry1_1__catalog_getBook,
			exit1_1__catalog_getBook,
			entry2_1__crm_getOrders,
			entry3_2__catalog_getBook,
			exit3_2__catalog_getBook,
			exit2_1__crm_getOrders,
			exit0_0__bookstore_searchBook,
		};
		return new TraceEventRecords(trace, events);
	}

	/**
	 * Returns the "well-known" Bookstore trace as a list of {@link CallOperationEvent}, {@link BeforeOperationEvent} and {@link AfterOperationEvent} events, ordered
	 * by
	 * its {@link kieker.common.record.flow.trace.AbstractTraceEvent#getOrderIndex()}es.
	 *
	 * @param firstTimestamp
	 *            timestamp of the earliest event, incremented by 1 for each subsequent event
	 * @param traceId
	 *            The trace ID.
	 * @param sessionId
	 *            The session ID.
	 * @param hostname
	 *            The name of the host.
	 * @return A Bookstore trace.
	 */
	public static TraceEventRecords validSyncTraceAdditionalCallEvents(final long firstTimestamp, final long traceId, final String sessionId,
			final String hostname) {
		int curOrderIndex = 0;

		final BeforeOperationEvent entry0_0__bookstore_searchBook; // NOCS NOPMD
		final CallOperationEvent call1_1__catalog_getBook; // NOCS NOPMD
		final BeforeOperationEvent entry1_1__catalog_getBook; // NOCS NOPMD
		final AfterOperationEvent exit1_1__catalog_getBook; // NOCS NOPMD
		final CallOperationEvent call2_1__crm_getOrders; // NOCS NOPMD
		final BeforeOperationEvent entry2_1__crm_getOrders; // NOCS NOPMD
		final CallOperationEvent call3_2__catalog_getBook; // NOCS NOPMD
		final BeforeOperationEvent entry3_2__catalog_getBook; // NOCS NOPMD
		final AfterOperationEvent exit3_2__catalog_getBook; // NOCS NOPMD
		final AfterOperationEvent exit2_1__crm_getOrders; // NOCS NOPMD
		final AfterOperationEvent exit0_0__bookstore_searchBook; // NOCS NOPMD

		entry0_0__bookstore_searchBook = new BeforeOperationEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_entry0_0__bookstore_searchBook,
				traceId, curOrderIndex++,
				BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_BOOKSTORE_SEARCH_BOOK,
				BookstoreOperationExecutionRecordFactory.FQ_CLASS_BOOKSTORE);
		call1_1__catalog_getBook = new CallOperationEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_call1_1__catalog_getBook,
				traceId, curOrderIndex++,
				BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_BOOKSTORE_SEARCH_BOOK,
				BookstoreOperationExecutionRecordFactory.FQ_CLASS_BOOKSTORE,
				BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_CATALOG_GET_BOOK,
				BookstoreOperationExecutionRecordFactory.FQ_CLASS_CATALOG);
		entry1_1__catalog_getBook = new BeforeOperationEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_entry1_1__catalog_getBook,
				traceId, curOrderIndex++,
				BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_CATALOG_GET_BOOK,
				BookstoreOperationExecutionRecordFactory.FQ_CLASS_CATALOG);
		exit1_1__catalog_getBook = new AfterOperationEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_exit1_1__catalog_getBook,
				traceId, curOrderIndex++,
				BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_CATALOG_GET_BOOK,
				BookstoreOperationExecutionRecordFactory.FQ_CLASS_CATALOG);
		call2_1__crm_getOrders = new CallOperationEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_call2_1__crm_getOrders,
				traceId, curOrderIndex++,
				BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_BOOKSTORE_SEARCH_BOOK,
				BookstoreOperationExecutionRecordFactory.FQ_CLASS_BOOKSTORE,
				BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_CRM_GET_ORDERS,
				BookstoreOperationExecutionRecordFactory.FQ_CLASS_CRM);
		entry2_1__crm_getOrders = new BeforeOperationEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_entry2_1__crm_getOrders,
				traceId, curOrderIndex++,
				BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_CRM_GET_ORDERS,
				BookstoreOperationExecutionRecordFactory.FQ_CLASS_CRM);
		call3_2__catalog_getBook = new CallOperationEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_call3_2__catalog_getBook,
				traceId, curOrderIndex++,
				BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_CRM_GET_ORDERS,
				BookstoreOperationExecutionRecordFactory.FQ_CLASS_CRM,
				BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_CATALOG_GET_BOOK,
				BookstoreOperationExecutionRecordFactory.FQ_CLASS_CATALOG);
		entry3_2__catalog_getBook = new BeforeOperationEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_entry3_2__catalog_getBook,
				traceId, curOrderIndex++,
				BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_CATALOG_GET_BOOK,
				BookstoreOperationExecutionRecordFactory.FQ_CLASS_CATALOG);
		exit3_2__catalog_getBook = new AfterOperationEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_exit3_2__catalog_getBook,
				traceId, curOrderIndex++,
				BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_CATALOG_GET_BOOK,
				BookstoreOperationExecutionRecordFactory.FQ_CLASS_CATALOG);
		exit2_1__crm_getOrders = new AfterOperationEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_exit2_1__crm_getOrders,
				traceId, curOrderIndex++,
				BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_CRM_GET_ORDERS,
				BookstoreOperationExecutionRecordFactory.FQ_CLASS_CRM);
		exit0_0__bookstore_searchBook = new AfterOperationEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_exit0_0__bookstore_searchBook,
				traceId, curOrderIndex++,
				BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_BOOKSTORE_SEARCH_BOOK,
				BookstoreOperationExecutionRecordFactory.FQ_CLASS_BOOKSTORE);

		final TraceMetadata trace = new TraceMetadata(traceId, -1, sessionId, hostname, -1, -1);
		final AbstractTraceEvent[] events = {
			entry0_0__bookstore_searchBook,
			call1_1__catalog_getBook,
			entry1_1__catalog_getBook,
			exit1_1__catalog_getBook,
			call2_1__crm_getOrders,
			entry2_1__crm_getOrders,
			call3_2__catalog_getBook,
			entry3_2__catalog_getBook,
			exit3_2__catalog_getBook,
			exit2_1__crm_getOrders,
			exit0_0__bookstore_searchBook,
		};
		return new TraceEventRecords(trace, events);
	}

	/**
	 * Returns the "well-known" Bookstore trace as a list of {@link CallOperationEvent}, {@link BeforeOperationEvent} and {@link AfterOperationEvent} events, ordered
	 * by
	 * its {@link kieker.common.record.flow.trace.AbstractTraceEvent#getOrderIndex()}es. In this trace, <i>CRM.getOrders</i> is assumed not to be instrumented.
	 *
	 * @param firstTimestamp
	 *            timestamp of the earliest event, incremented by 1 for each subsequent event
	 * @param traceId
	 *            The trace ID.
	 * @param sessionId
	 *            The session ID.
	 * @param hostname
	 *            The name of the host.
	 *
	 * @return A Bookstore trace.
	 */
	public static TraceEventRecords validSyncTraceAdditionalCallEventsGap(final long firstTimestamp, final long traceId, final String sessionId,
			final String hostname) {
		int curOrderIndex = 0;

		final BeforeOperationEvent entry0_0__bookstore_searchBook; // NOCS NOPMD
		final CallOperationEvent call1_1__catalog_getBook; // NOCS NOPMD
		final BeforeOperationEvent entry1_1__catalog_getBook; // NOCS NOPMD
		final AfterOperationEvent exit1_1__catalog_getBook; // NOCS NOPMD
		final CallOperationEvent call2_1__crm_getOrders; // NOCS NOPMD
		// assumed to be uninstrumented: final BeforeOperationEvent entry2_1__crm_getOrders; // NOCS
		final CallOperationEvent call3_2__catalog_getBook; // NOCS NOPMD
		final BeforeOperationEvent entry3_2__catalog_getBook; // NOCS NOPMD
		final AfterOperationEvent exit3_2__catalog_getBook; // NOCS NOPMD
		// assumed to be uninstrumented: final AfterOperationEvent exit2_1__crm_getOrders; // NOCS
		final AfterOperationEvent exit0_0__bookstore_searchBook; // NOCS NOPMD

		entry0_0__bookstore_searchBook = new BeforeOperationEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_entry0_0__bookstore_searchBook,
				traceId, curOrderIndex++,
				BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_BOOKSTORE_SEARCH_BOOK,
				BookstoreOperationExecutionRecordFactory.FQ_CLASS_BOOKSTORE);
		call1_1__catalog_getBook = new CallOperationEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_call1_1__catalog_getBook,
				traceId, curOrderIndex++,
				BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_BOOKSTORE_SEARCH_BOOK,
				BookstoreOperationExecutionRecordFactory.FQ_CLASS_BOOKSTORE,
				BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_CATALOG_GET_BOOK,
				BookstoreOperationExecutionRecordFactory.FQ_CLASS_CATALOG);
		entry1_1__catalog_getBook = new BeforeOperationEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_entry1_1__catalog_getBook,
				traceId, curOrderIndex++,
				BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_CATALOG_GET_BOOK,
				BookstoreOperationExecutionRecordFactory.FQ_CLASS_CATALOG);
		exit1_1__catalog_getBook = new AfterOperationEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_exit1_1__catalog_getBook,
				traceId, curOrderIndex++,
				BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_CATALOG_GET_BOOK,
				BookstoreOperationExecutionRecordFactory.FQ_CLASS_CATALOG);
		call2_1__crm_getOrders = new CallOperationEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_call2_1__crm_getOrders,
				traceId, curOrderIndex++,
				BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_BOOKSTORE_SEARCH_BOOK,
				BookstoreOperationExecutionRecordFactory.FQ_CLASS_BOOKSTORE,
				BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_CRM_GET_ORDERS,
				BookstoreOperationExecutionRecordFactory.FQ_CLASS_CRM);
		// assumed to be uninstrumented: entry2_1__crm_getOrders = new BeforeOperationEvent(curTime++, traceId, curOrderIndex++,
		// BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_CRM_GET_ORDERS,
		// BookstoreOperationExecutionRecordFactory.FQ_CLASS_CRM);
		call3_2__catalog_getBook = new CallOperationEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_call3_2__catalog_getBook,
				traceId, curOrderIndex++,
				BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_CRM_GET_ORDERS,
				BookstoreOperationExecutionRecordFactory.FQ_CLASS_CRM,
				BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_CATALOG_GET_BOOK,
				BookstoreOperationExecutionRecordFactory.FQ_CLASS_CATALOG);
		entry3_2__catalog_getBook = new BeforeOperationEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_entry3_2__catalog_getBook,
				traceId, curOrderIndex++,
				BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_CATALOG_GET_BOOK,
				BookstoreOperationExecutionRecordFactory.FQ_CLASS_CATALOG);
		exit3_2__catalog_getBook = new AfterOperationEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_exit3_2__catalog_getBook,
				traceId, curOrderIndex++,
				BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_CATALOG_GET_BOOK,
				BookstoreOperationExecutionRecordFactory.FQ_CLASS_CATALOG);
		// assumed to be uninstrumented: exit2_1__crm_getOrders = new AfterOperationEvent(curTime++, traceId, curOrderIndex++,
		// BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_CRM_GET_ORDERS,
		// BookstoreOperationExecutionRecordFactory.FQ_CLASS_CRM);

		exit0_0__bookstore_searchBook = new AfterOperationEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_exit0_0__bookstore_searchBook,
				traceId, curOrderIndex++,
				BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_BOOKSTORE_SEARCH_BOOK,
				BookstoreOperationExecutionRecordFactory.FQ_CLASS_BOOKSTORE);

		final TraceMetadata trace = new TraceMetadata(traceId, -1, sessionId, hostname, -1, -1);
		final AbstractTraceEvent[] events = {
			entry0_0__bookstore_searchBook,
			call1_1__catalog_getBook,
			entry1_1__catalog_getBook,
			exit1_1__catalog_getBook,
			call2_1__crm_getOrders,
			// entry2_1__crm_getOrders,
			call3_2__catalog_getBook,
			entry3_2__catalog_getBook,
			exit3_2__catalog_getBook,
			// exit2_1__crm_getOrders,
			exit0_0__bookstore_searchBook,
		};
		return new TraceEventRecords(trace, events);
	}

	/**
	 * Returns a variant of the "well-known" Bookstore trace as a list of {@link CallOperationEvent}, {@link BeforeOperationEvent} and {@link AfterOperationEvent}
	 * events, ordered by its {@link kieker.common.record.flow.trace.AbstractTraceEvent#getOrderIndex()}es. In this trace, <i>CRM.getOrders</i> and
	 * <i>Catalog.getBook</i> are assumed not to be instrumented.
	 *
	 * @param firstTimestamp
	 *            timestamp of the earliest event, incremented by 1 for each subsequent event
	 * @param traceId
	 *            The trace ID.
	 * @param sessionId
	 *            The session ID.
	 * @param hostname
	 *            The name of the host.
	 *
	 * @return A Bookstore trace.
	 */
	public static TraceEventRecords validSyncTraceSimpleEntryCallExit(final long firstTimestamp, final long traceId, final String sessionId,
			final String hostname) {
		int curOrderIndex = 0;

		final BeforeOperationEvent entry0_0__bookstore_searchBook; // NOCS NOPMD
		final CallOperationEvent call1_1__catalog_getBook; // NOCS NOPMD
		// assumed to be uninstrumented: final BeforeOperationEvent entry1_1__catalog_getBook; // NOCS
		// assumed to be uninstrumented: final AfterOperationEvent exit1_1__catalog_getBook; // NOCS
		// assumed to be uninstrumented: final CallOperationEvent call2_1__crm_getOrders; // NOCS
		// assumed to be uninstrumented: final BeforeOperationEvent entry2_1__crm_getOrders; // NOCS
		// assumed to be uninstrumented: final CallOperationEvent call3_2__catalog_getBook; // NOCS
		// assumed to be uninstrumented: final BeforeOperationEvent entry3_2__catalog_getBook; // NOCS
		// assumed to be uninstrumented: final AfterOperationEvent exit3_2__catalog_getBook; // NOCS
		// assumed to be uninstrumented: final AfterOperationEvent exit2_1__crm_getOrders; // NOCS
		final AfterOperationEvent exit0_0__bookstore_searchBook; // NOCS NOPMD

		entry0_0__bookstore_searchBook = new BeforeOperationEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_entry0_0__bookstore_searchBook,
				traceId, curOrderIndex++,
				BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_BOOKSTORE_SEARCH_BOOK,
				BookstoreOperationExecutionRecordFactory.FQ_CLASS_BOOKSTORE);
		call1_1__catalog_getBook = new CallOperationEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_call1_1__catalog_getBook,
				traceId, curOrderIndex++,
				BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_BOOKSTORE_SEARCH_BOOK,
				BookstoreOperationExecutionRecordFactory.FQ_CLASS_BOOKSTORE,
				BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_CATALOG_GET_BOOK,
				BookstoreOperationExecutionRecordFactory.FQ_CLASS_CATALOG);
		exit0_0__bookstore_searchBook = new AfterOperationEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_exit0_0__bookstore_searchBook,
				traceId, curOrderIndex++,
				BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_BOOKSTORE_SEARCH_BOOK,
				BookstoreOperationExecutionRecordFactory.FQ_CLASS_BOOKSTORE);

		final TraceMetadata trace = new TraceMetadata(traceId, -1, sessionId, hostname, -1, -1);
		final AbstractTraceEvent[] events = {
			entry0_0__bookstore_searchBook,
			call1_1__catalog_getBook,
			exit0_0__bookstore_searchBook,
		};
		return new TraceEventRecords(trace, events);
	}

	/**
	 * Returns a variant the of "well-known" Bookstore trace as a list of {@link CallOperationEvent}, {@link BeforeOperationEvent} and {@link AfterOperationEvent}
	 * events, ordered by its {@link kieker.common.record.flow.trace.AbstractTraceEvent#getOrderIndex()}es. In this trace, <i>CRM.getOrders</i> and
	 * <i>Catalog.getBook</i> are assumed not to be instrumented.
	 *
	 * @param firstTimestamp
	 *            timestamp of the earliest event, incremented by 1 for each subsequent event
	 * @param traceId
	 *            The ID of the trace.
	 * @param sessionId
	 *            The session ID.
	 * @param hostname
	 *            The name of the host for the trace.
	 *
	 * @return A Bookstore trace.
	 */
	public static TraceEventRecords validSyncTraceSimpleEntryCallReturnCallCallExit(final long firstTimestamp, final long traceId, final String sessionId,
			final String hostname) {
		int curOrderIndex = 0;

		final BeforeOperationEvent entry0_0__bookstore_searchBook; // NOCS NOPMD
		final CallOperationEvent call1_1__catalog_getBook; // NOCS NOPMD
		// assumed to be uninstrumented: final BeforeOperationEvent entry1_1__catalog_getBook; // NOCS
		// assumed to be uninstrumented: final AfterOperationEvent exit1_1__catalog_getBook; // NOCS
		final CallOperationEvent call2_1__crm_getOrders; // NOCS NOPMD
		final SplitEvent disturbEvent; // this caused to break the reconstruction once ...
		// assumed to be uninstrumented: final BeforeOperationEvent entry2_1__crm_getOrders; // NOCS
		// assumed to be uninstrumented: final CallOperationEvent call3_2__catalog_getBook; // NOCS
		// assumed to be uninstrumented: final BeforeOperationEvent entry3_2__catalog_getBook; // NOCS
		// assumed to be uninstrumented: final AfterOperationEvent exit3_2__catalog_getBook; // NOCS
		// assumed to be uninstrumented: final AfterOperationEvent exit2_1__crm_getOrders; // NOCS
		final AfterOperationEvent exit0_0__bookstore_searchBook; // NOCS

		entry0_0__bookstore_searchBook = new BeforeOperationEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_entry0_0__bookstore_searchBook,
				traceId, curOrderIndex++,
				BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_BOOKSTORE_SEARCH_BOOK,
				BookstoreOperationExecutionRecordFactory.FQ_CLASS_BOOKSTORE);
		call1_1__catalog_getBook = new CallOperationEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_call1_1__catalog_getBook,
				traceId, curOrderIndex++,
				BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_BOOKSTORE_SEARCH_BOOK,
				BookstoreOperationExecutionRecordFactory.FQ_CLASS_BOOKSTORE,
				BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_CATALOG_GET_BOOK,
				BookstoreOperationExecutionRecordFactory.FQ_CLASS_CATALOG);
		disturbEvent = new SplitEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_call3_2__catalog_getBook, traceId, curOrderIndex++);
		call2_1__crm_getOrders = new CallOperationEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_call2_1__crm_getOrders,
				// note that we are using the timestamp of the omitted event here!
				traceId, curOrderIndex++,
				BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_BOOKSTORE_SEARCH_BOOK,
				BookstoreOperationExecutionRecordFactory.FQ_CLASS_BOOKSTORE,
				BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_CRM_GET_ORDERS,
				BookstoreOperationExecutionRecordFactory.FQ_CLASS_CRM);
		exit0_0__bookstore_searchBook = new AfterOperationEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_exit0_0__bookstore_searchBook,
				traceId, curOrderIndex++,
				BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_BOOKSTORE_SEARCH_BOOK,
				BookstoreOperationExecutionRecordFactory.FQ_CLASS_BOOKSTORE);

		final TraceMetadata trace = new TraceMetadata(traceId, -1, sessionId, hostname, -1, -1);
		final AbstractTraceEvent[] events = {
			entry0_0__bookstore_searchBook,
			call1_1__catalog_getBook,
			disturbEvent,
			call2_1__crm_getOrders,
			exit0_0__bookstore_searchBook,
		};
		return new TraceEventRecords(trace, events);
	}

	/**
	 * Returns a variant of "well-known" Bookstore trace as a list of {@link CallOperationEvent}, {@link BeforeOperationEvent} and {@link AfterOperationEvent}
	 * events, ordered by its {@link kieker.common.record.flow.trace.AbstractTraceEvent#getOrderIndex()}es. In this trace, <i>Catalog.getBook</i> is assumed
	 * not to be instrumented.
	 *
	 * @param firstTimestamp
	 *            timestamp of the earliest event, incremented by 1 for each subsequent event
	 * @param traceId
	 *            The ID of the trace.
	 * @param sessionId
	 *            The session ID.
	 * @param hostname
	 *            The name of the host for the trace.
	 *
	 * @return A Bookstore trace.
	 */
	public static TraceEventRecords validSyncTraceSimpleEntryCallCallExit(final long firstTimestamp, final long traceId, final String sessionId,
			final String hostname) {
		int curOrderIndex = 0;

		final BeforeOperationEvent entry0_0__bookstore_searchBook; // NOCS NOPMD
		// assumed to be uninstrumented: final CallOperationEvent call1_1__catalog_getBook; // NOCS
		// assumed to be uninstrumented: final BeforeOperationEvent entry1_1__catalog_getBook; // NOCS
		// assumed to be uninstrumented: final AfterOperationEvent exit1_1__catalog_getBook; // NOCS
		final CallOperationEvent call2_1__crm_getOrders; // NOCS NOPMD
		// assumed to be uninstrumented: final BeforeOperationEvent entry2_1__crm_getOrders; // NOCS
		final CallOperationEvent call3_2__catalog_getBook; // NOCS NOPMD
		// assumed to be uninstrumented: final BeforeOperationEvent entry3_2__catalog_getBook; // NOCS
		// assumed to be uninstrumented: final AfterOperationEvent exit3_2__catalog_getBook; // NOCS
		// assumed to be uninstrumented: final AfterOperationEvent exit2_1__crm_getOrders; // NOCS
		final AfterOperationEvent exit0_0__bookstore_searchBook; // NOCS NOPMD

		entry0_0__bookstore_searchBook = new BeforeOperationEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_entry0_0__bookstore_searchBook,
				traceId, curOrderIndex++,
				BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_BOOKSTORE_SEARCH_BOOK,
				BookstoreOperationExecutionRecordFactory.FQ_CLASS_BOOKSTORE);
		call2_1__crm_getOrders = new CallOperationEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_call2_1__crm_getOrders,
				// note that we are using the timestamp of the omitted event here!
				traceId, curOrderIndex++,
				BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_BOOKSTORE_SEARCH_BOOK,
				BookstoreOperationExecutionRecordFactory.FQ_CLASS_BOOKSTORE,
				BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_CRM_GET_ORDERS,
				BookstoreOperationExecutionRecordFactory.FQ_CLASS_CRM);
		call3_2__catalog_getBook = new CallOperationEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_call3_2__catalog_getBook,
				traceId, curOrderIndex++,
				BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_CRM_GET_ORDERS,
				BookstoreOperationExecutionRecordFactory.FQ_CLASS_CRM,
				BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_CATALOG_GET_BOOK,
				BookstoreOperationExecutionRecordFactory.FQ_CLASS_CATALOG);
		exit0_0__bookstore_searchBook = new AfterOperationEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_exit0_0__bookstore_searchBook,
				traceId, curOrderIndex++,
				BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_BOOKSTORE_SEARCH_BOOK,
				BookstoreOperationExecutionRecordFactory.FQ_CLASS_BOOKSTORE);

		final TraceMetadata trace = new TraceMetadata(traceId, -1, sessionId, hostname, -1, -1);
		final AbstractTraceEvent[] events = {
			entry0_0__bookstore_searchBook,
			call2_1__crm_getOrders,
			call3_2__catalog_getBook,
			exit0_0__bookstore_searchBook,
		};
		return new TraceEventRecords(trace, events);
	}

	/**
	 * Valid synce trace simple call call.
	 *
	 * @param firstTimestamp
	 *            first time stampe
	 * @param traceId
	 *            trace id
	 * @param sessionId
	 *            session id
	 * @param hostname
	 *            hostname
	 * @return returns a set of trace events contained within TraceEventRecords
	 */
	public static TraceEventRecords validSyncTraceSimpleCallCall(final long firstTimestamp, final long traceId, final String sessionId, final String hostname) {
		int curOrderIndex = -1;

		// assumed to be uninstrumented: final BeforeOperationEvent entry0_0__bookstore_searchBook; // NOCS
		// assumed to be uninstrumented: final CallOperationEvent call1_1__catalog_getBook; // NOCS
		// assumed to be uninstrumented: final BeforeOperationEvent entry1_1__catalog_getBook; // NOCS
		// assumed to be uninstrumented: final AfterOperationEvent exit1_1__catalog_getBook; // NOCS
		final CallOperationEvent call2_1__crm_getOrders; // NOCS NOPMD
		// assumed to be uninstrumented: final BeforeOperationEvent entry2_1__crm_getOrders; // NOCS
		final CallOperationEvent call3_2__catalog_getBook; // NOCS NOPMD
		// assumed to be uninstrumented: final BeforeOperationEvent entry3_2__catalog_getBook; // NOCS
		// assumed to be uninstrumented: final AfterOperationEvent exit3_2__catalog_getBook; // NOCS
		// assumed to be uninstrumented: final AfterOperationEvent exit2_1__crm_getOrders; // NOCS
		// assumed to be uninstrumented: final AfterOperationEvent exit0_0__bookstore_searchBook; // NOCS

		call2_1__crm_getOrders = new CallOperationEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_call2_1__crm_getOrders,
				// note that we are using the timestamp of the omitted event here!
				traceId, ++curOrderIndex,
				BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_BOOKSTORE_SEARCH_BOOK,
				BookstoreOperationExecutionRecordFactory.FQ_CLASS_BOOKSTORE,
				BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_CRM_GET_ORDERS,
				BookstoreOperationExecutionRecordFactory.FQ_CLASS_CRM);
		call3_2__catalog_getBook = new CallOperationEvent(firstTimestamp + BookstoreEventRecordFactory.TSTAMP_OFFSET_call3_2__catalog_getBook,
				traceId, ++curOrderIndex,
				BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_CRM_GET_ORDERS,
				BookstoreOperationExecutionRecordFactory.FQ_CLASS_CRM,
				BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_CATALOG_GET_BOOK,
				BookstoreOperationExecutionRecordFactory.FQ_CLASS_CATALOG);

		final TraceMetadata trace = new TraceMetadata(traceId, -1, sessionId, hostname, -1, -1);
		final AbstractTraceEvent[] events = {
			call2_1__crm_getOrders,
			call3_2__catalog_getBook,
		};
		return new TraceEventRecords(trace, events);
	}
}
