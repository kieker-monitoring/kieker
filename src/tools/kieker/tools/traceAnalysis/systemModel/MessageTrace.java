package kieker.tools.traceAnalysis.systemModel;

import java.util.Iterator;
import java.util.Vector;

/* 
 * ==================LICENCE=========================
 * Copyright 2006-2010 Kieker Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ==================================================
 */

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
