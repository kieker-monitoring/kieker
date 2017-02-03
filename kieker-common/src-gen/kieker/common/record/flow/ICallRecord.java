package kieker.common.record.flow;


/**
 * @author Jan Waller
 * 
 * @since 1.6
 */
public interface ICallRecord extends IOperationRecord {
	public String getCallerOperationSignature() ;
	
	public String getCallerClassSignature() ;
	
	public String getCalleeOperationSignature() ;
	
	public String getCalleeClassSignature() ;
	
}
