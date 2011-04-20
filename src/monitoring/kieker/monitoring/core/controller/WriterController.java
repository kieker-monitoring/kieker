package kieker.monitoring.core.controller;

import java.util.concurrent.atomic.AtomicLong;

import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.configuration.Configuration;
import kieker.monitoring.timer.ITimeSource;
import kieker.monitoring.writer.IMonitoringWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Andre van Hoorn, Matthias Rohr, Jan Waller, Robert von Massow
 */
public final class WriterController extends AbstractController implements IWriterController {
	private static final Log log = LogFactory.getLog(WriterController.class);

	/**
	 * Used to track the total number of monitoring records received while the
	 * controller has been enabled
	 */
	private final AtomicLong numberOfInserts = new AtomicLong(0);
	/** Monitoring Writer */
	private final IMonitoringWriter monitoringWriter;
	/** the ITimeSource used by this instance */
	private final ITimeSource timeSource;
	
	private final StateController stateController;

	public WriterController(final Configuration configuration, final StateController stateController) {
		this.stateController = stateController;
		this.timeSource = createAndInitialize(ITimeSource.class, configuration.getStringProperty(Configuration.TIMER_CLASSNAME), configuration);
		if (this.timeSource == null) {
			this.monitoringWriter = null;
			terminate();
			return;
		}
		this.monitoringWriter = createAndInitialize(IMonitoringWriter.class, configuration.getStringProperty(Configuration.WRITER_CLASSNAME), configuration);
		if (this.monitoringWriter == null) {
			terminate();
			return;
		}
	}

	@Override
	protected final void cleanup() {
		stateController.terminate();
		WriterController.log.info("Shutting down Writer Controller");
		if (this.monitoringWriter != null) {
			this.monitoringWriter.terminate();
		}
	}

	@Override
	protected final void getState(final StringBuilder sb) {
		sb.append("WriterController:\n\tTime Source: '");
		sb.append(this.timeSource.getClass().getName());
		sb.append("'; Number of Inserts: '");
		sb.append(this.getNumberOfInserts());
		sb.append("'\n");
		if (this.monitoringWriter != null) {
			sb.append(this.monitoringWriter.getInfoString());
		} else {
			sb.append("No Monitoring Writer available");
		}
		sb.append("\n");
	}

	@Override
	public final boolean newMonitoringRecord(final IMonitoringRecord record) {
		try {
			if (!stateController.isMonitoringEnabled()) { // enabled and not terminated
				return false;
			}
			record.setLoggingTimestamp(timeSource.currentTimeNanos());
			numberOfInserts.incrementAndGet();
			final boolean successfulWriting = this.monitoringWriter.newMonitoringRecord(record);
			if (!successfulWriting) {
				WriterController.log.fatal("Error writing the monitoring data. Will terminate monitoring!");
				this.terminate();
				return false;
			}
			return true;
		} catch (final Throwable ex) {
			WriterController.log.fatal("Exception detected. Will terminate monitoring", ex);
			this.terminate();
			return false;
		}
	}

	@Override
	public final IMonitoringWriter getMonitoringWriter() {
		return this.monitoringWriter;
	}

	@Override
	public final long getNumberOfInserts() {
		return this.numberOfInserts.longValue();
	}

	@Override
	public ITimeSource getTimeSource() {
		return this.timeSource;
	}

	@SuppressWarnings("unchecked")
	public final static <C> C createAndInitialize(final Class<C> c, final String classname, final Configuration configuration) {
		C createdClass = null;
		try {
			final Class<?> clazz = Class.forName(classname);
			if (c.isAssignableFrom(clazz)) {
				createdClass = (C) clazz.getConstructor(Configuration.class).newInstance(configuration.getPropertiesStartingWith(classname));
			} else {
				WriterController.log.error("Class '" + classname + "' has to implement '" + c.getSimpleName() + "'");
			}
		} catch (final ClassNotFoundException e) {
			WriterController.log.error(c.getSimpleName() + ": Class '" + classname + "' not found", e);
		} catch (final NoSuchMethodException e) {
			WriterController.log.error(c.getSimpleName() + ": Class '" + classname + "' has to implement a constructor that accepts a single Configuration", e);
		} catch (final Throwable e) { // SecurityException, IllegalAccessException, IllegalArgumentException, InstantiationException, InvocationTargetException
			WriterController.log.error(c.getSimpleName() + ": Failed to load class for name '" + classname + "'", e);
		}
		return createdClass;
	}
}
