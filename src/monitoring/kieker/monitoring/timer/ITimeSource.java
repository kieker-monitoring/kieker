/***************************************************************************
 * Copyright 2013 Kieker Project (http://kieker-monitoring.net)
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

package kieker.monitoring.timer;

/**
 * @author Jan Waller
 * 
 * @since 1.3
 */
public interface ITimeSource {

	/**
	 * @return The timestamp for the current time. Usually, the timestamp is assumed to be in {@link java.util.concurrent.TimeUnit#NANOSECONDS}.
	 * 
	 * 
	 * @since 1.3
	 */
	public long getTime();

	/**
	 * @return A String representation of the {@link java.util.concurrent.TimeUnit} of the timesource.
	 * 
	 * @since 1.7
	 */
	public String getTimeUnit();

	/**
	 * @return A String representation of the timesource. E.g., the meaning of a timestamp from this source.
	 * 
	 * @since 1.5
	 */
	public String toString();
}
