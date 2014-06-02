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
package kieker.panalysis.framework.concurrent;

import java.util.Collection;

import kieker.panalysis.framework.core.IPipeline;
import kieker.panalysis.framework.core.IStage;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class StageWorkArrayList implements IStageWorkList {

	private final IPipeline pipeline;
	private final int accessesDeviceId;

	/**
	 * @since 1.10
	 */
	public StageWorkArrayList(final IPipeline pipeline, final int accessesDeviceId) {
		this.pipeline = pipeline;
		this.accessesDeviceId = accessesDeviceId;
	}

	public void pushAll(final Collection<? extends IStage> stages) {
		// TODO Auto-generated method stub

	}

	private boolean isValid(final IStage stage) {
		final boolean isValid = (stage.getAccessesDeviceId() == this.accessesDeviceId);
		if (!isValid) {
			// LOG.warn("Invalid stage: stage.accessesDeviceId = " + stage.getAccessesDeviceId() + ", accessesDeviceId = " + this.accessesDeviceId + ", stage = " +
			// stage);
		}
		return isValid;
	}

	public IStage pop() {
		// TODO Auto-generated method stub
		return null;
	}

	public IStage read() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

}
