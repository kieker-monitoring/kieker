package kieker.common.record.flow;


/**
 * @author Jan Waller
 * 
 * @since 1.6
 */
public interface ITraceRecord extends IFlowRecord {
	public long getTraceId() ;
	
	public int getOrderIndex() ;
	
}
