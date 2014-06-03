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
package kieker.panalysis.stage.stringBuffer;

import java.util.ArrayList;
import java.util.Collection;

import kieker.analysis.plugin.filter.forward.util.KiekerHashMap;
import kieker.panalysis.framework.core.AbstractFilter;
import kieker.panalysis.framework.core.Context;
import kieker.panalysis.framework.core.IInputPort;
import kieker.panalysis.framework.core.IOutputPort;
import kieker.panalysis.stage.stringBuffer.handler.AbstractDataTypeHandler;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class StringBufferFilter extends AbstractFilter<StringBufferFilter> {

	public final IInputPort<StringBufferFilter, Object> objectInputPort = this.createInputPort();

	public final IOutputPort<StringBufferFilter, Object> objectOutputPort = this.createOutputPort();

	// BETTER use a non shared data structure to avoid synchronization between threads
	private KiekerHashMap kiekerHashMap = new KiekerHashMap();

	private Collection<AbstractDataTypeHandler> dataTypeHandlers = new ArrayList<AbstractDataTypeHandler>();

	@Override
	protected boolean execute(final Context<StringBufferFilter> context) {
		final Object object = context.tryTake(this.objectInputPort);
		if (object == null) {
			return false;
		}

		final Object returnedObject = this.handle(object);
		context.put(this.objectOutputPort, returnedObject);

		return true;
	}

	@Override
	public void onPipelineStarts() throws Exception {
		for (final AbstractDataTypeHandler handler : this.dataTypeHandlers) {
			handler.setLogger(this.logger);
			handler.setStringRepository(this.kiekerHashMap);
		}
		super.onPipelineStarts();
	}

	private Object handle(final Object object) {
		for (final AbstractDataTypeHandler handler : this.dataTypeHandlers) {
			if (handler.canHandle(object)) {
				final Object returnedObject = handler.handle(object);
				return returnedObject;
			}
		}
		return object; // else relay given object
	}

	public KiekerHashMap getKiekerHashMap() {
		return this.kiekerHashMap;
	}

	public void setKiekerHashMap(final KiekerHashMap kiekerHashMap) {
		this.kiekerHashMap = kiekerHashMap;
	}

	public Collection<AbstractDataTypeHandler> getDataTypeHandlers() {
		return this.dataTypeHandlers;
	}

	public void setDataTypeHandlers(final Collection<AbstractDataTypeHandler> dataTypeHandlers) {
		this.dataTypeHandlers = dataTypeHandlers;
	}

}
