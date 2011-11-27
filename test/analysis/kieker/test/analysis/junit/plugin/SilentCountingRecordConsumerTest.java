package kieker.test.analysis.junit.plugin;

import kieker.analysis.configuration.Configuration;
import kieker.analysis.plugin.SilentCountingRecordConsumer;
import kieker.analysis.plugin.port.OutputPort;

import org.junit.Assert;
import org.junit.Test;

/**
 * This test is for the class <i>SilentCountingRecordConsumer</i>.
 * 
 * @author Nils Christian Ehmke
 */
public class SilentCountingRecordConsumerTest {

	@Test
	public void testNormal() {
		/* Establish the connection. */
		final SilentCountingRecordConsumer consumer = new SilentCountingRecordConsumer(new Configuration(null));
		final OutputPort output = new OutputPort("", null);
		output.subscribe(consumer.getAllInputPorts()[0]);

		Assert.assertEquals(0, consumer.getMessageCount());

		/* Now start to send some data. */
		output.deliver(new Object());
		output.deliver(new Object());
		output.deliver(new Object());

		Assert.assertEquals(3, consumer.getMessageCount());
	}

	@Test
	public void testConcurrently() {
		/* Establish the connection. */
		final SilentCountingRecordConsumer consumer = new SilentCountingRecordConsumer(new Configuration(null));
		final OutputPort output = new OutputPort("", null);
		output.subscribe(consumer.getAllInputPorts()[0]);

		Assert.assertEquals(0, consumer.getMessageCount());

		/* Now start some data concurrently. */
		final Thread t1 = new Thread() {
			@Override
			public void run() {
				output.deliver(new Object());
			}
		};
		final Thread t2 = new Thread() {
			@Override
			public void run() {
				output.deliver(new Object());
			}
		};
		final Thread t3 = new Thread() {
			@Override
			public void run() {
				output.deliver(new Object());
			}
		};
		t1.start();
		t2.start();
		t3.start();
		/* Join the threads. */
		try {
			t1.join();
			t2.join();
			t3.join();
		} catch (final InterruptedException e) {
			Assert.fail();
		}
		Assert.assertEquals(3, consumer.getMessageCount());
	}

	@Test
	public void testDifferentClasses() {
		/* Establish the connection. */
		final SilentCountingRecordConsumer consumer = new SilentCountingRecordConsumer(new Configuration(null));
		final OutputPort output = new OutputPort("", null);
		output.subscribe(consumer.getAllInputPorts()[0]);

		Assert.assertEquals(0, consumer.getMessageCount());

		/* Now send some other data. */
		output.deliver(new Long(10));
		output.deliver(null);
		output.deliver("");
		Assert.assertEquals(3, consumer.getMessageCount());
	}

}
