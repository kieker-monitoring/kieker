/***************************************************************************
 * Copyright 2021 Kieker Project (http://kieker-monitoring.net)
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

import kieker.analysis.trace.AbstractTraceAnalysisStage;
import kieker.common.util.signature.ClassOperationSignaturePair;
import kieker.model.repository.SystemModelRepository;
import kieker.model.system.model.Execution;

import kieker.test.common.util.record.BookstoreOperationExecutionRecordFactory;

/**
 * This factory class can be used to create artificial executions within the bookstore example.
 *
 * @author Andre van Hoorn
 *
 * @since 1.5
 */
public class BookstoreExecutionFactory {

	private final SystemModelRepository systemEntityFactory;

	/**
	 * Creates a new instance of this factory using the given parameter.
	 *
	 * @param systemEntityFactory
	 *            The system repository.
	 */
	public BookstoreExecutionFactory(final SystemModelRepository systemEntityFactory) {
		this.systemEntityFactory = systemEntityFactory;
	}

	/**
	 * This method creates an artificial bookstore execution of "Bookstore.searchBook(long)" with eoi set to 0 and ess set to 0.
	 *
	 * @param traceId
	 *            The trace ID.
	 * @param sessionId
	 *            The session ID.
	 * @param hostname
	 *            The name of the host.
	 * @param tin
	 *            The time the execution started.
	 * @param tout
	 *            The time the execution finished.
	 * @param assumed
	 *            Whether the execution is assumed or not.
	 *
	 * @return An artificial execution.
	 */
	public Execution createBookstoreExecution_exec0_0__bookstore_searchBook( // NOPMD (MethodNamingConventions) // NOCS (MethodNameCheck)
			final long traceId, final String sessionId, final String hostname, final long tin, final long tout, final boolean assumed) {

		final ClassOperationSignaturePair classOpSignaturePair = ClassOperationSignaturePair
				.splitOperationSignatureStr(BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_BOOKSTORE_SEARCH_BOOK);

		return AbstractTraceAnalysisStage.createExecutionByEntityNames(this.systemEntityFactory, hostname, classOpSignaturePair.getFqClassname(),
				classOpSignaturePair.getSignature(), traceId, sessionId, BookstoreOperationExecutionRecordFactory.EXEC0_0__BOOKSTORE_SEARCHBOOK_EOI,
				BookstoreOperationExecutionRecordFactory.EXEC0_0__BOOKSTORE_SEARCHBOOK_ESS, tin, tout, assumed);
	}

	/**
	 * This method creates an artificial bookstore execution of "Catalog.getBook(long)" with eoi set to 1 and ess set to 1.
	 *
	 * @param traceId
	 *            The trace ID.
	 * @param sessionId
	 *            The session ID.
	 * @param hostname
	 *            The name of the host.
	 * @param tin
	 *            The time the execution started.
	 * @param tout
	 *            The time the execution finished.
	 * @param assumed
	 *            Whether the execution is assumed or not.
	 *
	 * @return An artificial execution.
	 */
	public Execution createBookstoreExecution_exec1_1__catalog_getBook(// NOPMD (MethodNamingConventions) // NOCS (MethodNameCheck)
			final long traceId, final String sessionId, final String hostname, final long tin, final long tout, final boolean assumed) {
		final ClassOperationSignaturePair classOpSignaturePair = ClassOperationSignaturePair
				.splitOperationSignatureStr(BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_CATALOG_GET_BOOK);

		return AbstractTraceAnalysisStage.createExecutionByEntityNames(this.systemEntityFactory, hostname, classOpSignaturePair.getFqClassname(),
				classOpSignaturePair.getSignature(), traceId, sessionId, BookstoreOperationExecutionRecordFactory.EXEC1_1__CATALOG_GETBOOK_EOI,
				BookstoreOperationExecutionRecordFactory.EXEC1_1__CATALOG_GETBOOK_ESS, tin, tout, assumed);
	}

