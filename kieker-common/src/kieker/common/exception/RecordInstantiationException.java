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

package kieker.common.exception;

/**
 * Only thrown by the {@link kieker.common.record.factory.old.RecordFactoryWrapper}.
 *
 * @author Christian Wulf
 * @author Reiner Jung - add format string support
 *
 * @since 1.10
 */
public class RecordInstantiationException extends RuntimeException {

	private static final long serialVersionUID = -2968478850093576098L;

	public RecordInstantiationException(final Throwable throwable) {
		super(throwable);
	}

	/**
	 * Creates a new instance of a exception utilizing a format string.
	 *
	 * @param format
	 *            format string
	 * @param arguments
	 *            arguments used for the format string
	 */
	public RecordInstantiationException(final String format, final Object... arguments) {
		super(String.format(format, arguments));
	}

}
