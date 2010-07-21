package kieker.analysis.plugin.configuration;

import java.util.ArrayList;
import java.util.Collection;
import kieker.analysis.plugin.IAnalysisEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
 *
 * @author Andre van Hoorn
 */
public class OutputPort<T extends IAnalysisEvent> implements IOutputPort<T> {

    private static final Log log = LogFactory.getLog(OutputPort.class);
    /** Should use "better" data structure from java.concurrent */
    private final Collection<IInputPort<T>> subscriber =
            new ArrayList<IInputPort<T>>();
    private final String description;

    private OutputPort() {
        this.description = null;
    }

    public OutputPort(final String description) {
        this.description = description;
    }

    public synchronized void deliver(T event) {
        for (IInputPort<T> l : this.subscriber) {
            l.newEvent(event);
        }
    }

    public synchronized void subscribe(IInputPort<T> subscriber) {
        this.subscriber.add(subscriber);
    }

    public synchronized void unsubscribe(IInputPort<T> subscriber) {
        this.subscriber.remove(subscriber);
    }

    public String getDescription() {
        return this.description;
    }
}
