.. _tutorials-how-to-write-tests-for-your-own-kieker-probes:

How to Write Tests for Your own Kieker Probes 
=============================================

Writing your own probes with Kieker is quite simple. However, testing
them requires additional insight into Kieker which require reading a lot
of source code. As this is an unpleasant task, I collected some basic
ideas in this how-to.

Let say you have a written a probe ``ExampleProbe``:

.. code-block:: java
	:linenos:
		
	import kieker.common.record.IMonitoringRecord;
	import kieker.common.record.flow.trace.TraceMetadata;
	import kieker.common.record.flow.trace.operation.BeforeOperationEvent;
	import kieker.monitoring.core.controller.IMonitoringController;
	import kieker.monitoring.core.controller.MonitoringController;
	import kieker.monitoring.core.registry.TraceRegistry;
	
	public class ExampleProbe {
		private final IMonitoringController ctrl = MonitoringController.getInstance();
		private final TraceRegistry registry = TraceRegistry.INSTANCE;
		
		public ExampleProbe() {
		}

		public void takeMeasurement(final String operationSignature,
			final String classSignature) {
			
			/** collect event data. */
			final TraceMetadata trace = this.registry.getTrace();
			final long timestamp = this.ctrl.getTimeSource().getTime();
			final long traceId = trace.getTraceId();
			final int orderIndex = trace.getNextOrderId();

			/** create event. */
			final IMonitoringRecord event = new BeforeOperationEvent(timestamp, 
				traceId, orderIndex, operationSignature,
				classSignature);

			/** log event. */
			this.ctrl.newMonitoringRecord(event);
		}
	}

When you use this in an application, the ``IMonitoringController`` will
refer to a singleton within the application, which is great within an
application. You can pass a configuration via a file at a default
location or by specifying an environment variable. Unfortunately, this
makes is more complicated for testers to pass their configuration to the
controller. In case you instantiate a controller in the test class, it
will create a separate controller for the test class. Fortunately, there
is a way around this limitation. As the ``MonitoringController`` factory
method checks on environment variables, you can set them in a test
statically. Therefore, they are set before creating the first
``MonitoringController``.

.. code-block:: java
	:linenos:
	
	package example.probe.test;
	
	import kieker.monitoring.core.configuration.ConfigurationKeys;
	import org.junit.Test;

	public class ExampleProbeTest {
		
		/**
		 * Set system properties before instantiation anything. 
		 * Otherwise the MonitoringController will not see the
		 * configuration.
		 */
		static {
			System.setProperty(ConfigurationKeys.CONTROLLER_NAME, "ExampleProbeTest Controller");
			
			System.setProperty(ConfigurationKeys.WRITER_CLASSNAME,
				TestDummyWriter.class.getCanonicalName());
		}
		
		@Test
		public void test() {
			final ExampleProbe probe = new ExampleProbe();
			probe.takeMeasurement("myOperation()", "example.ExampleClass");
			
			/** first record. */
			final IMonitoringRecord metadata = TestDummyWriter.getEvents().get(0);
			
			Assert.assertEquals("First record should be KiekerMetaData",
				metadata.getClass().getName(),
				KiekerMetadataRecord.class.getName());

			/** second record. */
			final IMonitoringRecord beforeEvent =
				TestDummyWriter.getEvents().get(1);

			Assert.assertEquals("First record should be KiekerMetaData",
				beforeEvent.getClass().getName(),

			BeforeOperationEvent.class.getName());
		}
	}

In this test class, we set two properties. Firstly, we specify a
controller name. This helps when debugging tests, as we can check
whether the used controller is really the one with the internal name
"ExampleProbeTest Controller". Secondly, we set the writer class. By
default Kieker would write into a text log file. However, during testing
we do not want that Kieker creates a directory and stores log
information there. Instead we want to access logged data
programmatically. The ``TestDummyWriter`` allows to access events from a
statically defined internal list, which is most convenient for testing.
The list is statically accessed with ``TestDummyWriter.getEvents()``.
The first event is always ``KiekerMetadataRecord``, except you configure
the controller to omit the metadata record.

Based on this simple setup, you can test your own probes easily. Please
note, currently the ``TestDummyWriter`` is still part of iObserve and will
move to Kieker in the near future.
