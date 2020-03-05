/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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
public class CRM {
	private final Catalog catalog;

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param catalog
	 *            The catalog for this CRM.
	 */
	@Autowired
	public CRM(final Catalog catalog) {
		this.catalog = catalog;
	}

	public Book getOffers() {
		return this.catalog.getBook(false);
	}
}
