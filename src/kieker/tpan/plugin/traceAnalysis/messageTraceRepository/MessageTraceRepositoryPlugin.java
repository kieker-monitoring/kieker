package kieker.tpan.plugin.traceAnalysis.messageTraceRepository;

import java.util.Hashtable;
import kieker.tpan.datamodel.MessageTrace;
import kieker.tpan.datamodel.repository.SystemModelRepository;
import kieker.tpan.plugin.traceAnalysis.AbstractMessageTraceProcessingPlugin;
import kieker.tpan.plugin.traceAnalysis.traceReconstruction.TraceProcessingException;

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
public class MessageTraceRepositoryPlugin extends AbstractMessageTraceProcessingPlugin {

    private static final Log log = LogFactory.getLog(MessageTraceRepositoryPlugin.class);
    private final Hashtable<Long, MessageTrace> repo = new Hashtable<Long, MessageTrace>();

    // TODO: handle equivalence classes

    public MessageTraceRepositoryPlugin(final String name, final SystemModelRepository systemEntityFactory) {
        super(name, systemEntityFactory);
    }

    public Hashtable<Long, MessageTrace> getMessageTraceRepository() {
        return this.repo;
    }

    public void newEvent(MessageTrace mt) throws TraceProcessingException {
        this.repo.put(mt.getTraceId(), mt);
    }

    public boolean execute() {
        return true; // no need to do anything here
    }

    public void terminate(boolean error) {
        // no need to do anything here
    }
}
