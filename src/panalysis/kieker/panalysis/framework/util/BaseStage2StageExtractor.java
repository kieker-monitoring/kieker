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
package kieker.panalysis.framework.util;

import java.util.LinkedList;
import java.util.List;

import kieker.panalysis.framework.core.CompositeFilter;
import kieker.panalysis.framework.core.IBaseStage;
import kieker.panalysis.framework.core.IStage;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class BaseStage2StageExtractor {

	public List<? extends IStage> extract(final IBaseStage baseStage) {
		final List<IStage> stages = new LinkedList<IStage>();
		this.extractLocally(baseStage, stages);
		return stages;
	}

	private void extractLocally(final IBaseStage baseStage, final List<IStage> stages) {
		if (baseStage instanceof IStage) {
			stages.add((IStage) baseStage);
		} else if (baseStage instanceof CompositeFilter) {
			final List<IBaseStage> schedulableStages = ((CompositeFilter) baseStage).getSchedulableStages();
			for (final IBaseStage childBaseStage : schedulableStages) {
				this.extractLocally(childBaseStage, stages);
			}
		}
	}

}
