package livedemo.entities;

import java.util.Date;

public class DataEntry {
	
	long timestamp;
	int count;
	double sum;
	
	public DataEntry(long timestamp){
		this.timestamp = timestamp;
		this.count = 0;
		this.sum = 0;
	}
	
	public void addRecord(Record record){
		this.count++;
		this.sum += record.getResponseTime();
	}
	
	public int getCount(){
		return this.count;
	}
	
	public double getAverageResponsetime(){
		if(this.count == 0){
			return 0;
		}
		return this.sum/this.count;
	}
	
	public String getTimestamp(){
		Date date = new Date(this.timestamp/1000000);
		return date.toString().substring(11, 19);
	}

}
