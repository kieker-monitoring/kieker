package kieker.analysis.plugin.filter.forward;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.RepositoryOutputPort;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;

@Plugin(
		description = "A filter counting the elements flowing through this filter",
		outputPorts = {
			@OutputPort(name = CountingFilterWithRepository.OUTPUT_PORT_NAME_RELAYED_EVENTS, eventTypes = { Object.class },
					description = "Provides each incoming object"),
			@OutputPort(name = CountingFilterWithRepository.OUTPUT_PORT_NAME_COUNT, eventTypes = { Long.class },
					description = "Provides the current object count")
		}, repositoryOutputPorts =
		@RepositoryOutputPort(name = CountingFilterWithRepository.REPOSITORY_OUTPUT_PORT_COUNTER, eventTypes = Long.class))
public final class CountingFilterWithRepository extends AbstractFilterPlugin {

	public static final String INPUT_PORT_NAME_EVENTS = "inputEvents";

	public static final String OUTPUT_PORT_NAME_RELAYED_EVENTS = "relayedEvents";
	public static final String OUTPUT_PORT_NAME_COUNT = "currentEventCount";

	public static final String REPOSITORY_OUTPUT_PORT_COUNTER = "counterRepository";

	public CountingFilterWithRepository(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);
	}

	@Override
	public final Configuration getCurrentConfiguration() {
		return new Configuration();
	}

	public final long getMessageCount() {
		// return this.counter.get();
		return 0;
	}

	@InputPort(name = INPUT_PORT_NAME_EVENTS, eventTypes = { Object.class }, description = "Receives incoming objects to be counted and forwarded")
	public final void inputEvent(final Object event) {
		final Long currValue = (Long) super.deliverWithReturnTypeToRepository(REPOSITORY_OUTPUT_PORT_COUNTER, new Long(1));
		// final Long count = CountingFilterWithRepository.this.counter.incrementAndGet();

		super.deliver(OUTPUT_PORT_NAME_RELAYED_EVENTS, event);
		super.deliver(OUTPUT_PORT_NAME_COUNT, currValue.longValue());
	}
}
