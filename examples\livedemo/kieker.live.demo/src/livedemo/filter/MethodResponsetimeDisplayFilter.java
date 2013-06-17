package livedemo.filter;

import java.util.concurrent.TimeUnit;

import kieker.analysis.IProjectContext;
import kieker.analysis.display.XYPlot;
import kieker.analysis.display.annotation.Display;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.controlflow.OperationExecutionRecord;

/**
 * This is a filter which accepts {@link OperationExecutionRecord} instances and provides different views to visualize them. The incoming records are relayed without any
 * enrichment.
 * 
 * @author Bjoern Weissenfels
 * 
 * @since 1.8
 */
@Plugin(outputPorts =
		@OutputPort(
				name = MethodResponsetimeDisplayFilter.OUTPUT_PORT_NAME_RELAYED_EVENTS,
				eventTypes = { OperationExecutionRecord.class },
				description = "Provides each incoming object"),
		configuration = {
			@Property(
					name = MethodResponsetimeDisplayFilter.CONFIG_PROPERTY_NAME_NUMBER_OF_ENTRIES,
					defaultValue = MethodResponsetimeDisplayFilter.CONFIG_PROPERTY_VALUE_NUMBER_OF_ENTRIES,
					description = "Sets the number of max plot entries per cpu")})
public class MethodResponsetimeDisplayFilter extends AbstractFilterPlugin {
	
	private static final Log LOG = LogFactory.getLog(MethodResponsetimeDisplayFilter.class);
	
	public static final String INPUT_PORT_NAME_EVENTS = "inputEvents";

	public static final String OUTPUT_PORT_NAME_RELAYED_EVENTS = "relayedEvents";

	public static final String CONFIG_PROPERTY_NAME_NUMBER_OF_ENTRIES = "numberOfEntries";
	public static final String CONFIG_PROPERTY_VALUE_NUMBER_OF_ENTRIES = "100";
	
	private final XYPlot xyplot;
	private final int numberOfEntries;
	private final TimeUnit timeunit;

	public MethodResponsetimeDisplayFilter(Configuration configuration,
			IProjectContext projectContext) {
		super(configuration, projectContext);
		this.numberOfEntries = configuration.getIntProperty(CONFIG_PROPERTY_NAME_NUMBER_OF_ENTRIES);
		final String recordTimeunitProperty = projectContext.getProperty(IProjectContext.CONFIG_PROPERTY_NAME_RECORDS_TIME_UNIT);
		TimeUnit recordTimeunit;
		try {
			recordTimeunit = TimeUnit.valueOf(recordTimeunitProperty);
		} catch (final IllegalArgumentException ex) { // already caught in AnalysisController, should never happen
			LOG.warn(recordTimeunitProperty + " is no valid TimeUnit! Using NANOSECONDS instead.");
			recordTimeunit = TimeUnit.NANOSECONDS;
		}
		this.timeunit = recordTimeunit;
		this.xyplot = new XYPlot(this.numberOfEntries);
	}
	
	@InputPort(name = MethodResponsetimeDisplayFilter.INPUT_PORT_NAME_EVENTS, eventTypes = { OperationExecutionRecord.class })
	public void input(final OperationExecutionRecord record){
				
		this.xyplot.setEntry(record.getOperationSignature(), record.getLoggingTimestamp(), record.getTout() - record.getTin());
		
		super.deliver(OUTPUT_PORT_NAME_RELAYED_EVENTS, record);
	}
	
	@Display(name = "XYPlot Method Responsetime Display")
	public XYPlot getXYPlot() {
		return this.xyplot;
	}

	@Override
	public Configuration getCurrentConfiguration() {
		Configuration configuration = new Configuration();
		configuration.setProperty(CONFIG_PROPERTY_NAME_NUMBER_OF_ENTRIES, String.valueOf(this.numberOfEntries));
		return configuration;
	}

}
