/***************************************************************************
 * Copyright 2011 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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
 ***************************************************************************/

package kieker.tools.traceAnalysis.plugins.messageTraceRepository;

import java.util.Hashtable;

import kieker.analysis.plugin.configuration.AbstractInputPort;
import kieker.analysis.plugin.configuration.IInputPort;
import kieker.tools.traceAnalysis.plugins.AbstractMessageTraceProcessingPlugin;
import kieker.tools.traceAnalysis.systemModel.MessageTrace;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;

/**
 * @author Andre van Hoorn
 */
public class MessageTraceRepositoryPlugin extends AbstractMessageTraceProcessingPlugin {

    //private static final Log log = LogFactory.getLog(MessageTraceRepositoryPlugin.class);

    private final Hashtable<Long, MessageTrace> repo = new Hashtable<Long, MessageTrace>();

    // TODO: handle equivalence classes
    // See ticket http://samoa.informatik.uni-kiel.de:8000/kieker/ticket/150

    public MessageTraceRepositoryPlugin(final String name, final SystemModelRepository systemEntityFactory) {
        super(name, systemEntityFactory);
    }

    public Hashtable<Long, MessageTrace> getMessageTraceRepository() {
        return this.repo;
    }

    private final IInputPort<MessageTrace> messageTraceInputPort =
            new AbstractInputPort<MessageTrace>("Message traces"){

        @Override
        public void newEvent(final MessageTrace mt) {
            MessageTraceRepositoryPlugin.this.repo.put(mt.getTraceId(), mt);
        }
    };

    @Override
    public IInputPort<MessageTrace> getMessageTraceInputPort() {
        return this.messageTraceInputPort;
    }

    @Override
    public boolean execute() {
        return true; // no need to do anything here
    }

    @Override
    public void terminate(final boolean error) {
        // no need to do anything here
    }
}
