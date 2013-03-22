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

import java.util.ArrayList;
import java.util.List;

/**
 * A simple test and demonstration scenario for Kieker's
 * monitoring component.
 * 
 * This test does not use the Kieker typical Java Annotations. The test is used
 * to demonstrate how to instrument programs for that no sources are available.
 * 
 * @author Matthias Rohr
 * 
 * @since 0.9
 */
public class BookstoreWA extends Thread {
	public static final List<BookstoreWA> BOOKSTORE_SCENARIOS = new ArrayList<BookstoreWA>();

	private static final int NUM_REQUESTS = 100;
	private static final int INTER_REQUEST_TIME = 5;

	public BookstoreWA() {
		// nothing to do
	}

	/**
	 * 
	 * main is the load driver for the Bookstore. It creates
	 * request which all request a search from the bookstore.
	 * A fixed time delay is between two request. Requests
	 * are likely to overlap, which leads to request processing
	 * in more than one thread.
	 * 
	 * Both the number of requests and arrival rate are defined
	 * by the local variables above the method.
	 * (default: 100 requests; interRequestTime 5 (millisecs))
	 * 
	 * @param args
	 *            The command line arguments. They have currently no effect.
	 * 
	 * @throws InterruptedException
	 *             If the main thread has been interrupted.
	 */
	public static void main(final String[] args) throws InterruptedException {
		for (int i = 0; i < BookstoreWA.NUM_REQUESTS; i++) {
			System.out.println("BookstoreWA.main: Starting request " + i); // NOPMD (System.out)
			final BookstoreWA newBookstore = new BookstoreWA();
			synchronized (BookstoreWA.BOOKSTORE_SCENARIOS) {
				BookstoreWA.BOOKSTORE_SCENARIOS.add(newBookstore);
			}
			newBookstore.start();
			BookstoreWA.waitabit(BookstoreWA.INTER_REQUEST_TIME);
		}
		System.out.println("Bookstore.main: Finished with starting all requests."); // NOPMD (System.out)
		System.out.println("Bookstore.main: Waiting for threads to terminate"); // NOPMD (System.out)
		synchronized (BookstoreWA.BOOKSTORE_SCENARIOS) {
			while (!BookstoreWA.BOOKSTORE_SCENARIOS.isEmpty()) {
				BookstoreWA.BOOKSTORE_SCENARIOS.wait();
			}
		}
	}

	@Override
	public void run() {
		BookstoreWA.searchBook();
		synchronized (BookstoreWA.BOOKSTORE_SCENARIOS) {
			BookstoreWA.BOOKSTORE_SCENARIOS.remove(this);
			BookstoreWA.BOOKSTORE_SCENARIOS.notifyAll();
		}
	}

	public static void searchBook() {
		CatalogWA.getBook(false);
		CRMWA.getOffers();
	}

	/**
	 * This method encapsulates simply Thread.sleep().
	 * 
	 * @param waittime
	 *            The time to wait in milliseconds.
	 */
	public static void waitabit(final long waittime) {
		if (waittime > 0) {
			try {
				Thread.sleep(waittime);
			} catch (final InterruptedException e) {
			}
		}
	}
}
