package kieker.tpan.plugins;

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
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Andre van Hoorn
 */
public class EventPublishSubscribeConnector<T> {

    private static final Log log = LogFactory.getLog(EventPublishSubscribeConnector.class);
    /** Should use "better" data structure from java.concurrent */
    private final Collection<IEventListener<T>> listeners =
            new ArrayList<IEventListener<T>>();
    private final boolean failFast;

    /**
     *
     * @param failFast iff true, publish throws EventProcessingException as soon
     * as listener throws exeption
     */
    public EventPublishSubscribeConnector(final boolean failFast) {
        this.failFast = failFast;
    }

    /**
     * Sends event to all subscribed listeners.
     *
     * @param event
     * @return true iff all listeners successfully received event.
     * @throws EventProcessingException if failFast enabled and an EventProcessingException
     * occurs while sending the event to the listeners.
     */
    public synchronized boolean publish(final T event) throws EventProcessingException {
        boolean success = true;
        if (this.failFast) {
            for (IEventListener<T> l : this.listeners) {
                l.newEvent(event);
            }
            return true;
        } else {
            for (IEventListener<T> l : this.listeners) {
                try {
                    l.newEvent(event);
                } catch (EventProcessingException ex) {
                    log.warn("Listener threw EventProcessingException", ex);
                    success = false;
                }
            }
        }
        return success;
    }

    public synchronized void addListener(IEventListener<T> l) {
        this.listeners.add(l);
    }

    public synchronized boolean removeListener(IEventListener<T> l) {
        return this.listeners.remove(l);
    }
}
