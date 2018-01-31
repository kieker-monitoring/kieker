/***************************************************************************
* Copyright 2018 Kieker Project (http://www.iobserve-devops.net)
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
***************************************************************************/
package kieker.common.record.remotecontrol;


import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;

/**
 * @author Marc Adolf
 * 
 * @since 1.14
 */
public final class ActivationEventFactory implements IRecordFactory<ActivationEvent> {
	
	
	@Override
	public ActivationEvent create(final IValueDeserializer deserializer) {
		return new ActivationEvent(deserializer);
	}
	
	@Override
	@Deprecated
	public ActivationEvent create(final Object[] values) {
		return new ActivationEvent(values);
	}
	
	public int getRecordSizeInBytes() {
		return ActivationEvent.SIZE;
	}
}
