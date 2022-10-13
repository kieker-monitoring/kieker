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
package kieker.analysis.behavior.acceptance.matcher;

import kieker.analysis.behavior.data.EntryCallEvent;

/**
 * Interface for EntryCallEvent acceptance matcher. Not every EntryCallEvent might represent and
 * relevant behavior trace. For example, returning images or CSS is usually not part of the user behavior
 * which is relevant to us. Therefore, such matcher can be passed to the TraceAcceptanceFilter to
 * which then only forwards accepted calls.
 *
 * @author Reiner Jung
 * @since 2.0.0
 */
public interface IEntryCallAcceptanceMatcher {

	/**
	 * Match whether the call is correct of interest for the application.
	 *
	 * @param call
	 *            one entry call event
	 * @return true on success
	 */
	boolean match(final EntryCallEvent call);
}
