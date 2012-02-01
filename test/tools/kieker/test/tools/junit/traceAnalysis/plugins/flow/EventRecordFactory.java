package kieker.test.tools.junit.traceAnalysis.plugins.flow;

import java.util.ArrayList;
import java.util.List;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.flow.AfterOperationEvent;
import kieker.common.record.flow.BeforeOperationEvent;
import kieker.common.record.flow.OperationCallEvent;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;

public class EventRecordFactory {
	private static final Log LOG = LogFactory.getLog(EventRecordFactory.class);

	private static final String FQ_OP_BOOKSTORE_SEARCH_BOOK = "Bookstore.searchBook";
	private static final String FQ_OP_CATALOG_GET_BOOK = "Catalog.getBook";
	private static final String FQ_OP_CRM_GET_ORDERS = "CRM.getOrders";

	private static long curTime = 14444446;
	private static long traceId = 87786;

	/**
	 * 
	 * @return
	 */
	public static List<IMonitoringRecord> validSyncTraceBeforeAfterEvents() {
		int curOrderIndex = 0;
		EventRecordFactory.traceId++;

		final BeforeOperationEvent entry0_0__bookstore_searchBook; // NOCS
		final BeforeOperationEvent entry1_1__catalog_getBook; // NOCS
		final AfterOperationEvent exit1_1__catalog_getBook; // NOCS
		final BeforeOperationEvent entry2_1__crm_getOrders; // NOCS
		final BeforeOperationEvent entry3_2__catalog_getBook; // NOCS
		final AfterOperationEvent exit3_2__catalog_getBook; // NOCS
		final AfterOperationEvent exit2_1__crm_getOrders; // NOCS
		final AfterOperationEvent exit0_0__bookstore_searchBook; // NOCS

		entry0_0__bookstore_searchBook = new BeforeOperationEvent(EventRecordFactory.curTime++, EventRecordFactory.traceId, curOrderIndex++,
				EventRecordFactory.FQ_OP_BOOKSTORE_SEARCH_BOOK);
		entry1_1__catalog_getBook = new BeforeOperationEvent(EventRecordFactory.curTime++, EventRecordFactory.traceId, curOrderIndex++,
				EventRecordFactory.FQ_OP_CATALOG_GET_BOOK);
		exit1_1__catalog_getBook = new AfterOperationEvent(EventRecordFactory.curTime++, EventRecordFactory.traceId, curOrderIndex++,
				EventRecordFactory.FQ_OP_CATALOG_GET_BOOK);
		entry2_1__crm_getOrders = new BeforeOperationEvent(EventRecordFactory.curTime++, EventRecordFactory.traceId, curOrderIndex++,
				EventRecordFactory.FQ_OP_CRM_GET_ORDERS);
		entry3_2__catalog_getBook = new BeforeOperationEvent(EventRecordFactory.curTime++, EventRecordFactory.traceId, curOrderIndex++,
				EventRecordFactory.FQ_OP_CATALOG_GET_BOOK);
		exit3_2__catalog_getBook = new AfterOperationEvent(EventRecordFactory.curTime++, EventRecordFactory.traceId, curOrderIndex++,
				EventRecordFactory.FQ_OP_CATALOG_GET_BOOK);
		exit2_1__crm_getOrders = new AfterOperationEvent(EventRecordFactory.curTime++, EventRecordFactory.traceId, curOrderIndex++,
				EventRecordFactory.FQ_OP_CRM_GET_ORDERS);
		exit0_0__bookstore_searchBook = new AfterOperationEvent(EventRecordFactory.curTime++, EventRecordFactory.traceId, curOrderIndex++,
				EventRecordFactory.FQ_OP_BOOKSTORE_SEARCH_BOOK);

		final List<IMonitoringRecord> retList = new ArrayList<IMonitoringRecord>();
		retList.add(entry0_0__bookstore_searchBook);
		retList.add(entry1_1__catalog_getBook);
		retList.add(exit1_1__catalog_getBook);
		retList.add(entry2_1__crm_getOrders);
		retList.add(entry3_2__catalog_getBook);
		retList.add(exit3_2__catalog_getBook);
		retList.add(exit2_1__crm_getOrders);
		retList.add(exit0_0__bookstore_searchBook);

		return retList;
	}

