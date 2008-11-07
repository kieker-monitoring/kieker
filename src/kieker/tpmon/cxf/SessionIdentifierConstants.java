package kieker.tpmon.cxf;

import javax.xml.namespace.QName;

/**
 * kieker.tpmon.cxf.SessionIdentifierConstants
 *
 * ==================LICENCE=========================
 * Copyright 2006-2008 Matthias Rohr and the Kieker Project
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
 * ==================================================
 * 
 * Constants for the sessionIdentifier soap header.
 * 
 * @author Dennis Kieselhorst
 */
public interface SessionIdentifierConstants {
    public static final String NAMESPACE_URI = "http://kieker.sf.net";
    public static final String QUALIFIED_NAME = "sessionIdentifier";
    public static final QName SESSION_IDENTIFIER_QNAME = new QName(NAMESPACE_URI, QUALIFIED_NAME);
}
