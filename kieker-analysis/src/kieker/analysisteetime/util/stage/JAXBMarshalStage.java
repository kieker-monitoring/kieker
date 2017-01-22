package kieker.analysisteetime.util.stage;

import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

/**
 * This stage marshals the content tree rooted at an incoming element at the
 * first input port into an output stream at the second input port.
 *
 * A class object has to be passed at creation. Only elements of this type
 * wrapped in a {@code JAXBElement} could be marshaled.
 *
 * @author Sören Henning
 *
 */
public class JAXBMarshalStage<T> extends AbstractBiCombinerStage<JAXBElement<T>, OutputStream> {

	private static final Boolean FORMATTED_OUTPUT_DEFAULT = Boolean.TRUE;

	private final Marshaller marshaller;

	public JAXBMarshalStage(final Class<T> elementsClass) {
		this(elementsClass, FORMATTED_OUTPUT_DEFAULT);
	}

	public JAXBMarshalStage(final Class<T> elementsClass, final Boolean formattedOutput) {
		try {
			this.marshaller = JAXBContext.newInstance(elementsClass).createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, formattedOutput);
		} catch (JAXBException e) {
			// TODO Exception
			throw new IllegalStateException(e);
		}
	}

	@Override
	protected void combine(final JAXBElement<T> jaxbElement, final OutputStream outputStream) {

		try {
			marshaller.marshal(jaxbElement, outputStream);
		} catch (JAXBException e) {
			// TODO Exception
			throw new IllegalStateException("The received element could not be marshalled.", e);
		}
	}

}
