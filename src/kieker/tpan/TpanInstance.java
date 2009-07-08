package kieker.tpan;

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
import java.util.Vector;
import kieker.common.logReader.IKiekerRecordConsumer;
import kieker.common.logReader.IKiekerLogReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Andre van Hoorn
 */
public class TpanInstance {

    private static final Log log = LogFactory.getLog(TpanInstance.class);
    private final Vector<IKiekerLogReader> logReaders = new Vector<IKiekerLogReader>();
    private final Vector<IKiekerRecordConsumer> consumers = new Vector<IKiekerRecordConsumer>();

    public void run() {
        for (IKiekerLogReader r : this.logReaders) {
            for (IKiekerRecordConsumer c : this.consumers) {
                r.addConsumer(c, c.getRecordTypeSubscriptionList());
                c.execute();
            }
        }
        for (IKiekerLogReader r : this.logReaders) {
            r.execute();
        }
    }

    public void setLogReader(IKiekerLogReader reader) {
        this.logReaders.add(reader);
    }

    public void addRecordConsumer(IKiekerRecordConsumer consumer) {
        this.consumers.add(consumer);
    }
}
