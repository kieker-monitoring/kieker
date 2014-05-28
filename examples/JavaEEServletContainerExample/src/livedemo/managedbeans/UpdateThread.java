/***************************************************************************
 * Copyright 2013 Kicker Project (http://kicker-monitoring.net)
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

package livedemo.managedbeans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observer;

/**
 * This thread notifies some beans to update their content.
 * 
 * @author Bjoern Weissenfels
 * 
 * @since 1.9
 */
public class UpdateThread extends Thread {

	private final long timeout;
	private final List<Observer> observers;
	private volatile boolean terminated;

	public UpdateThread(final long timeout) {
		this.timeout = timeout;
		this.observers = Collections.synchronizedList(new ArrayList<Observer>());
		this.terminated = false;
	}

	@Override
	public void run() {
		while (!this.terminated) {
			try {
				Thread.sleep(this.timeout);
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
			this.notifyObservers();
		}
	}

	private void notifyObservers() {
		for (final Observer o : this.observers) {
			o.update(null, null);
		}
	}

	public void addObserver(final Observer observer) {
		if (!this.observers.contains(observer)) {
			this.observers.add(observer);
		}
	}

	public void deleteObserver(final Observer observer) {
		this.observers.remove(observer);
	}

	public void terminate() {
		this.terminated = true;
	}

}
