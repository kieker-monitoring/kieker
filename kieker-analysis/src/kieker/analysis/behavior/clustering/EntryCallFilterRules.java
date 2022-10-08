/***************************************************************************
 * Copyright (C) 2017 iObserve Project (https://www.iobserve-devops.net)
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
package kieker.analysis.behavior.clustering;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import kieker.analysis.behavior.data.EntryCallEvent;

/**
 *
 * @author Christoph Dornieden
 * @since 2.0.0
 */
public class EntryCallFilterRules {

	private final List<Pattern> filterList;
	private final boolean blackListMode;

	/**
	 * constructor.
	 *
	 * @param blackListMode
	 *            blackListMode
	 */
	public EntryCallFilterRules(final boolean blackListMode) {
		this.filterList = new ArrayList<>();
		this.blackListMode = blackListMode;
	}

	/**
	 * constructor.
	 *
	 * @param filterList
	 *            filterList
	 * @param blackListMode
	 *            blackListMode
	 */
	public EntryCallFilterRules(final List<String> filterList, final boolean blackListMode) {
		this.filterList = filterList.stream().map(Pattern::compile).collect(Collectors.toList());
		this.blackListMode = blackListMode;
	}

	/**
	 * Checks whether a signature of an entryCallEvent is allowed or not.
	 *
	 * @param entryCallEvent
	 *            an single entry call event
	 * @return true if signature is allowed, false else
	 */
	public boolean isAllowed(final EntryCallEvent entryCallEvent) {
		return this.isAllowed(entryCallEvent.getOperationSignature());
	}

	/**
	 * Checks whether a signature is allowed or not.
	 *
	 * @param signature
	 *            signature
	 * @return true if signature is allowed, false else
	 */
	public boolean isAllowed(final String signature) {
		boolean isAllowed = this.blackListMode;

		for (final Pattern filterRule : this.filterList) {

			final boolean isMatch = filterRule.matcher(signature).matches();
			isAllowed = this.blackListMode ? !isMatch && isAllowed : isAllowed || isMatch;
		}
		return isAllowed;
	}

	/**
	 * Add filter rule.
	 *
	 * @param regex
	 *            regex matching signatures
	 * @return true if regex could be added, false else
	 */
	public EntryCallFilterRules addFilterRule(final String regex) {
		this.filterList.add(Pattern.compile(regex));
		return this;
	}

	public List<Pattern> getFilterList() {
		return this.filterList;
	}

	public boolean isBlackListMode() {
		return this.blackListMode;
	}

}
