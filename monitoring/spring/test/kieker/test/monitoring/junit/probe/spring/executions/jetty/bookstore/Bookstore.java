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

package kieker.test.monitoring.junit.probe.spring.executions.jetty.bookstore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Andre van Hoorn
 *
 * @since 1.6
 */
@Service
public class Bookstore {

	@Autowired
	private Catalog catalog;
	@Autowired
	private CRM crm;

	/**
	 * Default constructor.
	 */
	public Bookstore() {
		// empty default constructor
	}

	/**
	 * Searches for a book.
	 *
	 * @param term
	 *            The term to search for.
	 *
	 * @return The resulting book.
	 */
	public Book searchBook(final String term) {
		this.catalog.getBook(false);
		return this.crm.getOffers();
	}
}
