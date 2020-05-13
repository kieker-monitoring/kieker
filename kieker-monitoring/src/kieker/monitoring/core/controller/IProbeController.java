/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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
import java.util.Map;

/**
 * @author Jan Waller
 *
 * @since 1.6
 */
public interface IProbeController {

	/**
	 * Activates a probe.
	 *
	 * @param pattern
	 *            pattern for the probe
	 * @return true on success
	 *
	 * @since 1.6
	 */
	public boolean activateProbe(final String pattern);

	/**
	 * Deactivates a probe.
	 *
	 * @param pattern
	 *            pattern for the probe
	 * @return true on success
	 *
	 * @since 1.6
	 */
	public boolean deactivateProbe(final String pattern);

	/**
	 * Tests if a probe is active.
	 *
	 * This test is ignorant of the fact whether monitoring itself is
	 * enabled/disabled/terminated.
	 *
	 * @param signature
	 *            signature of the probe
	 * @return true if the probe with this signature is active
	 *
	 * @since 1.6
	 */
	public boolean isProbeActivated(final String signature);

	/**
	 * Overwrites the current list of patterns with a new pattern list.
	 *
	 * @param patternList
	 *            list of strings with patterns where each string starts either with
	 *            a + or -. The list can be empty (in which case the internal
	 *            pattern list and the internal cache are cleared) - but not null.
	 *
	 * @since 1.6
	 */
	public void setProbePatternList(final List<String> patternList);

	/**
	 * Returns the current list of patterns with a prefix indicating whether the
	 * pattern is active or not.
	 *
	 * @return list of strings with patterns where each string starts either with a
	 *         + or -
	 *
	 * @since 1.6
	 */
	public List<String> getProbePatternList();

	/**
	 * Looks up the parameters for a probe /the pattern of the probe and returns
	 * them, may be null.
	 *
	 * @param pattern
	 *            The pattern of the probe.
	 * @return The parameters of the probe, may be null.
	 *
	 * @since 1.14
	 *
	 */
	public Map<String, List<String>> getAllPatternParameters(final String pattern);

	/**
	 * Deletes a parameter from the given pattern.
	 *
	 * @param pattern
	 *            The parameters to be deleted.
	 * @param name
	 *            The name of the parameter.
	 *
	 * @since 1.14
	 *
	 */
	public void deletePatternParameter(final String pattern, String name);

	/**
	 * Deletes all parameters from the given pattern.
	 *
	 * @param pattern
	 *            The parameters to be deleted.
	 *
	 * @since 1.14
	 *
	 */
	public void clearPatternParameters(final String pattern);

	/**
	 * Adds or updates a parameter for the given pattern.
	 *
	 * @param pattern
	 *            The pattern of the probe.
	 * @param name
	 *            The name of the parameter.
	 * @param values
	 *            a List of entries for this parameter.
	 *
	 * @since 1.14
	 */
	public void addPatternParameter(final String pattern, final String name, final List<String> values);

	/**
	 * Add one value to a list of parameter values for a pattern.
	 *
	 * @param pattern
	 *            identifying the pattern for the parameter
	 * @param name
	 *            name of the parameter
	 * @param value
	 *            value to be added to the list of parameter values.
	 *
	 * @since 1.15
	 */
	void addPatternParameterValue(final String pattern, final String name, final String value);

	/**
	 * Remove one value to a list of parameter values for a pattern.
	 *
	 * @param pattern
	 *            identifying the pattern for the parameter
	 * @param name
	 *            name of the parameter
	 * @param value
	 *            value to be removed from the list of parameter values.
	 *
	 * @since 1.15
	 */
	void removePatternParameterValue(final String pattern, final String name, final String value);

}
