package kieker.common.record.flow;


/**
 * @author Christian Wulf
 * 
 * @since 1.13
 */
public interface IThreadBasedRecord extends IFlowRecord {
	public long getThreadId() ;
	
	public int getOrderIndex() ;
	
}
