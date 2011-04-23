package kieker.tools.traceAnalysis.systemModel;

import java.util.Iterator;
import java.util.Vector;

/**
 * @author Andre van Hoorn
 */
public class MessageTrace extends Trace {

    private final Vector<Message> set;

    public MessageTrace(final long traceId, final Vector<Message> seq) {
        super(traceId);
        this.set = seq;
    }

    public final Vector<Message> getSequenceAsVector() {
        return this.set;
    }

    @Override
    public String toString() {
        StringBuilder strBuild =
                new StringBuilder("Trace " + this.getTraceId() + ":\n");
        Iterator<Message> it = set.iterator();
        while (it.hasNext()) {
            Message m = it.next();
            strBuild.append("<");
            strBuild.append(m.toString());
            strBuild.append(">\n");
        }
        return strBuild.toString();
    }

    @Override
    public boolean equals(Object obj) {
       if (!(obj instanceof MessageTrace)){
            return false;
        }
        if (this == obj) {
            return true;
        }
        MessageTrace other = (MessageTrace)obj;

        return this.set.equals(other.set);
    }
}