	/**
	 * This method creates an artificial bookstore execution of "CRM.getOrders(long)".
	 *
	 * @param traceId
	 *            The trace ID.
	 * @param sessionId
	 *            The session ID.
	 * @param hostname
	 *            The name of the host.
	 * @param tin
	 *            The time the execution started.
	 * @param tout
	 *            The time the execution finished.
	 * @param eoi
	 *            The execution order index.
	 * @param ess
	 *            The execution stack size.
	 * @param assumed
	 *            Whether the execution is assumed or not.
	 *
	 * @return An artificial execution.
	 */
	public Execution createBookstoreExecution_crm_getOrders( // NOPMD (MethodNamingConventions) // NOCS (MethodNameCheck)
			final long traceId, final String sessionId, final String hostname, final long tin, final long tout, final int eoi, final int ess,
			final boolean assumed) {
		final ClassOperationSignaturePair classOpSignaturePair = ClassOperationSignaturePair
				.splitOperationSignatureStr(BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_CRM_GET_ORDERS);

		return AbstractTraceAnalysisStage.createExecutionByEntityNames(this.systemEntityFactory, hostname, classOpSignaturePair.getFqClassname(),
				classOpSignaturePair.getSignature(), traceId, sessionId, eoi, ess, tin, tout, assumed);
	}

	/**
	 * This method creates an artificial bookstore execution of "CRM.getOrders(long)" with eoi set to 2 and ess set to 1.
	 *
	 * @param traceId
	 *            The trace ID.
	 * @param sessionId
	 *            The session ID.
	 * @param hostname
	 *            The name of the host.
	 * @param tin
	 *            The time the execution started.
	 * @param tout
	 *            The time the execution finished.
	 * @param assumed
	 *            Whether the execution is assumed or not.
	 *
	 * @return An artificial execution.
	 */
	public Execution createBookstoreExecution_exec2_1__crm_getOrders( // NOPMD (MethodNamingConventions) // NOCS (MethodNameCheck)
			final long traceId, final String sessionId, final String hostname, final long tin, final long tout, final boolean assumed) {
		return this.createBookstoreExecution_crm_getOrders(traceId, sessionId, hostname, tin, tout,
				BookstoreOperationExecutionRecordFactory.EXEC2_1__CRM_GETORDERS_EOI, BookstoreOperationExecutionRecordFactory.EXEC2_1__CRM_GETORDERS_ESS, assumed);
	}

	/**
	 * This method creates an artificial bookstore execution of "Catalog.getBook(long)".
	 *
	 * @param traceId
	 *            The trace ID.
	 * @param sessionId
	 *            The session ID.
	 * @param hostname
	 *            The name of the host.
	 * @param tin
	 *            The time the execution started.
	 * @param tout
	 *            The time the execution finished.
	 * @param eoi
	 *            The execution order index.
	 * @param ess
	 *            The execution stack size.
	 * @param assumed
	 *            Whether the execution is assumed or not.
	 *
	 * @return An artificial execution.
	 */
	public Execution createBookstoreExecution_catalog_getBook( // NOPMD (MethodNamingConventions) // NOCS (MethodNameCheck)
			final long traceId, final String sessionId, final String hostname, final long tin, final long tout, final int eoi, final int ess,
			final boolean assumed) {

		final ClassOperationSignaturePair classOpSignaturePair = ClassOperationSignaturePair
				.splitOperationSignatureStr(BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_CATALOG_GET_BOOK);

		return AbstractTraceAnalysisStage.createExecutionByEntityNames(this.systemEntityFactory, hostname, classOpSignaturePair.getFqClassname(),
				classOpSignaturePair.getSignature(), traceId, sessionId, eoi, ess, tin, tout, assumed);
	}

	/**
	 * This method creates an artificial bookstore execution of "Catalog.getBook(long)" with eoi set to 3 and ess set to 2.
	 *
	 * @param traceId
	 *            The trace ID.
	 * @param sessionId
	 *            The session ID.
	 * @param hostname
	 *            The name of the host.
	 * @param tin
	 *            The time the execution started.
	 * @param tout
	 *            The time the execution finished.
	 * @param assumed
	 *            Whether the execution is assumed or not.
	 *
	 * @return An artificial execution.
	 */
	public Execution createBookstoreExecution_exec3_2__catalog_getBook( // NOPMD (MethodNamingConventions) // NOCS (MethodNameCheck)
			final long traceId, final String sessionId, final String hostname, final long tin, final long tout, final boolean assumed) {
		return this.createBookstoreExecution_catalog_getBook(traceId, sessionId, hostname, tin, tout,
				BookstoreOperationExecutionRecordFactory.EXEC3_2__CATALOG_GETBOOK_EOI, BookstoreOperationExecutionRecordFactory.EXEC3_2__CATALOG_GETBOOK_ESS,
				assumed);
	}
}
