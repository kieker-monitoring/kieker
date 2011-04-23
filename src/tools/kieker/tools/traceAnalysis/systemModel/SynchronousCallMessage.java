package kieker.tools.traceAnalysis.systemModel;

/**
 *
 * @author Andre van Hoorn
 */
public class SynchronousCallMessage extends Message {

    public SynchronousCallMessage() {
        super();
    }

    public SynchronousCallMessage(final long timestamp,
            final Execution sendingExecution,
            final Execution receivingExecution){
        super(timestamp, sendingExecution, receivingExecution);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SynchronousCallMessage)){
            return false;
        }
        if (this == obj) {
            return true;
        }
        SynchronousCallMessage other = (SynchronousCallMessage)obj;

        return this.getTimestamp() == other.getTimestamp()
                && this.getSendingExecution().equals(other.getSendingExecution())
                && this.getReceivingExecution().equals(other.getReceivingExecution());
    }
}
