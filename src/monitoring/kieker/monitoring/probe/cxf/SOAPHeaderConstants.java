package kieker.monitoring.probe.cxf;

import javax.xml.namespace.QName;

/**
 * Constants for the sessionIdentifier soap header.
 */

/**
 * @author Dennis Kieselhorst
 */
public interface SOAPHeaderConstants {
    public static final String NAMESPACE_URI = "http://kieker.sf.net";
    public static final String SESSION_QUALIFIED_NAME = "sessionId";
    public static final String TRACE_QUALIFIED_NAME = "traceId";
    public static final String EOI_QUALIFIED_NAME = "eoi";
    public static final String ESS_QUALIFIED_NAME = "ess";
    public static final QName SESSION_IDENTIFIER_QNAME = new QName(NAMESPACE_URI, SESSION_QUALIFIED_NAME);
    public static final QName TRACE_IDENTIFIER_QNAME = new QName(NAMESPACE_URI, TRACE_QUALIFIED_NAME);
    public static final QName EOI_IDENTIFIER_QNAME = new QName(NAMESPACE_URI, EOI_QUALIFIED_NAME);
    public static final QName ESS_IDENTIFIER_QNAME = new QName(NAMESPACE_URI, ESS_QUALIFIED_NAME);
}
