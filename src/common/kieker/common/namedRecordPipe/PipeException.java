package kieker.common.namedRecordPipe;

/**
 *
 * @author Andre van Hoorn
 */
public class PipeException extends Exception{
    /**
	 * 
	 */
	private static final long serialVersionUID = 56l;

	public PipeException(final String msg){
        super(msg);
    }

    public PipeException(final String msg, final Throwable thrw){
        super(msg, thrw);
    }
}
