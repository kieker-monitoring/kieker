package livedemo.entities;

import java.util.Date;

import kieker.common.record.system.CPUUtilizationRecord;
import kieker.common.record.system.MemSwapUsageRecord;

/**
 * @author Bjoern Weissenfels
 */
public class DataEntry {
	
	private long timestamp;
	private int count;
	
	private double sumRespTime;
	private double sumIdle;
	private double sumIrq;
	private double sumNice;
	private double sumSystem;
	private double sumTotalUtilzation;
	private double sumUser;
	private double sumWait;
	
	private long sumMemFree;
	private long sumMemUsed;
	private long sumSwapFree;
	private long sumSwapUsed;
	
	private String hourMinSec;
	private String minSec;
	private String sec;
	
	public DataEntry(long timestamp){
		this.timestamp = timestamp;
		this.count = 0;
		this.computeTimestamp();
		
		this.sumRespTime = 0;
		this.sumIdle = 0;
		this.sumIrq = 0;
		this.sumNice = 0;
		this.sumSystem = 0;
		this.sumTotalUtilzation = 0;
		this.sumUser = 0;
		this.sumWait = 0;
		
		this.sumMemFree = 0;
		this.sumMemUsed = 0;
		this.sumSwapFree = 0;
		this.sumSwapUsed = 0;
	}
	
	public void mergeDataEntries(DataEntry dataEntry){
		this.count += dataEntry.getCount();
		
		this.sumRespTime += dataEntry.sumRespTime;
		this.sumIdle += dataEntry.sumIdle;
		this.sumIrq += dataEntry.sumIrq;
		this.sumNice += dataEntry.sumNice;
		this.sumSystem += dataEntry.sumSystem;
		this.sumTotalUtilzation += dataEntry.sumTotalUtilzation;
		this.sumUser += dataEntry.sumUser;
		this.sumWait += dataEntry.sumWait;
		
		this.sumMemFree += dataEntry.sumMemFree;
		this.sumMemUsed += dataEntry.sumMemUsed;
		this.sumSwapFree += dataEntry.sumSwapFree;
		this.sumSwapUsed += dataEntry.sumSwapUsed;
	}
	
	public void addRecord(Record record){
		this.count++;
		this.sumRespTime += record.getResponseTime();
	}
	
	public void addCPUUtilizationRecord(CPUUtilizationRecord record){
		this.count++;
		this.sumIdle += record.getIdle();
		this.sumIrq += record.getIrq();
		this.sumNice += record.getNice();
		this.sumSystem += record.getSystem();
		this.sumTotalUtilzation += record.getTotalUtilization();
		this.sumUser += record.getUser();
		this.sumWait += record.getWait();
	}
	
	public void addMemSwapUsageRecord(MemSwapUsageRecord record){
		count++;
		this.sumMemFree += record.getMemFree();
		this.sumMemUsed += record.getMemUsed();
		this.sumSwapFree += record.getSwapFree();
		this.sumSwapUsed += record.getSwapUsed();
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
	
	public double getAverageIdle(){
		if(this.count == 0){
			return 0;
		}
		return this.sumIdle/this.count*100;
	}
	
	public double getAverageIrq(){
		if(this.count == 0){
			return 0;
		}
		return this.sumIrq/this.count*100;
	}
	
	public double getAverageNice(){
		if(this.count == 0){
			return 0;
		}
		return this.sumNice/this.count*100;
	}
	
	public double getAverageSystem(){
		if(this.count == 0){
			return 0;
		}
		return this.sumSystem/this.count*100;
	}
	
	public double getAverageTotalUtilization(){
		if(this.count == 0){
			return 0;
		}
		return this.sumTotalUtilzation/this.count*100;
	}
	
	public double getAverageUser(){
		if(this.count == 0){
			return 0;
		}
		return this.sumUser/this.count*100;
	}
	
	public double getAverageWait(){
		if(this.count == 0){
			return 0;
		}
		return this.sumWait/this.count*100;
	}
	
	public long getAverageMemFree(){
		if(this.count == 0){
			return 0;
		}
		return this.sumMemFree/(this.count*1048576);
	}
	
	public long getAverageMemUsed(){
		if(this.count == 0){
			return 0;
		}
		return this.sumMemUsed/(this.count*1048576);
	}
	
	public long getAverageSwapFree(){
		if(this.count == 0){
			return 0;
		}
		return this.sumSwapFree/(this.count*1048576);
	}
	
	public long getAverageSwapUsed(){
		if(this.count == 0){
			return 0;
		}
		return this.sumSwapUsed/(this.count*1048576);
	}
	
	private void computeTimestamp(){
		Date date = new Date(this.timestamp/1000000);
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
