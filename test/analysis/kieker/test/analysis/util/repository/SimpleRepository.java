package kieker.test.analysis.util.repository;

import kieker.analysis.repository.AbstractRepository;
import kieker.analysis.repository.annotation.Repository;
import kieker.common.configuration.Configuration;

/**
 * @author Nils Christian Ehmke, Jan Waller
 */
@Repository(name = SimpleRepository.REPOSITORY_NAME, description = SimpleRepository.REPOSITORY_DESCRIPTION)
public class SimpleRepository extends AbstractRepository { // NOPMD (SubClassOfTest)

	public static final String REPOSITORY_NAME = "repoName-hNcuzIKc8e";

	public static final String REPOSITORY_DESCRIPTION = "repoDescription-DEYmVN6sEp";

	public SimpleRepository(final Configuration configuration) {
		super(configuration);
	}

	@Override
	protected Configuration getDefaultConfiguration() {
		return new Configuration();
	}

	public Configuration getCurrentConfiguration() {
		return new Configuration();
	}
}
