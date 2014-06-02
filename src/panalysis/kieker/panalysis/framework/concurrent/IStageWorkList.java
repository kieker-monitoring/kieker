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

import kieker.panalysis.framework.core.IOutputPort;
import kieker.panalysis.framework.core.IStage;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public interface IStageWorkList {

	public abstract void pushAll(Collection<? extends IStage> stages);

	public abstract void pushAll(IOutputPort<?, ?>[] outputPorts);

	public abstract IStage pop();

	public abstract IStage read();

	public abstract boolean isEmpty();

}
