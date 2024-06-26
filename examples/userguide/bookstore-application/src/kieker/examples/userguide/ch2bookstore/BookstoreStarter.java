/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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

package kieker.examples.userguide.ch2bookstore;

public final class BookstoreStarter {

	private BookstoreStarter() {}

	public static void main(final String[] args) {
		final Bookstore bookstore = new Bookstore();
		for (int i = 0; i < 5; i++) {
			System.out.println("Bookstore.main: Starting request " + i);
			bookstore.searchBook();
		}
	}
}
