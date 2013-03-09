/***************************************************************************
 * Copyright 2013 Kieker Project (http://kieker-monitoring.net)
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
 * Constants for the sessionIdentifier soap header.
 */

/**
 * @author Dennis Kieselhorst
 */
public interface SOAPHeaderConstants { // NOPMD NOCS (constants interface)
	public static final String NAMESPACE_URI = "http://kieker-monitoring.net";
	public static final String SESSION_QUALIFIED_NAME = "sessionId";
	public static final String TRACE_QUALIFIED_NAME = "traceId";
	public static final String EOI_QUALIFIED_NAME = "eoi";
	public static final String ESS_QUALIFIED_NAME = "ess";
	public static final QName SESSION_IDENTIFIER_QNAME = new QName(NAMESPACE_URI, SESSION_QUALIFIED_NAME);
	public static final QName TRACE_IDENTIFIER_QNAME = new QName(NAMESPACE_URI, TRACE_QUALIFIED_NAME);
	public static final QName EOI_IDENTIFIER_QNAME = new QName(NAMESPACE_URI, EOI_QUALIFIED_NAME);
	public static final QName ESS_IDENTIFIER_QNAME = new QName(NAMESPACE_URI, ESS_QUALIFIED_NAME);
}
