/***************************************************************************
 * Copyright 2012 by
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

import kieker.common.util.ClassOperationSignaturePair;
import kieker.tools.traceAnalysis.filter.AbstractTraceAnalysisFilter;
import kieker.tools.traceAnalysis.systemModel.Execution;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;

import kieker.test.common.util.record.BookstoreOperationExecutionRecordFactory;

/**
 * @author Andre van Hoorn
 */
public class BookstoreExecutionFactory {
	private final SystemModelRepository systemEntityFactory;

	/**
	 * @param systemEntityFactory
	 */
	public BookstoreExecutionFactory(final SystemModelRepository systemEntityFactory) {
		this.systemEntityFactory = systemEntityFactory;
	}

	public Execution createBookstoreExecution_exec0_0__bookstore_searchBook(// NOPMD (MethodNamingConventions) // NOCS (MethodNameCheck)
			final long traceId, final String sessionId, final String hostname, final long tin, final long tout) {
		final boolean assumed = false; // currently not relevant

		final ClassOperationSignaturePair classOpSignaturePair = ClassOperationSignaturePair
				.splitOperationSignatureStr(BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_BOOKSTORE_SEARCH_BOOK);

		return AbstractTraceAnalysisFilter.createExecutionByEntityNames(this.systemEntityFactory, hostname, classOpSignaturePair.getFqClassname(),
				classOpSignaturePair.getSignature(),
				traceId, sessionId, BookstoreOperationExecutionRecordFactory.EXEC0_0__BOOKSTORE_SEARCHBOOK_EOI,
				BookstoreOperationExecutionRecordFactory.EXEC0_0__BOOKSTORE_SEARCHBOOK_ESS, tin, tout,
				assumed);
	}

	public Execution createBookstoreExecution_exec1_1__catalog_getBook(// NOPMD (MethodNamingConventions) // NOCS (MethodNameCheck)
			final long traceId, final String sessionId, final String hostname, final long tin, final long tout) {
		final boolean assumed = false; // currently not relevant

		final ClassOperationSignaturePair classOpSignaturePair = ClassOperationSignaturePair
				.splitOperationSignatureStr(BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_CATALOG_GET_BOOK);

		return AbstractTraceAnalysisFilter.createExecutionByEntityNames(this.systemEntityFactory, hostname, classOpSignaturePair.getFqClassname(),
				classOpSignaturePair.getSignature(),
				traceId, sessionId, BookstoreOperationExecutionRecordFactory.EXEC1_1__CATALOG_GETBOOK_EOI,
				BookstoreOperationExecutionRecordFactory.EXEC1_1__CATALOG_GETBOOK_ESS, tin, tout,
				assumed);
	}

	public Execution createBookstoreExecution_crm_getOrders(// NOPMD (MethodNamingConventions) // NOCS (MethodNameCheck)
			final long traceId, final String sessionId, final String hostname, final long tin, final long tout, final int eoi, final int ess) {
		final boolean assumed = false; // currently not relevant

		final ClassOperationSignaturePair classOpSignaturePair = ClassOperationSignaturePair
				.splitOperationSignatureStr(BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_CRM_GET_ORDERS);

		return AbstractTraceAnalysisFilter.createExecutionByEntityNames(this.systemEntityFactory, hostname, classOpSignaturePair.getFqClassname(),
				classOpSignaturePair.getSignature(),
				traceId, sessionId, eoi, ess, tin, tout, assumed);
	}

	public Execution createBookstoreExecution_exec2_1__crm_getOrders(// NOPMD (MethodNamingConventions) // NOCS (MethodNameCheck)
			final long traceId, final String sessionId, final String hostname, final long tin, final long tout) {
		return this.createBookstoreExecution_crm_getOrders(traceId, sessionId, hostname, tin, tout,
				BookstoreOperationExecutionRecordFactory.EXEC2_1__CRM_GETORDERS_EOI, BookstoreOperationExecutionRecordFactory.EXEC2_1__CRM_GETORDERS_ESS);
	}

	public Execution createBookstoreExecution_catalog_getBook(// NOPMD (MethodNamingConventions) // NOCS (MethodNameCheck)
			final long traceId, final String sessionId, final String hostname, final long tin, final long tout, final int eoi, final int ess) {
		final boolean assumed = false; // currently not relevant

		final ClassOperationSignaturePair classOpSignaturePair = ClassOperationSignaturePair
				.splitOperationSignatureStr(BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_CATALOG_GET_BOOK);

		return AbstractTraceAnalysisFilter.createExecutionByEntityNames(this.systemEntityFactory, hostname, classOpSignaturePair.getFqClassname(),
				classOpSignaturePair.getSignature(), traceId, sessionId, eoi, ess, tin, tout, assumed);
	}

	public Execution createBookstoreExecution_exec3_2__catalog_getBook(// NOPMD (MethodNamingConventions) // NOCS (MethodNameCheck)
			final long traceId, final String sessionId, final String hostname, final long tin, final long tout) {
		return this.createBookstoreExecution_catalog_getBook(traceId, sessionId, hostname, tin, tout,
				BookstoreOperationExecutionRecordFactory.EXEC3_2__CATALOG_GETBOOK_EOI, BookstoreOperationExecutionRecordFactory.EXEC3_2__CATALOG_GETBOOK_ESS);
	}
}
