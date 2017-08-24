package kieker.analysis.plugin.reader.newio;

public interface IRawDataReader {
	
	public Outcome onInitialization();
	
	public Outcome read();
	
	public Outcome onTermination();

}
