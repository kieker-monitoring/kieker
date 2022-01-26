/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Collection of alarms.
 *
 * @author Reiner Jung
 *
 * @since 1.15
 */
public class Alarms implements IErrorMessages {
	private List<String> messages = new ArrayList<>();
	private Date date;

	/**
	 * Create an alarm result.
	 */
	public Alarms() {
		// empty default constructor
	}

	@Override
	public final List<String> getMessages() {
		return this.messages;
	}

	public void setMessages(final List<String> messages) {
		this.messages = messages;
	}

	@Override
	public void addMessage(final String message) {
		this.messages.add(message);
	}

	@Override
	public Date getDate() {
		return this.date;
	}

	@Override
	public void setDate(final Date date) {
		this.date = date;
	}
}
