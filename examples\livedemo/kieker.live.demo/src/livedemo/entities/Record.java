package livedemo.entities;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.common.util.registry.IRegistry;

/**
 * @author Bjoern Weissenfels
 */
public class Record implements IMonitoringRecord{
	
	private static final long serialVersionUID = 4652653198700953697L;

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

	@Override
	public int compareTo(IMonitoringRecord otherRecord) {
		return this.record.compareTo(otherRecord);
	}

	@Override
	public long getLoggingTimestamp() {
		return this.record.getLoggingTimestamp();
	}

	@Override
	public Class<?>[] getValueTypes() {
		return this.record.getValueTypes();
	}

	@Override
	public void initFromArray(Object[] arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setLoggingTimestamp(long timestamp) {
		this.record.setLoggingTimestamp(timestamp);
	}

	@Override
	public Object[] toArray() {
		return this.record.toArray();
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void initFromBytes(ByteBuffer arg0, IRegistry<String> arg1)
			throws BufferUnderflowException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void writeBytes(ByteBuffer arg0, IRegistry<String> arg1)
			throws BufferOverflowException {
		// TODO Auto-generated method stub
		
	}
	
}
