/***************************************************************************
 * Copyright 2011 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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

package kieker.analysis.plugin.configuration;

import java.util.ArrayList;
import java.util.Collection;

import kieker.analysis.plugin.IAnalysisEvent;

/**
 * 
 * @author Andre van Hoorn
 */
public class OutputPort<T extends IAnalysisEvent> implements IOutputPort<T> {

	// private static final Log log = LogFactory.getLog(OutputPort.class);

	/** Should use "better" data structure from java.concurrent */
	private final Collection<IInputPort<T>> subscriber = new ArrayList<IInputPort<T>>();
	private final String description;

	@SuppressWarnings("unused")
	private OutputPort() {
		this.description = null;
	}

	public OutputPort(final String description) {
		this.description = description;
	}

	public synchronized void deliver(final T event) {
		for (final IInputPort<T> l : this.subscriber) {
			l.newEvent(event);
		}
	}

	@Override
	public synchronized void subscribe(final IInputPort<T> subscriber) {
		this.subscriber.add(subscriber);
	}

	@Override
	public synchronized void unsubscribe(final IInputPort<T> subscriber) {
		this.subscriber.remove(subscriber);
	}

	@Override
	public String getDescription() {
		return this.description;
	}
}
