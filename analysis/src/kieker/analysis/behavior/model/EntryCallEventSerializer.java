/***************************************************************************
 * Copyright (C) 2017 iObserve Project (https://www.iobserve-devops.net)
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
package kieker.analysis.behavior.model;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import kieker.analysis.behavior.events.EntryCallEvent;

/**
 * A serializer, which serializes an PayloadAwareEntryCallEvent by printing the operation signature,
 * the parameters and the values.
 *
 * @author Lars Jürgensen
 * @since 2.0.0
 */
public class EntryCallEventSerializer extends StdSerializer<EntryCallEvent> {

	@SuppressWarnings("unused")
	private static final long serialVersionUID = -1964014516300428956L;

	public EntryCallEventSerializer() {
		super(EntryCallEvent.class);
	}

	@Override
	public void serialize(final EntryCallEvent event, final JsonGenerator jsonGenerator,
			final SerializerProvider provider) throws IOException, JsonGenerationException {
		jsonGenerator.writeStartObject();
		jsonGenerator.writeStringField("operationSignature", event.getOperationSignature());
		jsonGenerator.writeObjectField("parameters", event.getParameters());
		jsonGenerator.writeObjectField("values", event.getValues());
		jsonGenerator.writeEndObject();

	}

}
