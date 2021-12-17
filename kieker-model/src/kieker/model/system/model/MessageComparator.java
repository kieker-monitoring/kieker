/***************************************************************************
 * Copyright 2021 Kieker Project (http://kieker-monitoring.net)
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

package kieker.model.system.model;

import java.io.Serializable;
import java.util.Comparator;

/**
 * @author Christian Wulf (chw)
 *
 * @since 1.14
 */
class MessageComparator implements Comparator<AbstractMessage>, Serializable {

	/**
	 * 8697359317711222757L for 1.14.
	 */
	private static final long serialVersionUID = 8697359317711222757L;

	public MessageComparator() {
		super();
	}

	@Override
	public int compare(final AbstractMessage o1, final AbstractMessage o2) {
		return Long.compare(o1.getTimestamp(), o2.getTimestamp());
	}

}
