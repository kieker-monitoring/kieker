/***************************************************************************
 * Copyright (C) 2021 Kieker Project (https://kieker-monitoring.net)
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
package kieker.log.rewriter;

import java.io.IOException;

import kieker.analysis.generic.sink.DataSink;
import kieker.tools.source.LogsReaderCompositeStage;

import teetime.framework.Configuration;

import org.oceandsl.analysis.generic.stages.RewriteBeforeAndAfterEventsStage;

/**
 * @author Reiner Jung
 * @since 1.0
 */
public class TeetimeConfiguration extends Configuration {

    private static final String TRUE_VALUE = "true";
    private static final String FALSE_VALUE = "false";

    public TeetimeConfiguration(final Settings parameterConfiguration) throws IOException {
        final LogsReaderCompositeStage reader = new LogsReaderCompositeStage(parameterConfiguration.getInputFiles(),
                false, 8192);
        final RewriteBeforeAndAfterEventsStage processor = new RewriteBeforeAndAfterEventsStage(
                parameterConfiguration.getAddrlineExecutable(), parameterConfiguration.getModelExecutable(), false);
        final DataSink writer = new DataSink(this.createConfiguration(parameterConfiguration));

        this.connectPorts(reader.getOutputPort(), processor.getInputPort());
        this.connectPorts(processor.getOutputPort(), writer.getInputPort());
    }

    private kieker.common.configuration.Configuration createConfiguration(final Settings parameterConfiguration)
            throws IOException {
        // Configuration for the data sink stage
        final kieker.common.configuration.Configuration configuration = new kieker.common.configuration.Configuration();
        configuration.setProperty("kieker.monitoring.name", "KIEKER");
        configuration.setProperty("kieker.monitoring.enabled", TeetimeConfiguration.TRUE_VALUE);
        configuration.setProperty("kieker.monitoring.initialExperimentId", "transcoded");
        configuration.setProperty("kieker.monitoring.metadata", TeetimeConfiguration.TRUE_VALUE);
        configuration.setProperty("kieker.monitoring.setLoggingTimestamp", TeetimeConfiguration.TRUE_VALUE);
        configuration.setProperty("kieker.monitoring.useShutdownHook", TeetimeConfiguration.TRUE_VALUE);
        configuration.setProperty("kieker.monitoring.jmx", TeetimeConfiguration.FALSE_VALUE);

        configuration.setProperty("kieker.monitoring.timer",
                kieker.monitoring.timer.SystemNanoTimer.class.getCanonicalName());
        configuration.setProperty("kieker.monitoring.timer.SystemMilliTimer.unit", "0");
        configuration.setProperty("kieker.monitoring.timer.SystemNanoTimer.unit", "0");
        configuration.setProperty("kieker.monitoring.writer",
                kieker.monitoring.writer.filesystem.FileWriter.class.getCanonicalName());
        configuration.setProperty("kieker.monitoring.core.controller.WriterController.RecordQueueFQN",
                org.jctools.queues.MpscArrayQueue.class.getCanonicalName());
        configuration.setProperty("kieker.monitoring.core.controller.WriterController.RecordQueueSize", "10000");
        configuration.setProperty("kieker.monitoring.core.controller.WriterController.RecordQueueInsertBehavior", "1");
        configuration.setProperty("kieker.monitoring.writer.filesystem.FileWriter.customStoragePath",
                parameterConfiguration.getOutputFile().getCanonicalPath());
        configuration.setProperty("kieker.monitoring.writer.filesystem.FileWriter.charsetName", "UTF-8");
        configuration.setProperty("kieker.monitoring.writer.filesystem.FileWriter.maxEntriesInFile", "25000");
        configuration.setProperty("kieker.monitoring.writer.filesystem.FileWriter.maxLogSize", "-1");
        configuration.setProperty("kieker.monitoring.writer.filesystem.FileWriter.maxLogFiles", "-1");
        configuration.setProperty("kieker.monitoring.writer.filesystem.FileWriter.mapFileHandler",
                kieker.monitoring.writer.filesystem.TextMapFileHandler.class.getCanonicalName());
        configuration.setProperty("kieker.monitoring.writer.filesystem.TextMapFileHandler.flush",
                TeetimeConfiguration.TRUE_VALUE);
        configuration.setProperty("kieker.monitoring.writer.filesystem.TextMapFileHandler.compression",
                kieker.monitoring.writer.compression.NoneCompressionFilter.class.getCanonicalName());
        configuration.setProperty("kieker.monitoring.writer.filesystem.FileWriter.logFilePoolHandler",
                kieker.monitoring.writer.filesystem.RotatingLogFilePoolHandler.class.getCanonicalName());
        configuration.setProperty("kieker.monitoring.writer.filesystem.FileWriter.logStreamHandler",
                kieker.monitoring.writer.filesystem.TextLogStreamHandler.class.getCanonicalName());
        configuration.setProperty("kieker.monitoring.writer.filesystem.FileWriter.flush",
                TeetimeConfiguration.TRUE_VALUE);
        configuration.setProperty("kieker.monitoring.writer.filesystem.BinaryFileWriter.bufferSize", "8192");
        configuration.setProperty("kieker.monitoring.writer.filesystem.BinaryFileWriter.compression",
                kieker.monitoring.writer.compression.NoneCompressionFilter.class.getCanonicalName());

        return configuration;
    }
}
