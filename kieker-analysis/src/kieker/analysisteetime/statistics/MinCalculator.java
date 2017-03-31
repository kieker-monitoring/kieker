package kieker.analysisteetime.statistics;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class MinCalculator<T> implements BiConsumer<Statistic, T> {

	private final static Property MIN_PROPERTY = PredefinedProperties.MIN;

	private Function<T, Long> valueAccessor;
	
	@Override
	public void accept(Statistic statistic, T input) {
		final long value = this.valueAccessor.apply(input);
		final long oldMin = statistic.getProperty(MIN_PROPERTY);
		if (value < oldMin) {
			statistic.setProperty(MIN_PROPERTY, value);			
		}
	}

}
