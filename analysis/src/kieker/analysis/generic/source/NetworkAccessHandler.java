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
package kieker.analysis.generic.source;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The network access handler accepts a list of network patterns which it checks
 * every time acceptRemoteIpAddress is called.
 *
 * Accepted patterns are: ip-address(/[0-9]+)?
 * Whereas ip-address can be IPv4 and IPv6.
 *
 * @author Reiner Jung
 * @since 2.0.0
 */
public class NetworkAccessHandler implements IAccessHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(NetworkPatternEntry.class.getCanonicalName());

	private static final byte[] BYTE_PATTERNS = {
		(byte) 0x00, (byte) 0x80, (byte) 0xc0, (byte) 0xe0, (byte) 0xf0,
		(byte) 0xf8, (byte) 0xfc, (byte) 0xfe, (byte) 0xff };

	private final Collection<NetworkPatternEntry> patterns = new ArrayList<>();

	/**
	 * Create network access handler.
	 *
	 * @param networkPatterns
	 *            list of ip/network patterns specifying allowed IP addresses
	 * @throws UnknownHostException
	 *             if the ip address cannot be resolved
	 */
	public NetworkAccessHandler(final Collection<String> networkPatterns) throws UnknownHostException {
		for (final String pattern : networkPatterns) {
			final String[] parts = pattern.split("/");

			final InetAddress address = InetAddress.getByName(parts[0]);
			final int width;
			if (parts.length == 2) {
				width = Integer.parseInt(parts[1]);
			} else {
				width = address.getAddress().length == 4 ? 32 : 128; // NOCS
			}
			this.patterns.add(new NetworkPatternEntry(address.getAddress(), this.computeMask(address.getAddress().length, width)));
		}
	}

	/**
	 * Compute a bitmask with 'width' bit set from left to right (network byte order).
	 *
	 * @param width
	 *            number of bytes to use
	 * @param maskSetbitCount
	 *            number of bits from left to right to be set to one
	 * @return returns the computer bit mask
	 */
	private byte[] computeMask(final int width, final int maskSetBitCount) {
		final byte[] mask = new byte[width];
		int remainingBitsToSet = maskSetBitCount;

		int i = 0;
		while (remainingBitsToSet > 8) {
			mask[i] = (byte) 0xff;
			remainingBitsToSet -= 8;
			i++;
		}
		if (remainingBitsToSet > 0) {
			mask[i] = BYTE_PATTERNS[remainingBitsToSet];
		}

		return mask;
	}

	@Override
	public boolean acceptRemoteIpAddress(final String remoteIpAddress) {
		try {
			final InetAddress address = InetAddress.getByName(remoteIpAddress);
			for (final NetworkPatternEntry entry : this.patterns) {
				final byte[] patternMask = this.applyMask(entry.getIpAddress(), entry.getMask());
				final byte[] addressMask = this.applyMask(address.getAddress(), entry.getMask());
				if (Arrays.equals(patternMask, addressMask)) {
					return true;
				}
			}
		} catch (final UnknownHostException e) {
			LOGGER.error("Ip address or hostname {} is not found.", remoteIpAddress);
		}

		return false;
	}

	private byte[] applyMask(final byte[] address, final byte[] mask) {
		if (address.length != mask.length) {
			return new byte[mask.length];
		} else {
			final byte[] result = new byte[address.length];
			for (int i = 0; i < address.length; i++) {
				result[i] = (byte) (address[i] & mask[i]);
			}
			return result;
		}
	}
}
