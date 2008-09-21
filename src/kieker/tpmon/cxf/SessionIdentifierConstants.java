package kieker.tpmon.cxf;

import javax.xml.namespace.QName;

/**
 * Constants for the sessionIdentifier soap header.
 * 
 * @author Dennis Kieselhorst
 *
 */
public interface SessionIdentifierConstants {
  public static final String NAMESPACE_URI = "http://kieker.sf.net";
  public static final String QUALIFIED_NAME = "sessionIdentifier";
  public static final QName SESSION_IDENTIFIER_QNAME = new QName(NAMESPACE_URI, QUALIFIED_NAME);
}
