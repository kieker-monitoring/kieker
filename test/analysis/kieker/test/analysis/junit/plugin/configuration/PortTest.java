package kieker.test.analysis.junit.plugin.configuration;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import kieker.analysis.plugin.configuration.AbstractInputPort;
import kieker.analysis.plugin.configuration.OutputPort;

import org.junit.Test;

/**
 * A simple test suit to ensure the functionality of the ports. More precisely:
 * The test checks whether the output port delivers only objects from the
 * selected types.
 * 
 * @author Nils Christian Ehmke
 */
public class PortTest {

	@Test
	public void testPortsA() {
		final AtomicInteger counter = new AtomicInteger();
		final OutputPort oport = new OutputPort("out", Collections.unmodifiableCollection(new CopyOnWriteArrayList<Class<?>>(
				new Class<?>[] { Number.class, Character.class })));
		final AbstractInputPort iport = new AbstractInputPort(null, null) {

			@Override
			public void newEvent(Object event) {
				assertTrue(Number.class.isInstance(event) ||
						Character.class.isInstance(event));
				counter.incrementAndGet();
			}
		};

		// Connect the ports!
		oport.subscribe(iport);

		// Shouldn't reach the input port!
		oport.deliver(new Object());
		// Should reach the input port
		oport.deliver(new Float(0.0));
		// Should reach the input port
		oport.deliver(new Character('a'));
		// Should reach the input port
		oport.deliver(new Number() {

			private static final long serialVersionUID = 1L;

			@Override
			public long longValue() {
				return 0;
			}

			@Override
			public int intValue() {
				return 0;
			}

			@Override
			public float floatValue() {
				return 0;
			}

			@Override
			public double doubleValue() {
				return 0;
			}
		});
		// Shouldn't reach the input port!
		oport.deliver(new String());

		assertEquals(3, counter.get());
	}

	@Test
	public void testPortsB() {
		final AtomicInteger counter = new AtomicInteger();
		final OutputPort oport = new OutputPort("out", null);
		final AbstractInputPort iport = new AbstractInputPort(null, null) {

			@Override
			public void newEvent(Object event) {
				counter.incrementAndGet();
			}
		};

		// Connect the ports!
		oport.subscribe(iport);

		// Every object should reach the input port

		oport.deliver(new Object());
		oport.deliver(new Float(0.0));
		oport.deliver(new Character('a'));
		oport.deliver(new String());

		assertEquals(4, counter.get());
	}
}
