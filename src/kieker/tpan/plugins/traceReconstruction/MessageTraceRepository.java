package kieker.tpan.plugins.traceReconstruction;

import java.util.Hashtable;
import kieker.tpan.datamodel.MessageTrace;
import kieker.tpan.plugins.IEventListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/*
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
 */

/**
 * @author Andre van Hoorn
 */
public class MessageTraceRepository implements IEventListener<MessageTrace> {

    private static final Log log = LogFactory.getLog(MessageTraceRepository.class);
    private final Hashtable<Long, MessageTrace> repo = new Hashtable<Long, MessageTrace>();

    // TODO: handle equivalence classes

    public MessageTraceRepository() {
    }

    public Hashtable<Long, MessageTrace> getMessageTraceRepository() {
        return this.repo;
    }

    public void newEvent(MessageTrace mt) throws TraceProcessingException {
        this.repo.put(mt.getTraceId(), mt);
    }
}
