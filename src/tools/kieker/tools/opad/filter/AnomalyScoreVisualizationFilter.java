package kieker.tools.opad.filter;

import kieker.analysis.IProjectContext;
import kieker.analysis.display.XYPlot;
import kieker.analysis.display.annotation.Display;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.analysis.plugin.filter.sink.CPUUtilizationDisplayFilter;
import kieker.common.configuration.Configuration;
import kieker.tools.opad.record.StorableDetectionResult;

/**
 * @author Nils Christian Ehmke
 * @since 1.9
 */
@Plugin(configuration = {
	@Property(
			name = CPUUtilizationDisplayFilter.CONFIG_PROPERTY_NAME_NUMBER_OF_ENTRIES,
			defaultValue = CPUUtilizationDisplayFilter.CONFIG_PROPERTY_VALUE_NUMBER_OF_ENTRIES) })
public class AnomalyScoreVisualizationFilter extends AbstractFilterPlugin {

	public static final String INPUT_PORT_NAME_NORMAL_EVENTS = "inputEventsNormal";
	public static final String INPUT_PORT_NAME_ABNORMAL_EVENTS = "inputEventsAbnormal";

	public static final String CONFIG_PROPERTY_NAME_NUMBER_OF_ENTRIES = "numberOfEntries";
	public static final String CONFIG_PROPERTY_VALUE_NUMBER_OF_ENTRIES = "100";

	private final XYPlot xyplot;

	private final int numberOfEntries;

	public AnomalyScoreVisualizationFilter(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);

		this.numberOfEntries = configuration.getIntProperty(CONFIG_PROPERTY_NAME_NUMBER_OF_ENTRIES);
		this.xyplot = new XYPlot(this.numberOfEntries);
	}

	@InputPort(name = AnomalyScoreVisualizationFilter.INPUT_PORT_NAME_NORMAL_EVENTS, eventTypes = { StorableDetectionResult.class })
	public void inputNormal(final StorableDetectionResult record) {
		this.updateDisplays(record, false);
	}

	@InputPort(name = AnomalyScoreVisualizationFilter.INPUT_PORT_NAME_ABNORMAL_EVENTS, eventTypes = { StorableDetectionResult.class })
	public void inputAbnormal(final StorableDetectionResult record) {
		this.updateDisplays(record, true);
	}

	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();

		configuration.setProperty(CONFIG_PROPERTY_NAME_NUMBER_OF_ENTRIES, String.valueOf(this.numberOfEntries));

		return configuration;
	}

	private void updateDisplays(final StorableDetectionResult record, final boolean abnormal) {
		if (abnormal) {
			this.xyplot.setEntry(record.getApplication() + " - Abnormal", record.getTimestamp(), record.getScore());
		} else {
			this.xyplot.setEntry(record.getApplication() + " - Normal", record.getTimestamp(), record.getScore());
		}

		this.xyplot.setEntry(record.getApplication() + " - Forecast", record.getTimestamp(), record.getForecast());
	}

	@Display(name = "XYPlot Anomaly Display")
	public XYPlot getXYPlot() {
		return this.xyplot;
	}
}
