package kieker.panalysis.stage;

import java.util.Arrays;

import kieker.panalysis.predicate.IsInstanceOfPredicate;
import kieker.panalysis.stage.basic.PredicateFilter;

/**
 * @author Jan Waller, Nils Christian Ehmke, Christian Wulf
 * 
 * @since 1.10
 */
public class InstanceOfFilter<T> extends PredicateFilter<T> {

	/**
	 * @param acceptedClasses
	 * @since 1.10
	 */
	public InstanceOfFilter(final Class<?>... acceptedClasses) {
		super(new IsInstanceOfPredicate<T>(Arrays.asList(acceptedClasses)));
	}
}
