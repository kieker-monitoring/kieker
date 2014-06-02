package kieker.panalysis.stage;

import java.util.Collection;

import kieker.panalysis.predicate.IsSuperTypePredicate;
import kieker.panalysis.stage.basic.PredicateFilter;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class SuperTypeFilter<T> extends PredicateFilter<T> {

	/**
	 * @param acceptedClasses
	 * @since 1.10
	 */
	public SuperTypeFilter(final Collection<Class<?>> acceptedClasses) {
		super(new IsSuperTypePredicate<T>(acceptedClasses));
	}

}
