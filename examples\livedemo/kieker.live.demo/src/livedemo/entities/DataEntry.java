package livedemo.entities;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import kieker.common.record.system.CPUUtilizationRecord;
import kieker.common.record.system.MemSwapUsageRecord;

/**
 * @author Bjoern Weissenfels
 */
public class DataEntry {
	
	private long timestamp; // in nanoseconds
	private int count;
	private double sumRespTime;
	private String hourMinSec;
	private String minSec;
	private String sec;
	private TimeUnit timeunit;
	
	public DataEntry(long timestamp, TimeUnit timeunit){
		this.timestamp = timestamp;
		this.timeunit = timeunit;
		this.count = 0;
		this.computeTimestamp();
		this.sumRespTime = 0;
	}
	
	public void mergeDataEntries(DataEntry dataEntry){
		this.count += dataEntry.getCount();
		this.sumRespTime += dataEntry.sumRespTime;
	}
	
	public void addResponsetime(double responsetime){
		
	}
	
	public void addRecord(Record record){
		this.count++;
		this.sumRespTime += record.getResponseTime();
	}
	
	public int getCount(){
		return this.count;
	}
	
	public double getAverageResponsetime(){
		if(this.count == 0){
			return 0;
		}
		return this.sumRespTime/this.count;
	}
	
	private void computeTimestamp(){
		final Date date = new Date(TimeUnit.MILLISECONDS.convert(this.timestamp, this.timeunit));
		this.hourMinSec = date.toString().substring(11, 19);
		this.minSec = date.toString().substring(14, 19);
		this.sec = date.toString().substring(17, 19);
	}
	
	public long getTimestamp(){
		return this.timestamp;
	}
	
	public String getHourMinSec(){
		return this.hourMinSec;
	}
	
	public String getMinSec(){
		return this.minSec;
	}
	
	public String getSec(){
		return this.sec;
	}

}
