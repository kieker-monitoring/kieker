package kieker.tools.traceAnalysis.systemModel;

/**
 *
 * @author Andre van Hoorn
 */
public class SynchronousReplyMessage extends Message {

    public SynchronousReplyMessage() {
        super();
    }

    public SynchronousReplyMessage(final long timestamp,
            final Execution sendingExecution,
            final Execution receivingExecution){
        super(timestamp, sendingExecution, receivingExecution);
    }

    @Override
    public boolean equals(Object obj) {
       if (!(obj instanceof SynchronousReplyMessage)){
            return false;
        }
        if (this == obj) {
            return true;
        }
        SynchronousReplyMessage other = (SynchronousReplyMessage)obj;

        return this.getTimestamp() == other.getTimestamp()
                && this.getSendingExecution().equals(other.getSendingExecution())
                && this.getReceivingExecution().equals(other.getReceivingExecution());
    }
}
