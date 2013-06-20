package livedemo.filter;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import kieker.analysis.IProjectContext;
import kieker.analysis.display.XYPlot;
import kieker.analysis.display.annotation.Display;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.controlflow.OperationExecutionRecord;

/**
 * This is a filter which accepts {@link OperationExecutionRecord} instances and provides different views to visualize them. The incoming records are relayed without any
 * enrichment.
 * 
 * @author Bjoern Weissenfels
 * 
 * @since 1.8
 */
@Plugin(outputPorts =
		@OutputPort(
				name = MethodResponsetimeDisplayFilter.OUTPUT_PORT_NAME_RELAYED_RECORDS,
				eventTypes = { OperationExecutionRecord.class },
				description = "Provides each incoming object"),
		configuration = {
			@Property(name = MethodResponsetimeDisplayFilter.CONFIG_PROPERTY_NAME_NUMBER_OF_ENTRIES,
					defaultValue = MethodResponsetimeDisplayFilter.CONFIG_PROPERTY_VALUE_NUMBER_OF_ENTRIES,
					description = "Sets the number of max plot entries per cpu"),
			@Property(name = MethodResponsetimeDisplayFilter.CONFIG_PROPERTY_NAME_NUMBER_OF_ENTRIES,
					defaultValue = MethodResponsetimeDisplayFilter.CONFIG_PROPERTY_VALUE_NUMBER_OF_ENTRIES,
					description = "Sets the timeunit of the responsetime")})
public class MethodResponsetimeDisplayFilter extends AbstractFilterPlugin {
	
	public static final String INPUT_PORT_NAME_RECORDS = "inputRecordEvents";
	public static final String INPUT_PORT_NAME_TIMESTAMPS = "inputTimeEvents";

	public static final String OUTPUT_PORT_NAME_RELAYED_RECORDS = "relayedEvents";

	public static final String CONFIG_PROPERTY_NAME_NUMBER_OF_ENTRIES = "numberOfEntries";
	public static final String CONFIG_PROPERTY_VALUE_NUMBER_OF_ENTRIES = "100";
	
	public static final String CONFIG_PROPERTY_NAME_RESPONSETIME_TIMEUNIT = "responsetimeunit";
	public static final String CONFIG_PROPERTY_VALUE_RESPONSETIME_TIMEUNIT = "NANOSECONDS";
	
	private static final Log LOG = LogFactory.getLog(MethodResponsetimeDisplayFilter.class);
	
	private final XYPlot methodResponsetimeXYplot;
	private final XYPlot methodCallsXYplot;
	private final int numberOfEntries;
	private final TimeUnit timeunit;
	private final TimeUnit responsetimeunit;
	private final Map<String, Pair<Long,Integer>> signatureResponsetimeMap;
	private final Map<String, Pair<Long,Integer>> newSignatures;

	public MethodResponsetimeDisplayFilter(Configuration configuration,
			IProjectContext projectContext) {
		super(configuration, projectContext);
		this.numberOfEntries = configuration.getIntProperty(CONFIG_PROPERTY_NAME_NUMBER_OF_ENTRIES);
		final String responsetimeTimeunitProperty = configuration.getStringProperty(MethodResponsetimeDisplayFilter.CONFIG_PROPERTY_NAME_RESPONSETIME_TIMEUNIT);
		final String recordTimeunitProperty = projectContext.getProperty(IProjectContext.CONFIG_PROPERTY_NAME_RECORDS_TIME_UNIT);
		TimeUnit responsetimeTimeunit;
		TimeUnit recordTimeunit;
		try {
			responsetimeTimeunit = TimeUnit.valueOf(responsetimeTimeunitProperty);
			recordTimeunit = TimeUnit.valueOf(recordTimeunitProperty);
		} catch (final IllegalArgumentException ex) { // already caught in AnalysisController, should never happen
			LOG.warn(recordTimeunitProperty + " is no valid TimeUnit! Using NANOSECONDS instead.");
			responsetimeTimeunit = TimeUnit.NANOSECONDS;
			recordTimeunit = TimeUnit.NANOSECONDS;
		}
		this.responsetimeunit = responsetimeTimeunit;
		this.timeunit = recordTimeunit;
		this.methodResponsetimeXYplot = new XYPlot(this.numberOfEntries);
		this.methodCallsXYplot = new XYPlot(this.numberOfEntries);
		this.signatureResponsetimeMap = new ConcurrentHashMap<String, MethodResponsetimeDisplayFilter.Pair<Long,Integer>>();
		this.newSignatures = new ConcurrentHashMap<String, MethodResponsetimeDisplayFilter.Pair<Long,Integer>>();
	}
	
