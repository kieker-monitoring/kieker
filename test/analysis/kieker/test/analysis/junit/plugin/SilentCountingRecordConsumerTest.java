package kieker.test.analysis.junit.plugin;

import kieker.analysis.plugin.AbstractAnalysisPlugin;
import kieker.analysis.plugin.AbstractPlugin;
import kieker.analysis.plugin.SilentCountingRecordConsumer;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.common.configuration.Configuration;

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
		final SilentCountingRecordConsumer consumer = new SilentCountingRecordConsumer(new Configuration());
		final SourceClass src = new SourceClass();
		Assert.assertTrue(AbstractPlugin.connect(src, SourceClass.OUTPUT_PORT_NAME, consumer, SilentCountingRecordConsumer.INPUT_PORT_NAME));

		Assert.assertEquals(0, consumer.getMessageCount());

		/* Now start to send some data. */
		src.deliver(new Object());
		src.deliver(new Object());
		src.deliver(new Object());

		Assert.assertEquals(3, consumer.getMessageCount());
	}

	@Test
	public void testConcurrently() {
		/* Establish the connection. */
		final SilentCountingRecordConsumer consumer = new SilentCountingRecordConsumer(new Configuration());
		final SourceClass src = new SourceClass();
		Assert.assertTrue(AbstractPlugin.connect(src, SourceClass.OUTPUT_PORT_NAME, consumer, SilentCountingRecordConsumer.INPUT_PORT_NAME));

		Assert.assertEquals(0, consumer.getMessageCount());

		/* Now start some data concurrently. */
		final Thread t1 = new Thread() {
			@Override
			public void run() {
				src.deliver(new Object());
			}
		};
		final Thread t2 = new Thread() {
			@Override
			public void run() {
				src.deliver(new Object());
			}
		};
		final Thread t3 = new Thread() {
			@Override
			public void run() {
				src.deliver(new Object());
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
		final SilentCountingRecordConsumer consumer = new SilentCountingRecordConsumer(new Configuration());
		final SourceClass src = new SourceClass();
		Assert.assertTrue(AbstractPlugin.connect(src, SourceClass.OUTPUT_PORT_NAME, consumer, SilentCountingRecordConsumer.INPUT_PORT_NAME));

		Assert.assertEquals(0, consumer.getMessageCount());

		/* Now send some other data. */
		src.deliver(Long.valueOf(10));
		src.deliver(null); // should not be delivered
		src.deliver("");
		Assert.assertEquals(2, consumer.getMessageCount());
	}

	@Plugin(
			outputPorts = {
				@OutputPort(name = SourceClass.OUTPUT_PORT_NAME, eventTypes = {})
			})
	static class SourceClass extends AbstractAnalysisPlugin {

		public static final String OUTPUT_PORT_NAME = "output";

		public SourceClass() {
			super(new Configuration());
		}

		public void deliver(final Object data) {
			if (data != null) {
				Assert.assertTrue(super.deliver(SourceClass.OUTPUT_PORT_NAME, data));
			} else {
				Assert.assertFalse(super.deliver(SourceClass.OUTPUT_PORT_NAME, data));
			}
		}

		@Override
		protected Configuration getDefaultConfiguration() {
			return new Configuration();
		}

		@Override
		public Configuration getCurrentConfiguration() {
			return new Configuration();
		}
	}
}
