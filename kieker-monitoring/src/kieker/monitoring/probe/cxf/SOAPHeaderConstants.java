/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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

package kieker.monitoring.probe.cxf;

import javax.xml.namespace.QName;

/**
 * This interface contains some constants for the sessionIdentifier soap header.
 *
 * @author Dennis Kieselhorst
 */
public interface SOAPHeaderConstants { // NOPMD NOCS (constants interface)
	/** This is the namespace URI which is used to assembly the qualified names. */
	String NAMESPACE_URI = "http://kieker-monitoring.net";
	/** The local part for the qualified name of the session. */
	String SESSION_QUALIFIED_NAME = "sessionId";
	/** The local part for the qualified name of the trace ID. */
	String TRACE_QUALIFIED_NAME = "traceId";
	/** The local part for the qualified name of the EOI. */
	String EOI_QUALIFIED_NAME = "eoi";
	/** The local part for the qualified name of the ESS. */
	String ESS_QUALIFIED_NAME = "ess";
	/** The qualified name for the session. */
	QName SESSION_IDENTIFIER_QNAME = new QName(NAMESPACE_URI, SESSION_QUALIFIED_NAME);
	/** The qualified name for the trace ID. */
	QName TRACE_IDENTIFIER_QNAME = new QName(NAMESPACE_URI, TRACE_QUALIFIED_NAME);
	/** The qualified name for the EOI. */
	QName EOI_IDENTIFIER_QNAME = new QName(NAMESPACE_URI, EOI_QUALIFIED_NAME);
	/** The qualified name for the ESS. */
	QName ESS_IDENTIFIER_QNAME = new QName(NAMESPACE_URI, ESS_QUALIFIED_NAME);
}
