package kieker.examples.userguide.cxfsoap;

import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public interface Bookstore {

	public abstract Book searchBook(@WebParam(name = "term") String term);

}