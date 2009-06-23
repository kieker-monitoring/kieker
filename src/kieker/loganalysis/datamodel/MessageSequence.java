package kieker.loganalysis.datamodel;

import java.util.Iterator;
import java.util.Vector;

/**
 * kieker.loganalysis.datamodel.MessageSequence
 * 
 * ==================LICENCE=========================
 * Copyright 2006-2009 Kieker Project
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
 *
 * @author Andre van Hoorn
 */
public class MessageSequence {

    private long traceId = -1;
    private Vector<Message> seq = null;

    public MessageSequence(long traceId, Vector<Message> seq) {
        this.traceId = traceId;
        this.seq = seq;
    }

    public Vector<Message> getSequenceAsVector() {
        return this.seq;
    }

    public String toString() {
        StringBuilder strBuild = new StringBuilder("Trace " + this.traceId + ":");
        Iterator<Message> it = seq.iterator();
        while (it.hasNext()) {
            Message m = it.next();
            strBuild.append("<");
            strBuild.append(m.toString());
            strBuild.append(">");
        }
        return strBuild.toString();
    }
}
