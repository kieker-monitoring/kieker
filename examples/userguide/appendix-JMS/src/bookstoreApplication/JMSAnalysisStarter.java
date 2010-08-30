package bookstoreApplication;

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
 *
 */

import kieker.analysis.AnalysisController;
import kieker.analysis.reader.IMonitoringLogReader;
import kieker.analysis.reader.JMSReader;
import kieker.analysis.reader.MonitoringLogReaderException;
import kieker.analysis.plugin.IMonitoringRecordConsumerPlugin;
import kieker.analysis.plugin.MonitoringRecordConsumerException;

/**
 *
 * @author Andre van Hoorn
 */
public class JMSAnalysisStarter {

    private static final long MAX_RT_NANOS = 1700;

    public static void main(String[] args) throws MonitoringLogReaderException, MonitoringRecordConsumerException {
        AnalysisController analysisInstance = new AnalysisController();
        IMonitoringLogReader logReader =
                new JMSReader("tcp://127.0.0.1:3035/", "queue1");
        analysisInstance.setLogReader(logReader);
        IMonitoringRecordConsumerPlugin consumer =
                new Consumer(MAX_RT_NANOS);
        analysisInstance.registerPlugin(consumer);

        analysisInstance.run();
    }
}
