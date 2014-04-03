/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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

package kieker.panalysis.base;

import java.util.List;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class TaskBundle {

	protected final IStage<?> stage;
	protected final List<Object> tasks;

	public TaskBundle(final IStage<?> stage, final List<Object> tasks) {
		this.stage = stage;
		this.tasks = tasks;
	}

	public IStage<?> getStage() {
		return this.stage;
	}

	public List<Object> getTasks() {
		return this.tasks;
	}
}
