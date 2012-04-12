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

package kieker.test.common.junit.record;

import java.util.ArrayList;
import java.util.List;

import kieker.common.record.controlflow.OperationExecutionRecord;

/**
 * Provides some constants for the bookstore example, including class names,
 * operation signatures etc., as well as methods returning (valid and invalid)
 * bookstore traces.
 * 
 * @author Andre van Hoorn
 * 
 */
public final class BookstoreOperationExecutionRecordFactory {
	public static final String PACKAGE_BOOKSTORE_APP = "bookstore";

	public static final String SIMPLE_CLASS_BOOKSTORE = "Bookstore";
	public static final String SIMPLE_CLASS_CATALOG = "Catalog";
	public static final String SIMPLE_CLASS_CRM = "CRM";

	public static final String FQ_CLASS_BOOKSTORE =
			BookstoreOperationExecutionRecordFactory.PACKAGE_BOOKSTORE_APP
					+ "." + BookstoreOperationExecutionRecordFactory.SIMPLE_CLASS_BOOKSTORE;
	public static final String FQ_CLASS_CATALOG = BookstoreOperationExecutionRecordFactory.PACKAGE_BOOKSTORE_APP
			+ "." + BookstoreOperationExecutionRecordFactory.SIMPLE_CLASS_CATALOG;
	public static final String FQ_CLASS_CRM = BookstoreOperationExecutionRecordFactory.PACKAGE_BOOKSTORE_APP
			+ "." + BookstoreOperationExecutionRecordFactory.SIMPLE_CLASS_CRM;

	public static final String OP_MODIFIER_BOOKSTORE_SEARCH_BOOK = "public";
	public static final String OP_RETTYPE_BOOKSTORE_SEARCH_BOOK = "Book";
	public static final String OP_NAME_BOOKSTORE_SEARCH_BOOK = "searchBook";
	public static final String OP_ARGTYPE_BOOKSTORE_SEARCH_BOOK = "long";
	public static final String OP_FQNAME_BOOKSTORE_SEARCH_BOOK =
			BookstoreOperationExecutionRecordFactory.FQ_CLASS_BOOKSTORE + "." + BookstoreOperationExecutionRecordFactory.OP_NAME_BOOKSTORE_SEARCH_BOOK;
	public static final String OP_NAMEWITHARG_BOOKSTORE_SEARCH_BOOK =
			BookstoreOperationExecutionRecordFactory.OP_NAME_BOOKSTORE_SEARCH_BOOK + "("
					+ BookstoreOperationExecutionRecordFactory.OP_ARGTYPE_BOOKSTORE_SEARCH_BOOK + ")";

	public static final String OP_MODIFIER_CATALOG_GET_BOOK = "private";
	public static final String OP_RETTYPE_CATALOG_GET_BOOK = "Book";
	public static final String OP_NAME_CATALOG_GET_BOOK = "getBook";
	public static final String OP_ARGTYPE_CATALOG_GET_BOOK = "long";
	public static final String OP_FQNAME_CATALOG_GET_BOOK =
			BookstoreOperationExecutionRecordFactory.FQ_CLASS_CATALOG + "." + BookstoreOperationExecutionRecordFactory.OP_NAME_CATALOG_GET_BOOK;
	public static final String OP_NAMEWITHARG_CATALOG_GET_BOOK =
			BookstoreOperationExecutionRecordFactory.OP_NAME_CATALOG_GET_BOOK + "("
					+ BookstoreOperationExecutionRecordFactory.OP_ARGTYPE_CATALOG_GET_BOOK + ")";

	public static final String OP_MODIFIER_CRM_GET_ORDERS = "private";
	public static final String OP_RETTYPE_CRM_GET_ORDERS = "Order[]";
	public static final String OP_NAME_CRM_GET_ORDERS = "getOrders";
	public static final String OP_ARGTYPE_CRM_GET_ORDERS = "long";
	public static final String OP_FQNAME_CRM_GET_ORDERS =
			BookstoreOperationExecutionRecordFactory.FQ_CLASS_CRM + "." + BookstoreOperationExecutionRecordFactory.OP_NAME_CRM_GET_ORDERS;
	public static final String OP_NAMEWITHARG_CRM_GET_ORDERS =
			BookstoreOperationExecutionRecordFactory.OP_NAME_CRM_GET_ORDERS + "("
					+ BookstoreOperationExecutionRecordFactory.OP_ARGTYPE_CRM_GET_ORDERS + ")";

	public static final String FQ_SIGNATURE_BOOKSTORE_SEARCH_BOOK =
			BookstoreOperationExecutionRecordFactory.OP_MODIFIER_BOOKSTORE_SEARCH_BOOK + " "
					+ BookstoreOperationExecutionRecordFactory.OP_RETTYPE_BOOKSTORE_SEARCH_BOOK + " "
					+ BookstoreOperationExecutionRecordFactory.OP_FQNAME_BOOKSTORE_SEARCH_BOOK + "("
					+ BookstoreOperationExecutionRecordFactory.OP_ARGTYPE_BOOKSTORE_SEARCH_BOOK + ")";

