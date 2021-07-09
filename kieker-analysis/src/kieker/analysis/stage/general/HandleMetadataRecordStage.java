/**
 *
 */
package kieker.analysis.stage.general;

import java.util.Map;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.misc.KiekerMetadataRecord;

import teetime.stage.basic.AbstractFilter;

/**
 * Receive all events and filter out the metadata records and store its information in a metadata model.
 * Relay all other events.
 *
 * @author Reiner Jung
 * @since 1.15
 */
public class HandleMetadataRecordStage extends AbstractFilter<IMonitoringRecord> {

	private final Map<String, KiekerMetadataRecord> metadataMap;

	public HandleMetadataRecordStage(final Map<String, KiekerMetadataRecord> metadataMap) {
		this.metadataMap = metadataMap;
	}

	@Override
	protected void execute(final IMonitoringRecord element) throws Exception {
		if (element instanceof KiekerMetadataRecord) {
			final KiekerMetadataRecord metadata = (KiekerMetadataRecord) element;
			this.metadataMap.put(metadata.getHostname(), metadata);
		} else {
			this.outputPort.send(element);
		}
	}

}
