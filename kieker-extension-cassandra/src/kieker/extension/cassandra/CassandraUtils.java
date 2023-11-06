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
package kieker.extension.cassandra;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Reiner Jung
 * @since 1.16
 */
public final class CassandraUtils {
		
	public static final String DEFAULT_HOST = "localhost";
	public static final int DEFAULT_PORT = 9042;

	private CassandraUtils() {
		// utility class
	}
	
	/**
	 * Compute the database connections.
	 *
	 * @param contactPointSpecs contact point specs
	 * @return socket address
	 */
	public static List<InetSocketAddress> computeDatabaseConnections(final String[] contactPointSpecs) {
		final List<InetSocketAddress> contactPoints = new ArrayList<>();

		if (contactPointSpecs.length == 0) {
			final InetSocketAddress socket = new InetSocketAddress(DEFAULT_HOST, DEFAULT_PORT);
			contactPoints.add(socket);
		} else {
			for (final String contactpoint : contactPointSpecs) {
				final String[] array = contactpoint.split(":");
				if (array.length == 2) {
					final InetSocketAddress socket = new InetSocketAddress(array[0], Integer.parseInt(array[1]));
					contactPoints.add(socket);
				}
			}
		}

		return contactPoints;
	}
}