	public static final String FQ_SIGNATURE_CATALOG_GET_BOOK =
			BookstoreOperationExecutionRecordFactory.OP_MODIFIER_CATALOG_GET_BOOK + " "
					+ BookstoreOperationExecutionRecordFactory.OP_RETTYPE_CATALOG_GET_BOOK + " "
					+ BookstoreOperationExecutionRecordFactory.OP_FQNAME_CATALOG_GET_BOOK + "("
					+ BookstoreOperationExecutionRecordFactory.OP_ARGTYPE_CATALOG_GET_BOOK + ")";

	public static final String FQ_SIGNATURE_CRM_GET_ORDERS =
			BookstoreOperationExecutionRecordFactory.OP_MODIFIER_CRM_GET_ORDERS + " "
					+ BookstoreOperationExecutionRecordFactory.OP_RETTYPE_CRM_GET_ORDERS + " "
					+ BookstoreOperationExecutionRecordFactory.OP_FQNAME_CRM_GET_ORDERS + "("
					+ BookstoreOperationExecutionRecordFactory.OP_ARGTYPE_CRM_GET_ORDERS + ")";

	public static final int EXEC0_0__BOOKSTORE_SEARCHBOOK_EOI = 0; // NOCS (constant name)
	public static final int EXEC0_0__BOOKSTORE_SEARCHBOOK_ESS = 0; // NOCS (constant name)
	public static final int EXEC1_1__CATALOG_GETBOOK_EOI = 1; // NOCS (constant name)
	public static final int EXEC1_1__CATALOG_GETBOOK_ESS = 1; // NOCS (constant name)
	public static final int EXEC2_1__CRM_GETORDERS_EOI = 2; // NOCS (constant name)
	public static final int EXEC2_1__CRM_GETORDERS_ESS = 1; // NOCS (constant name)
	public static final int EXEC3_2__CATALOG_GETBOOK_EOI = 3; // NOCS (constant name)
	public static final int EXEC3_2__CATALOG_GETBOOK_ESS = 2; // NOCS (constant name)

	private BookstoreOperationExecutionRecordFactory() {}

	/**
	 * Returns the ordered List of {@link OperationExecutionRecord}s for the "well-known" bookstore trace with
	 * short operation signatures, i.e., class name and operation without modifiers, return type, args etc.
	 * Example: <code>Catalog.searchBook</code>.
	 * 
	 * @param sessionId
	 * @param traceId
	 * @return
	 */
	public static List<OperationExecutionRecord> genValidBookstoreTraceFullSignature(final String sessionId, final long traceId) {
		final String hostname = "srv9786";

		final List<OperationExecutionRecord> retList = new ArrayList<OperationExecutionRecord>(/* # executions: */4);

		final OperationExecutionRecord exec0_0__bookstore_searchBook = // NOCS (LocalFinalVariableNameCheck)
		new OperationExecutionRecord(
				BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_BOOKSTORE_SEARCH_BOOK,
				sessionId, traceId, /* tin: */1, /* tout: */10,
				hostname,
				BookstoreOperationExecutionRecordFactory.EXEC0_0__BOOKSTORE_SEARCHBOOK_EOI,
				BookstoreOperationExecutionRecordFactory.EXEC0_0__BOOKSTORE_SEARCHBOOK_ESS);
		retList.add(exec0_0__bookstore_searchBook);

		final OperationExecutionRecord exec1_1__catalog_getBook = // NOCS (LocalFinalVariableNameCheck)
		new OperationExecutionRecord(
				BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_CATALOG_GET_BOOK,
				sessionId, traceId, /* tin: */2, /* tout: */4,
				hostname,
				BookstoreOperationExecutionRecordFactory.EXEC1_1__CATALOG_GETBOOK_EOI,
				BookstoreOperationExecutionRecordFactory.EXEC0_0__BOOKSTORE_SEARCHBOOK_ESS);
		retList.add(exec1_1__catalog_getBook);

		final OperationExecutionRecord exec2_1__crm_getOrders = // NOCS (LocalFinalVariableNameCheck)
		new OperationExecutionRecord(
				BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_CRM_GET_ORDERS,
				sessionId, traceId, /* tin: */5, /* tout: */8, hostname,
				BookstoreOperationExecutionRecordFactory.EXEC2_1__CRM_GETORDERS_EOI,
				BookstoreOperationExecutionRecordFactory.EXEC2_1__CRM_GETORDERS_ESS);
		retList.add(exec2_1__crm_getOrders);

		final OperationExecutionRecord exec3_2__catalog_getBook = // NOCS (LocalFinalVariableNameCheck)
		new OperationExecutionRecord(
				BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_CATALOG_GET_BOOK,
				sessionId, traceId, /* tin: */6, /* tout: */7, hostname,
				BookstoreOperationExecutionRecordFactory.EXEC3_2__CATALOG_GETBOOK_EOI,
				BookstoreOperationExecutionRecordFactory.EXEC3_2__CATALOG_GETBOOK_ESS);
		retList.add(exec3_2__catalog_getBook);
		return retList;
	}
}