	@InputPort(name = MethodResponsetimeDisplayFilter.INPUT_PORT_NAME_RECORDS, eventTypes = { OperationExecutionRecord.class })
	public synchronized void inputRecords(final OperationExecutionRecord record){
		if(this.methodResponsetimeXYplot.getKeys().contains(record.getOperationSignature())){
			if(this.signatureResponsetimeMap.containsKey(record.getOperationSignature())){
				Pair<Long, Integer> pair = this.signatureResponsetimeMap.get(record.getOperationSignature());
				pair.setFirst(pair.getFirst() + (record.getTout() - record.getTin()));
				pair.setLast(pair.getLast() + 1);
			}else{
				Pair<Long, Integer> pair = new Pair<Long, Integer>(record.getTout() - record.getTin(), 1);
				this.signatureResponsetimeMap.put(record.getOperationSignature(), pair);
			}
		}else{
			if(this.newSignatures.containsKey(record.getOperationSignature())){
				Pair<Long, Integer> pair = this.newSignatures.get(record.getOperationSignature());
				pair.setFirst(pair.getFirst() + (record.getTout() - record.getTin()));
				pair.setLast(pair.getLast() + 1);
			}else{
				Pair<Long, Integer> pair = new Pair<Long, Integer>(record.getTout() - record.getTin(), 1);
				this.newSignatures.put(record.getOperationSignature(), pair);
			}
		}
		
		super.deliver(OUTPUT_PORT_NAME_RELAYED_RECORDS, record);
	}
	
	@InputPort(name = MethodResponsetimeDisplayFilter.INPUT_PORT_NAME_TIMESTAMPS, eventTypes = { Long.class })
	public synchronized void inputTimeEvents(final long timestamp){
		// Calculate the minutes and seconds of the logging timestamp of the record
		final Date date = new Date(TimeUnit.MILLISECONDS.convert(timestamp, this.timeunit));
		final String minutesAndSeconds = date.toString().substring(14, 19);
				
		for(String key : this.methodResponsetimeXYplot.getKeys()){
			if(this.signatureResponsetimeMap.containsKey(key)){
				Pair<Long, Integer> p = this.signatureResponsetimeMap.get(key);
				long averageResponsetime = p.getFirst() / p.getLast(); 
				this.methodResponsetimeXYplot.setEntry(key, minutesAndSeconds, this.convertFromNanosToMillis(averageResponsetime));
				this.methodCallsXYplot.setEntry(key, minutesAndSeconds, p.getLast());
			}else{
				this.methodResponsetimeXYplot.setEntry(key, minutesAndSeconds, 0);
				this.methodCallsXYplot.setEntry(key, minutesAndSeconds, 0);
			}
		}
		for(String newKey : this.newSignatures.keySet()){
			Pair<Long, Integer> p = this.newSignatures.get(newKey);
			long averageResponsetime = p.getFirst() / p.getLast(); 
			this.methodResponsetimeXYplot.setEntry(newKey, minutesAndSeconds, this.convertFromNanosToMillis(averageResponsetime));
			this.methodCallsXYplot.setEntry(newKey, minutesAndSeconds, p.getLast());
		}
		
		this.signatureResponsetimeMap.clear();
		this.newSignatures.clear();
	}
	
	// another possibility to convert the response time, but under circumstances very inaccurate (900.000 ns = 0 ms)
	@SuppressWarnings("unused")
	private long convertResponsetime(long responsetime){
		return this.responsetimeunit.convert(responsetime, this.timeunit);
	}
	
	private double convertFromNanosToMillis(long duration){
		duration = TimeUnit.NANOSECONDS.convert(duration, this.timeunit);
		return Math.round(duration/100000.0)/10.0;
	}
		
	@Display(name = "XYPlot Of Average Method Responsetime Per Intervall")
	public XYPlot getMethodResponsetimeXYPlot() {
		return this.methodResponsetimeXYplot;
	}
	
	@Display(name = "XYPlot Of Method Calls Per Intervall")
	public XYPlot getMethodCallsXYPlot() {
		return this.methodCallsXYplot;
	}

	@Override
	public Configuration getCurrentConfiguration() {
		Configuration configuration = new Configuration();
		configuration.setProperty(CONFIG_PROPERTY_NAME_NUMBER_OF_ENTRIES, String.valueOf(this.numberOfEntries));
		return configuration;
	}
	
	public class Pair<T1,T2>{
		private T1 first;
		private T2 last;
		
		public Pair(T1 first, T2 last){
			this.first = first;
			this.last = last;
		}
		
		public T1 getFirst(){
			return this.first;
		}
		
		public void setFirst(T1 first){
			this.first = first;
		}
		
		public T2 getLast(){
			return this.last;
		}
		
		public void setLast(T2 last){
			this.last = last;
		}
	}

}
