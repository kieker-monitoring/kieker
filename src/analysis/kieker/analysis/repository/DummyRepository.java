package kieker.analysis.repository;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.RepositoryInputPort;
import kieker.analysis.repository.annotation.Repository;
import kieker.common.configuration.Configuration;

@Repository
public class DummyRepository extends AbstractRepository {

	public static final String REPOSITORY_INPUT_PORT_COUNTER = "counterPort";

	private long counter = 0;

	public DummyRepository(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);
	}

	@Override
	public Configuration getCurrentConfiguration() {
		return new Configuration();
	}

	@RepositoryInputPort(returnType = Long.class, eventTypes = Long.class, name = DummyRepository.REPOSITORY_INPUT_PORT_COUNTER)
	public Long getAndIncrementCounter(final Long i) {
		synchronized (this) {
			final long preValue = this.counter;
			this.counter += i.longValue();
			return preValue;
		}
	}

}
