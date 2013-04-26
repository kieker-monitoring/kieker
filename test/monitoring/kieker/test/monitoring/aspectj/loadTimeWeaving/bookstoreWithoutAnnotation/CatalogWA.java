/***************************************************************************
 * Copyright 2013 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.monitoring.aspectj.loadTimeWeaving.bookstoreWithoutAnnotation;

/**
 * A simple test and demonstration scenario for Kieker's monitoring component.
 * 
 * @author Matthias Rohr History: 2008/08/30: Created based on Catalog.java
 *         without Annotations
 * 
 * @since 0.9
 */
public final class CatalogWA {

	private CatalogWA() {}

	/**
	 * A dummy method which waits a little bit.
	 * 
	 * @param complexQuery
	 *            Determines whether to wait 20 ms or 2 ms.
	 */
	public static void getBook(final boolean complexQuery) {
		if (complexQuery) {
			// complex query
			BookstoreWA.waitabit(20);
		} else {
			// simple query
			BookstoreWA.waitabit(2);
		}
	}
}
