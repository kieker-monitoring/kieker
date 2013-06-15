package kieker.analysis.plugin.reader;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.system.CPUUtilizationRecord;
import kieker.common.record.system.MemSwapUsageRecord;

@Plugin(
		outputPorts = {
			@OutputPort(
					name = CPUAndMemSwapRecordsLoadDriver.OUTPUT_PORT_NAME_RECORDS,
					eventTypes = { IMonitoringRecord.class }) },
		configuration = {
			@Property(name = CPUAndMemSwapRecordsLoadDriver.CONFIG_PROPERTY_NAME_NUMBER_DATA_SETS,
					defaultValue = CPUAndMemSwapRecordsLoadDriver.CONFIG_PROPERTY_VALUE_NUMBER_DATA_SETS),
			@Property(name = CPUAndMemSwapRecordsLoadDriver.CONFIG_PROPERTY_NAME_NUMBER_CPUS,
					defaultValue = CPUAndMemSwapRecordsLoadDriver.CONFIG_PROPERTY_VALUE_NUMBER_CPUS) })
public class CPUAndMemSwapRecordsLoadDriver extends AbstractReaderPlugin {

	public static final java.lang.String OUTPUT_PORT_NAME_RECORDS = "monitoringRecords";

	public static final java.lang.String CONFIG_PROPERTY_NAME_NUMBER_DATA_SETS = "numberDataSets";
	public static final java.lang.String CONFIG_PROPERTY_VALUE_NUMBER_DATA_SETS = "1000";

	public static final java.lang.String CONFIG_PROPERTY_NAME_NUMBER_CPUS = "numberCPUs";
	public static final java.lang.String CONFIG_PROPERTY_VALUE_NUMBER_CPUS = "4";

	private static final Log LOG = LogFactory.getLog(CPUAndMemSwapRecordsLoadDriver.class);
	private static final long COOLDOWN_TIME_MS = 5 * 60 * 1000; // 5 Minutes

	private final long numberDataSets;
	private final int numberCPUs;

	public CPUAndMemSwapRecordsLoadDriver(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);

		this.numberDataSets = configuration.getLongProperty(CONFIG_PROPERTY_NAME_NUMBER_DATA_SETS);
		this.numberCPUs = configuration.getIntProperty(CONFIG_PROPERTY_NAME_NUMBER_CPUS);
	}

	public boolean read() {
		for (int j = 0; j < 2; j++) {
			for (long i = 0; i < this.numberDataSets; i++) {
				for (int cpuID = 0; cpuID < this.numberCPUs; cpuID++) {
					super.deliver(OUTPUT_PORT_NAME_RECORDS, this.createNextCPURecord(cpuID, i));
				}
				super.deliver(OUTPUT_PORT_NAME_RECORDS, this.createNextMemSwapRecord(i));
			}

			// Begin a cooldown phase before starting with the next records
			if (j == 0) {
				LOG.info("Begin of cooldown phase");
				try {
					Thread.sleep(COOLDOWN_TIME_MS);
				} catch (final InterruptedException ex) {
					LOG.error("Interrupted Exception", ex);
				}
				LOG.info("End of cooldown phase");
			}
		}

		return true;
	}

	private MemSwapUsageRecord createNextMemSwapRecord(final long i) {
		return new MemSwapUsageRecord(i, "localhost", 0, 0, 0, 0, 0, 0);
	}

	private CPUUtilizationRecord createNextCPURecord(final int cpuID, final long i) {
		return new CPUUtilizationRecord(i, "localhost", Integer.toString(cpuID), 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
	}

	public void terminate(final boolean error) {
		// No code necessary
	}

	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();

		configuration.setProperty(CONFIG_PROPERTY_NAME_NUMBER_CPUS, Integer.toString(this.numberCPUs));
		configuration.setProperty(CONFIG_PROPERTY_VALUE_NUMBER_DATA_SETS, Long.toString(this.numberDataSets));

		return configuration;
	}

}
