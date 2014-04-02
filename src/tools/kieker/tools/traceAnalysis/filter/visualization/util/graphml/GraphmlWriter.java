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

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;

import org.graphdrawing.graphml.xmlns.GraphmlType;
import org.graphdrawing.graphml.xmlns.ObjectFactory;

/**
 * @author Christian Wulf
 * 
 * @since 1.9
 */
public class GraphmlWriter extends JAXBElementWriter<GraphmlType> {

	private static final String GRAPHML_XML_NAMESPACE = "org.graphdrawing.graphml.xmlns";
	private static final ObjectFactory OBJECT_FACTORY = new ObjectFactory();

	public GraphmlWriter() {
		super(GRAPHML_XML_NAMESPACE);
	}

	public void write(final GraphmlType graphml, final String filename) throws JAXBException {
		final JAXBElement<GraphmlType> jaxbObject = OBJECT_FACTORY.createGraphml(graphml);
		this.writeToFile(jaxbObject, filename);
	}
}
