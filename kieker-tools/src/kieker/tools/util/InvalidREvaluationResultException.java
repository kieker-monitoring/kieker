/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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

package kieker.tools.util;

/**
 * Exception that states, that the results from a R evaluation are invalid.
 *
 * @author Thomas Duellmann
 *
 * @since 1.11
 */
public class InvalidREvaluationResultException extends NullPointerException {
	private static final long serialVersionUID = 2368292926336488354L;

	public InvalidREvaluationResultException(final String message) {
		super(message);
	}
}