	/**
	 * 
	 * @return
	 */
	public static List<IMonitoringRecord> validSyncTraceAdditionalCallEvents() {
		int curOrderIndex = 0;
		EventRecordFactory.traceId++;

		final BeforeOperationEvent entry0_0__bookstore_searchBook; // NOCS
		final OperationCallEvent call1_1__catalog_getBook; // NOCS
		final BeforeOperationEvent entry1_1__catalog_getBook; // NOCS
		final AfterOperationEvent exit1_1__catalog_getBook; // NOCS
		final OperationCallEvent call2_1__crm_getOrders; // NOCS
		final BeforeOperationEvent entry2_1__crm_getOrders; // NOCS
		final OperationCallEvent call3_2__catalog_getBook; // NOCS
		final BeforeOperationEvent entry3_2__catalog_getBook; // NOCS
		final AfterOperationEvent exit3_2__catalog_getBook; // NOCS
		final AfterOperationEvent exit2_1__crm_getOrders; // NOCS
		final AfterOperationEvent exit0_0__bookstore_searchBook; // NOCS

		entry0_0__bookstore_searchBook = new BeforeOperationEvent(EventRecordFactory.curTime++, EventRecordFactory.traceId, curOrderIndex++,
				EventRecordFactory.FQ_OP_BOOKSTORE_SEARCH_BOOK);
		call1_1__catalog_getBook = new OperationCallEvent(EventRecordFactory.curTime++, EventRecordFactory.traceId, curOrderIndex++,
				EventRecordFactory.FQ_OP_BOOKSTORE_SEARCH_BOOK,
				EventRecordFactory.FQ_OP_CATALOG_GET_BOOK);
		entry1_1__catalog_getBook = new BeforeOperationEvent(EventRecordFactory.curTime++, EventRecordFactory.traceId, curOrderIndex++,
				EventRecordFactory.FQ_OP_CATALOG_GET_BOOK);
		exit1_1__catalog_getBook = new AfterOperationEvent(EventRecordFactory.curTime++, EventRecordFactory.traceId, curOrderIndex++,
				EventRecordFactory.FQ_OP_CATALOG_GET_BOOK);
		call2_1__crm_getOrders = new OperationCallEvent(EventRecordFactory.curTime++, EventRecordFactory.traceId, curOrderIndex++,
				EventRecordFactory.FQ_OP_BOOKSTORE_SEARCH_BOOK,
				EventRecordFactory.FQ_OP_CRM_GET_ORDERS);
		entry2_1__crm_getOrders = new BeforeOperationEvent(EventRecordFactory.curTime++, EventRecordFactory.traceId, curOrderIndex++,
				EventRecordFactory.FQ_OP_CRM_GET_ORDERS);
		call3_2__catalog_getBook = new OperationCallEvent(EventRecordFactory.curTime++, EventRecordFactory.traceId, curOrderIndex++,
				EventRecordFactory.FQ_OP_CRM_GET_ORDERS,
				EventRecordFactory.FQ_OP_CATALOG_GET_BOOK);
		entry3_2__catalog_getBook = new BeforeOperationEvent(EventRecordFactory.curTime++, EventRecordFactory.traceId, curOrderIndex++,
				EventRecordFactory.FQ_OP_CATALOG_GET_BOOK);
		exit3_2__catalog_getBook = new AfterOperationEvent(EventRecordFactory.curTime++, EventRecordFactory.traceId, curOrderIndex++,
				EventRecordFactory.FQ_OP_CATALOG_GET_BOOK);
		exit2_1__crm_getOrders = new AfterOperationEvent(EventRecordFactory.curTime++, EventRecordFactory.traceId, curOrderIndex++,
				EventRecordFactory.FQ_OP_CRM_GET_ORDERS);
		exit0_0__bookstore_searchBook = new AfterOperationEvent(EventRecordFactory.curTime++, EventRecordFactory.traceId, curOrderIndex++,
				EventRecordFactory.FQ_OP_BOOKSTORE_SEARCH_BOOK);

		final List<IMonitoringRecord> retList = new ArrayList<IMonitoringRecord>();
		retList.add(entry0_0__bookstore_searchBook);
		retList.add(call1_1__catalog_getBook);
		retList.add(entry1_1__catalog_getBook);
		retList.add(exit1_1__catalog_getBook);
		retList.add(call2_1__crm_getOrders);
		retList.add(entry2_1__crm_getOrders);
		retList.add(call3_2__catalog_getBook);
		retList.add(entry3_2__catalog_getBook);
		retList.add(exit3_2__catalog_getBook);
		retList.add(exit2_1__crm_getOrders);
		retList.add(exit0_0__bookstore_searchBook);

		return retList;
	}

