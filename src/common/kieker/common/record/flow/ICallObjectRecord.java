package kieker.common.record.flow;

public interface ICallObjectRecord extends IObjectRecord {

	public abstract int getCallerObjectId();

	public abstract int getCalleeObjectId();
}
