/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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

/**
 * @author Andre van Hoorn
 * 
 * @since 1.6
 */
public class Book {

	private String title;

	/**
	 * Creates a new book with the given title.
	 * 
	 * @param title
	 *            The title of the book.
	 */
	public Book(final String title) {
		this.title = title;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String tile) {
		this.title = tile;
	}

	@Override
	public String toString() {
		return this.title;
	}
}
