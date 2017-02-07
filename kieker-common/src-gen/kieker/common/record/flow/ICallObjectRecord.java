package kieker.common.record.flow;


/**
 * @author Jan Waller
 * 
 * @since 1.6
 */
public interface ICallObjectRecord extends ICallRecord, IObjectRecord {
	public int getCallerObjectId() ;
	
	public int getCalleeObjectId() ;
	
}
