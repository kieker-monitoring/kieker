package kieker.test.monitoring.junit.probe.cxf.executions.bookstore;

import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * 
 * @author Marius Loewe
 *
 */
@WebService
public interface IBookstore {

	public abstract String searchBook(@WebParam(name = "term") String term);

}