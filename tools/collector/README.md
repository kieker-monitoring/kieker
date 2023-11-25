# How to configure the collector

The collector uses a central configuration file to configure its features.

It is invoked using collector -c collector.config

## Configuration file

Common Kieker parameters
```
kieker.monitoring.name=${EXPERIMENT_ID}
kieker.monitoring.hostname=
kieker.monitoring.metadata=true
```

TCP collector
```
kieker.tools.source=kieker.tools.source.MultipleConnectionTcpSourceCompositeStage
kieker.tools.source.MultipleConnectionTcpSourceCompositeStage.port=9876
kieker.tools.source.MultipleConnectionTcpSourceCompositeStage.capacity=8192
```

DataSinkStage configuration
```
kieker.monitoring.writer=kieker.monitoring.writer.filesystem.FileWriter
kieker.monitoring.writer.filesystem.FileWriter.customStoragePath=$COLLECTOR_DATA_DIR/
kieker.monitoring.writer.filesystem.FileWriter.charsetName=UTF-8
kieker.monitoring.writer.filesystem.FileWriter.maxEntriesInFile=25000
kieker.monitoring.writer.filesystem.FileWriter.maxLogSize=-1
kieker.monitoring.writer.filesystem.FileWriter.maxLogFiles=-1
kieker.monitoring.writer.filesystem.FileWriter.mapFileHandler=kieker.monitoring.writer.filesystem.TextMapFileHandler
kieker.monitoring.writer.filesystem.TextMapFileHandler.flush=true
kieker.monitoring.writer.filesystem.TextMapFileHandler.compression=kieker.monitoring.writer.filesystem.compression.NoneCompressionFilter
kieker.monitoring.writer.filesystem.FileWriter.logFilePoolHandler=kieker.monitoring.writer.filesystem.RotatingLogFilePoolHandler
kieker.monitoring.writer.filesystem.FileWriter.logStreamHandler=kieker.monitoring.writer.filesystem.TextLogStreamHandler
kieker.monitoring.writer.filesystem.FileWriter.flush=true
kieker.monitoring.writer.filesystem.FileWriter.bufferSize=81920

## Command line setup

You may specify a logging configuration with:

export COLLECTOR_OPTS=-Dlog4j.configuration=file:///$BASE_DIR/log4j.cfg
