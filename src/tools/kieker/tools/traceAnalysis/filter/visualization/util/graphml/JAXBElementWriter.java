package kieker.tools.traceAnalysis.filter.visualization.util.graphml;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

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

	public void write(final JAXBElement<T> jaxbObject, final String filename) throws JAXBException {
		this.marshaller.marshal(jaxbObject, new File(filename));
	}

}
