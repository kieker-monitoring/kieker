package kieker.analysis.plugin.reader.newio;

public abstract class AbstractRawDataReader implements IRawDataReader {

	protected final IRawDataProcessor dataProcessor;
	
	protected AbstractRawDataReader(IRawDataProcessor dataProcessor) {
		this.dataProcessor = dataProcessor;
	}
		
}