	/**
	 * 
	 * @return
	 */
	public static List<IMonitoringRecord> validSyncTraceAdditionalCallEventsGap() {
		int curOrderIndex = 0;
		EventRecordFactory.traceId++;

		final BeforeOperationEvent entry0_0__bookstore_searchBook; // NOCS
		final OperationCallEvent call1_1__catalog_getBook; // NOCS
		final BeforeOperationEvent entry1_1__catalog_getBook; // NOCS
		final AfterOperationEvent exit1_1__catalog_getBook; // NOCS
		final OperationCallEvent call2_1__crm_getOrders; // NOCS
		// assumed to be uninstrumented: final BeforeOperationEvent entry2_1__crm_getOrders; // NOCS
		final OperationCallEvent call3_2__catalog_getBook; // NOCS
		final BeforeOperationEvent entry3_2__catalog_getBook; // NOCS
		final AfterOperationEvent exit3_2__catalog_getBook; // NOCS
		// assumed to be uninstrumented: final AfterOperationEvent exit2_1__crm_getOrders; // NOCS
		final AfterOperationEvent exit0_0__bookstore_searchBook; // NOCS

		entry0_0__bookstore_searchBook = new BeforeOperationEvent(EventRecordFactory.curTime++, EventRecordFactory.traceId, curOrderIndex++,
				EventRecordFactory.FQ_OP_BOOKSTORE_SEARCH_BOOK);
		call1_1__catalog_getBook = new OperationCallEvent(EventRecordFactory.curTime++, EventRecordFactory.traceId, curOrderIndex++,
				EventRecordFactory.FQ_OP_BOOKSTORE_SEARCH_BOOK,
				EventRecordFactory.FQ_OP_CATALOG_GET_BOOK);
		entry1_1__catalog_getBook = new BeforeOperationEvent(EventRecordFactory.curTime++, EventRecordFactory.traceId, curOrderIndex++,
				EventRecordFactory.FQ_OP_CATALOG_GET_BOOK);
		exit1_1__catalog_getBook = new AfterOperationEvent(EventRecordFactory.curTime++, EventRecordFactory.traceId, curOrderIndex++,
				EventRecordFactory.FQ_OP_CATALOG_GET_BOOK);
		call2_1__crm_getOrders = new OperationCallEvent(EventRecordFactory.curTime++, EventRecordFactory.traceId, curOrderIndex++,
				EventRecordFactory.FQ_OP_BOOKSTORE_SEARCH_BOOK,
				EventRecordFactory.FQ_OP_CRM_GET_ORDERS);
		// assumed to be uninstrumented:
		// entry2_1__crm_getOrders = new BeforeOperationEvent(curTime++, traceId, curOrderIndex++, EventRecordFactory.FQ_OP_CRM_GET_ORDERS);
		call3_2__catalog_getBook = new OperationCallEvent(EventRecordFactory.curTime++, EventRecordFactory.traceId, curOrderIndex++,
				EventRecordFactory.FQ_OP_CRM_GET_ORDERS,
				EventRecordFactory.FQ_OP_CATALOG_GET_BOOK);
		entry3_2__catalog_getBook = new BeforeOperationEvent(EventRecordFactory.curTime++, EventRecordFactory.traceId, curOrderIndex++,
				EventRecordFactory.FQ_OP_CATALOG_GET_BOOK);
		exit3_2__catalog_getBook = new AfterOperationEvent(EventRecordFactory.curTime++, EventRecordFactory.traceId, curOrderIndex++,
				EventRecordFactory.FQ_OP_CATALOG_GET_BOOK);
		// assumed to be uninstrumented:
		// exit2_1__crm_getOrders = new AfterOperationEvent(curTime++, traceId, curOrderIndex++, EventRecordFactory.FQ_OP_CRM_GET_ORDERS);
		exit0_0__bookstore_searchBook = new AfterOperationEvent(EventRecordFactory.curTime++, EventRecordFactory.traceId, curOrderIndex++,
				EventRecordFactory.FQ_OP_BOOKSTORE_SEARCH_BOOK);

		final List<IMonitoringRecord> retList = new ArrayList<IMonitoringRecord>();
		retList.add(entry0_0__bookstore_searchBook);
		retList.add(call1_1__catalog_getBook);
		retList.add(entry1_1__catalog_getBook);
		retList.add(exit1_1__catalog_getBook);
		retList.add(call2_1__crm_getOrders);
		retList.add(call3_2__catalog_getBook);
		retList.add(entry3_2__catalog_getBook);
		retList.add(exit3_2__catalog_getBook);
		retList.add(exit0_0__bookstore_searchBook);

		return retList;
	}

	public static void main(final String[] args) {
		final IMonitoringController ctrl = MonitoringController.getInstance();

		final List<IMonitoringRecord> allRecords = new ArrayList<IMonitoringRecord>();

		final List<IMonitoringRecord> validSyncTraceBeforeAfterEvents = EventRecordFactory.validSyncTraceAdditionalCallEventsGap();
		allRecords.addAll(validSyncTraceBeforeAfterEvents);
		final List<IMonitoringRecord> validSyncTraceAdditionalCallEvents = EventRecordFactory.validSyncTraceAdditionalCallEvents();
		allRecords.addAll(validSyncTraceAdditionalCallEvents);
		final List<IMonitoringRecord> validSyncTraceAdditionalCallEventsGap = EventRecordFactory.validSyncTraceAdditionalCallEventsGap();
		allRecords.addAll(validSyncTraceAdditionalCallEventsGap);

		for (final IMonitoringRecord r : allRecords) {
			ctrl.newMonitoringRecord(r);
		}

		ctrl.terminateMonitoring();
	}
}
