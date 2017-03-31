package kieker.analysisteetime.statistics;

import java.util.function.Consumer;

public class CountCalculator<T> implements Consumer<Statistic> {

	private final static Property COUNT_PROPERTY = PredefinedProperties.COUNT;
	
	@Override
	public void accept(final Statistic statistic) {
		final long oldCount = statistic.getProperty(COUNT_PROPERTY);
		final long newCount = oldCount + 1;
		statistic.setProperty(COUNT_PROPERTY, newCount);
	}

}
