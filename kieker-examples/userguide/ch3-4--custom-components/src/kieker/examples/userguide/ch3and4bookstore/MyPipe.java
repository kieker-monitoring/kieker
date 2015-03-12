/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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

package kieker.examples.userguide.ch3and4bookstore;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class MyPipe {

	private final String pipeName;
	private final LinkedBlockingQueue<PipeData> buffer =
			new LinkedBlockingQueue<PipeData>();

	public MyPipe(final String pipeName) {
		this.pipeName = pipeName;
	}

	public String getPipeName() {
		return this.pipeName;
	}

	public void put(final PipeData data) throws InterruptedException {
		this.buffer.put(data);
	}

	public PipeData poll(final long timeout) throws InterruptedException {
		return this.buffer.poll(timeout, TimeUnit.SECONDS);
	}

}
