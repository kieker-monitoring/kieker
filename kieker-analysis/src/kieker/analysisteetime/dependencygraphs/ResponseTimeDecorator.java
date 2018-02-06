package kieker.analysisteetime.dependencygraphs;

import java.time.temporal.ChronoUnit;

import kieker.analysisteetime.statistics.Properties;
import kieker.analysisteetime.statistics.Property;
import kieker.analysisteetime.statistics.StatisticsModel;
import kieker.analysisteetime.statistics.Units;
import kieker.analysisteetime.util.graph.Vertex;
import kieker.analysisteetime.util.time.ChronoUnitToSymbolMapper;

/**
 * This class adds corresponding statistics to vertices.
 *
 * @author Sören Henning
 */
public class ResponseTimeDecorator {

	private final StatisticsModel statisticsModel;
	private final String timeUnit;

	public ResponseTimeDecorator(final StatisticsModel statisticsModel, final ChronoUnit chronoUnit) {
		this.statisticsModel = statisticsModel;
		this.timeUnit = ChronoUnitToSymbolMapper.create().apply(chronoUnit);
	}

	public void decorate(final Vertex vertex, final Object object) {
		vertex.setPropertyIfAbsent(PropertyKeys.MIN_REPSONSE_TIME, this.getStatisticValue(object, Properties.MIN));
		vertex.setPropertyIfAbsent(PropertyKeys.MAX_REPSONSE_TIME, this.getStatisticValue(object, Properties.MAX));
		vertex.setPropertyIfAbsent(PropertyKeys.TOTAL_RESPONSE_TIME, this.getStatisticValue(object, Properties.TOTAL));
		vertex.setPropertyIfAbsent(PropertyKeys.MEAN_REPSONSE_TIME, this.getStatisticValue(object, Properties.AVERAGE));
		vertex.setPropertyIfAbsent(PropertyKeys.MEDIAN_REPSONSE_TIME, this.getStatisticValue(object, Properties.MEDIAN));
		vertex.setPropertyIfAbsent(PropertyKeys.TIME_UNIT, this.timeUnit);
	}

	private long getStatisticValue(final Object object, final Property property) {
		if (this.statisticsModel.has(object)) {
			final Long value = this.statisticsModel.get(object).getStatistic(Units.RESPONSE_TIME).getProperty(property);
			if (value != null) {
				return value;
			}
		}
		return 0;
	}
}
