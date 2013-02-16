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

package kieker.common.namedRecordPipe;

/**
 * This exception shows that something with respective to a pipe failed.
 * 
 * @author Andre van Hoorn
 */
// TODO It seems to me that there is no reference to this exception in Kieker. Do we need it?
public class PipeException extends Exception {
	/** The UID. */
	private static final long serialVersionUID = 56L;

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param msg
	 *            The message of this exception.
	 */
	public PipeException(final String msg) {
		super(msg);
	}

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param msg
	 *            The message of this exception.
	 * @param thrw
	 *            The cause of this exception.
	 */
	public PipeException(final String msg, final Throwable thrw) {
		super(msg, thrw);
	}
}
