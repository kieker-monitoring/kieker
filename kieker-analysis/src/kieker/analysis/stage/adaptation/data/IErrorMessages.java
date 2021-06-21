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
package kieker.analysis.stage.adaptation.data;

import java.util.Date;
import java.util.List;

/**
 * Represents messages that contain errors warnings and so on that may occur during the execution of
 * the analysis.
 *
 * @author Marc Adolf
 * @since 1.15
 */
public interface IErrorMessages {

	/**
	 * Get date of error messages.
	 *
	 * @return date of the messages
	 */
	Date getDate();

	/**
	 * Set date of error messages.
	 *
	 * @param date
	 *            of the messages
	 */
	void setDate(Date date);

	/**
	 * Returns the list of saved messages.
	 *
	 * @return the contained (error) messages.
	 */
	List<String> getMessages();

	/**
	 * Adds one message to the list.
	 *
	 * @param message
	 *            the new message in the list.
	 */
	void addMessage(String message);

}
