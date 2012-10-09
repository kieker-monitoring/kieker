/***************************************************************************
 * Copyright 2012 Kieker Project (http://kieker-monitoring.net)
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

package kieker.monitoring.core.controller;

import java.util.List;

/**
 * @author Jan Waller
 */
public interface IProbeController {

	/**
	 * Activates a probe.
	 * 
	 * @param string
	 *            pattern for the probe
	 * @return
	 *         true on success
	 */
	public boolean activateProbe(final String pattern);

	/**
	 * Deactivates a probe.
	 * 
	 * @param string
	 *            pattern for the probe
	 * @return
	 *         true on success
	 */
	public boolean deactivateProbe(final String pattern);

	/**
	 * @param string
	 *            signature of the probe
	 * @return
	 *         true if the probe with this signature is active
	 */
	public boolean isProbeActivated(final String signature);

	/**
	 * @param patternList
	 *            list of strings with patterns
	 *            where each string starts either with a + or -
	 */
	public void setProbePatternList(final List<String> patternList);

	/**
	 * @return
	 *         list of strings with patterns
	 *         where each string starts either with a + or -
	 */
	public List<String> getProbePatternList();
}
