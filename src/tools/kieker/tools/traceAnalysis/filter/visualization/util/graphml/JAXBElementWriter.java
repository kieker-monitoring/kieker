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

package kieker.tools.traceAnalysis.filter.visualization.util.graphml;

import java.io.File;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

/**
 * @author Christian Wulf
 * 
 * @since 1.9
 * 
 * @param <T>
 */
public class JAXBElementWriter<T> {

	private Marshaller marshaller;

	public JAXBElementWriter(final String contextPath) {
		try {
			final JAXBContext context = JAXBContext.newInstance(contextPath);
			this.marshaller = context.createMarshaller();
			this.marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		} catch (final JAXBException e) {
			throw new IllegalStateException(e);
		}
	}

	public void writeToFile(final JAXBElement<T> jaxbObject, final String filename) throws JAXBException {
		this.marshaller.marshal(jaxbObject, new File(filename));
	}

	public String writeToString(final JAXBElement<T> jaxbObject) throws JAXBException {
		final StringWriter stringWriter = new StringWriter();
		this.marshaller.marshal(jaxbObject, stringWriter);
		return stringWriter.toString();
	}

}
