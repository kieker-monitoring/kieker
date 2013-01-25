package livedemo.managedbeans;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import kieker.analysis.AnalysisController;
import kieker.analysis.plugin.filter.forward.CountingThroughputFilter;
import kieker.analysis.plugin.filter.forward.ListCollectionFilter;
import kieker.analysis.plugin.filter.forward.TeeFilter;
import kieker.analysis.plugin.reader.jmx.JMXReader;
import kieker.common.configuration.Configuration;
import kieker.common.record.controlflow.OperationExecutionRecord;

@ManagedBean(name="startingBean", eager=true)
@ApplicationScoped
public class StartingBean {
	
	final AnalysisController analysisInstance;
	final ListCollectionFilter<OperationExecutionRecord> listCollectionFilter;
	final CountingThroughputFilter ctFilter;
	
	public StartingBean(){
		this.analysisInstance = new AnalysisController();
		this.listCollectionFilter = new ListCollectionFilter<OperationExecutionRecord>(new Configuration());
		this.ctFilter = new CountingThroughputFilter(new Configuration());
		StarterThread s = new StarterThread();
		new Thread(s).start();
	}
	
	public CountingThroughputFilter getCountingFilter(){
		return this.ctFilter;
	}
	
	public ListCollectionFilter<OperationExecutionRecord> getCollectionFilter(){
		return this.listCollectionFilter;
	}

	private class StarterThread implements Runnable{

		@Override
		public void run() {
			try {
				final Configuration jmxReaderConfig = new Configuration();
				jmxReaderConfig.setProperty(JMXReader.CONFIG_PROPERTY_NAME_SILENT, "true");
				final JMXReader reader = new JMXReader(jmxReaderConfig);
				analysisInstance.registerReader(reader);

				
				final Configuration teeFilterConfig = new Configuration();
				teeFilterConfig.setProperty(TeeFilter.CONFIG_PROPERTY_NAME_STREAM,
				TeeFilter.CONFIG_PROPERTY_VALUE_STREAM_STDOUT);
				final TeeFilter teeFilter = new TeeFilter(teeFilterConfig);

				analysisInstance.registerFilter(teeFilter);
				analysisInstance.registerFilter(listCollectionFilter);
				analysisInstance.registerFilter(ctFilter);

				analysisInstance.connect(reader, JMXReader.OUTPUT_PORT_NAME_RECORDS,
						listCollectionFilter, ListCollectionFilter.INPUT_PORT_NAME);
				analysisInstance.connect(listCollectionFilter, ListCollectionFilter.OUTPUT_PORT_NAME,
						teeFilter, TeeFilter.INPUT_PORT_NAME_EVENTS);
				analysisInstance.connect(teeFilter, TeeFilter.OUTPUT_PORT_NAME_RELAYED_EVENTS, 
						ctFilter, CountingThroughputFilter.INPUT_PORT_NAME_RECORDS);
				
				/* Start the analysis */
				System.out.println("analysis startet");
				analysisInstance.run();
			} catch (final Exception e) {
				System.out.println("An error occurred: " + e.getMessage());
				System.exit(1);
			}
			
		}
		
	}

}
