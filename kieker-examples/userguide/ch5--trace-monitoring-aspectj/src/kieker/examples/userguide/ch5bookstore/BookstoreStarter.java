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

package kieker.examples.userguide.ch5bookstore;

import java.util.concurrent.CountDownLatch;

public final class BookstoreStarter {

	private static final int NUM_REQUESTS_DEFAULT = 1;

	private BookstoreStarter() {}

	public static void main(final String[] args) throws InterruptedException {
		final Bookstore bookstore = new Bookstore();
		final int numTraces = BookstoreStarter.extractNumRequestsFromArgs(args);
		final CountDownLatch latch = new CountDownLatch(numTraces);

		for (int i = 0; i < numTraces; i++) {
			System.out.println("Bookstore.main: Starting request " + i);
			BookstoreStarter.spawnAsyncRequest(bookstore, latch);
		}
		// Now, wait for all threads requests to complete
		latch.await();
	}

	/**
	 * Starts a thread that issues a request to the bookstore and returns
	 * immediately. After the return of the request, the countDown method of
	 * latch is called by the spawned thread.
	 * 
	 * @param latch
	 *            count down latch counted down after the request is completed
	 * @param bookstore
	 *            the bookstore
	 */
	private static void spawnAsyncRequest(final Bookstore bookstore,
			final CountDownLatch latch) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				bookstore.searchBook();
				latch.countDown();
			}
		}).start();
	}

	/**
	 * Returns the number of requests to simulate based on the command-line
	 * argument list args. Users may pass the number of requests as the first
	 * command-line argument, i.e., the value of args' field with index 0.
	 * If args is null or empty, or the value cannot be parsed to an int, this
	 * method returns the default value ${@link #NUM_REQUESTS_DEFAULT}.
	 * 
	 * @param args
	 *            the command-line arguments
	 * @return the number of requests to simulate
	 */
	private static int extractNumRequestsFromArgs(final String[] args) {
		if ((args == null) || (args.length == 0)) {
			return BookstoreStarter.NUM_REQUESTS_DEFAULT;
		}

		try {
			return Integer.parseInt(args[0]);
		} catch (final NumberFormatException exc) {
			System.out.println("Couldn't extract number from argument value " + args[0]);
			System.out.println("Will use the default value " + BookstoreStarter.NUM_REQUESTS_DEFAULT);
			return BookstoreStarter.NUM_REQUESTS_DEFAULT;
		}
	}
}
