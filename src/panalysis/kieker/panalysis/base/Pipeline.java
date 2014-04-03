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

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class Pipeline {

	private final List<Stage<?>> stages = new LinkedList<Stage<?>>();
	private int freeId = 0;

	public void addStage(final Stage<?> stage) {
		stage.setId(this.freeId++);
		this.stages.add(stage);
	}

	public void start() {
		this.stages.get(0).execute();
	}

	public Collection<Stage<?>> getStages() {
		return this.stages;
	}
}
