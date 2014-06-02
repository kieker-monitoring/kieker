package kieker.panalysis.stage;

import java.util.Collection;

import kieker.panalysis.predicate.IsExactTypePredicate;
import kieker.panalysis.stage.basic.PredicateFilter;

/**
 * @author Jan Waller, Nils Christian Ehmke, Christian Wulf
 * 
 * @since 1.10
 */
public class ExactTypeFilter<T> extends PredicateFilter<T> {

	/**
	 * @param acceptedClasses
	 * @since 1.10
	 */
	public ExactTypeFilter(final Collection<Class<?>> acceptedClasses) {
		super(new IsExactTypePredicate<T>(acceptedClasses));
	}
}
