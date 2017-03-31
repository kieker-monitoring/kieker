package kieker.analysisteetime.statistics;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class TotalCalculator<T> implements BiConsumer<Statistic, T> {

	private final static Property TOTAL_PROPERTY = PredefinedProperties.TOTAL;

	private Function<T, Long> valueAccessor;
	
	@Override
	public void accept(Statistic statistic, T input) {
		final long value = this.valueAccessor.apply(input);
		final long oldCount = statistic.getProperty(TOTAL_PROPERTY);
		final long newCount = oldCount + value;
		statistic.setProperty(TOTAL_PROPERTY, newCount);
	}

}
