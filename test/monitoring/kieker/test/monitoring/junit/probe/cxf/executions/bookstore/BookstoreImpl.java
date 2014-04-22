/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.monitoring.junit.probe.cxf.executions.bookstore;

import javax.jws.WebService;

/**
 * @author Marius Loewe
 * 
 * @since 1.6
 */
@WebService(endpointInterface = "kieker.test.monitoring.junit.probe.cxf.executions.bookstore.IBookstore", serviceName = "Bookstore")
public class BookstoreImpl implements IBookstore {

	/**
	 * Default constructor.
	 */
	public BookstoreImpl() {
		// empty default constructor
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String searchBook(final String term) {
		return term;
	}
}
