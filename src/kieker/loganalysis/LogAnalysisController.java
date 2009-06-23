package kieker.loganalysis;

/**
 * kieker.loganalysis.LogAnalysisController
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
 */
import java.util.Vector;
import kieker.loganalysis.consumer.IMonitoringRecordConsumer;
import kieker.loganalysis.consumer.MonitoringRecordLogger;
import kieker.loganalysis.logReader.FSReader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Andre van Hoorn
 */
public class LogAnalysisController {

    private static final Log log = LogFactory.getLog(LogAnalysisController.class);
    private FSReader logReader = null;
    private Vector<IMonitoringRecordConsumer> consumers = new Vector<IMonitoringRecordConsumer>();

    public static void main(String args[]) {
        log.info("Hi, this is Kieker.LogAnalysis");

        String inputDir = System.getProperty("inputDir");
        if (inputDir == null || inputDir.length() == 0 || inputDir.equals("${inputDir}")) {
            log.error("No input dir found!");
            log.error("Provide an input dir as system property.");
            log.error("Example to read all tpmon-* files from /tmp:\n" +
                    "                    ant -DinputDir=/tmp/ run-logAnalysis    ");
            System.exit(1);
        } else {
            log.info("Reading all tpmon-* files from " + inputDir);
        }

        LogAnalysisController analysisInstance = new LogAnalysisController();
        analysisInstance.setLogReader(new FSReader(inputDir));
        analysisInstance.addConsumer(new MonitoringRecordLogger());
        analysisInstance.run();
        log.info("Bye, this was Kieker.LogAnalysis");
        System.exit(0);
    }

    public void run() {
        for (IMonitoringRecordConsumer c : this.consumers) {
            this.logReader.registerConsumer(c);
            c.run();
        }
        this.logReader.run();
    }

    public void setLogReader(FSReader reader) {
        this.logReader = reader;
    }

    public void addConsumer(IMonitoringRecordConsumer consumer) {
        this.consumers.add(consumer);
    }
}
