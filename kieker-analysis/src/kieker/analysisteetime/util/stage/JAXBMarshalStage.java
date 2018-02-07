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
package kieker.analysisteetime.util.stage;

import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

/**
 * This stage marshals the content tree rooted at an incoming element at the
 * first input port into an output stream at the second input port.
 *
 * A class object has to be passed at creation. Only elements of this type
 * wrapped in a {@code JAXBElement} could be marshaled.
 *
 * @author Sören Henning
 *
 * @since 1.13
 */
public class JAXBMarshalStage<T> extends AbstractBiCombinerStage<JAXBElement<T>, OutputStream> {

	private static final Boolean FORMATTED_OUTPUT_DEFAULT = Boolean.TRUE;

	private final Marshaller marshaller;

	public JAXBMarshalStage(final Class<T> elementsClass) {
		this(elementsClass, FORMATTED_OUTPUT_DEFAULT);
	}

	public JAXBMarshalStage(final Class<T> elementsClass, final Boolean formattedOutput) {
		try {
			this.marshaller = JAXBContext.newInstance(elementsClass).createMarshaller();
			this.marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, formattedOutput);
		} catch (final JAXBException e) {
			// BETTER Consider exception handling
			throw new IllegalStateException(e);
		}
	}

	@Override
	protected void combine(final JAXBElement<T> jaxbElement, final OutputStream outputStream) {

		try {
			this.marshaller.marshal(jaxbElement, outputStream);
		} catch (final JAXBException e) {
			throw new IllegalStateException("The received element could not be marshalled.", e);
		}
	}

}
