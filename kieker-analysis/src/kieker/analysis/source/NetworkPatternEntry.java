/***************************************************************************
 * Copyright 2019 Kieker Project (http://kieker-monitoring.net)
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
package kieker.analysis.source;

/**
 * POJO for a network pattern consisting of an ip-address and a netmask.
 *
 * @author Reiner Jung
 * @since 1.15
 */
public class NetworkPatternEntry {

	private final byte[] ipAddress;
	private final byte[] mask;

	/**
	 * Create one pattern entry.
	 * 
	 * @param ipAddress
	 *            the ip-address
	 * @param mask
	 *            the network mask
	 */
	public NetworkPatternEntry(final byte[] ipAddress, final byte[] mask) {
		this.ipAddress = ipAddress;
		this.mask = mask;
	}

	public final byte[] getIpAddress() {
		return this.ipAddress;
	}

	public final byte[] getMask() {
		return this.mask;
	}

}
