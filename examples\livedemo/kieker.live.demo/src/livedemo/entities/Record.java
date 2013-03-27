package livedemo.entities;

import kieker.common.record.controlflow.OperationExecutionRecord;

/**
 * @author Bjoern Weissenfels
 */
public class Record {
	
	private final OperationExecutionRecord record;
	
	private double responseTime; // in milliseconds, rounded to one decimal
	private String wholeRecord; // semicolon separated values
	private String shortSignature; // should be ...class.method(...)
	
	public Record(final OperationExecutionRecord record) {
		this.record = record;
		this.responseTime = this.computeResponseTime();
		this.wholeRecord = this.createWholeRecord();
		this.shortSignature = this.createShortSignature();
	}
	
	private double computeResponseTime(){
		double resp = this.record.getTout() - this.record.getTin();
		resp = resp/1000000; // conversion to milliseconds
		double rounded = Math.round(resp*10)/10.0; // rounded to one decimal
		return rounded;
	}
	
	private String createWholeRecord(){
		String result = this.record.toString();
		return result;
	}
	
	private String createShortSignature(){
		String[] array = this.record.getOperationSignature().split("\\(");
		array = array[0].split("\\.");
		int end = array.length;
		String result = "..." + array[end-2] + "." + array[end-1] + "(...)";
		return result;
	}
	
	public OperationExecutionRecord getOperationExecutionRecord(){
		return this.record;
	}

	public double getResponseTime() {
		return responseTime;
	}

	public String getWholeRecord() {
		return wholeRecord;
	}

	public String getShortSignature() {
		return shortSignature;
	}
	
}
