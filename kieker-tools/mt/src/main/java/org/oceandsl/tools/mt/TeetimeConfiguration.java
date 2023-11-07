/***************************************************************************
 * Copyright (C) 2023 OceanDSL (https://oceandsl.uni-kiel.de)
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
package org.oceandsl.tools.mt;

import java.io.IOException;
import java.util.List;

import org.apache.commons.text.similarity.LevenshteinDistance;

import kieker.analysis.generic.clustering.ExtractDBScanClustersStage;
import kieker.analysis.generic.clustering.mtree.IDistanceFunction;
import kieker.analysis.generic.clustering.mtree.MTreeGeneratorStage;
import kieker.analysis.generic.clustering.optics.OpticsData;
import kieker.analysis.generic.clustering.optics.OpticsStage;

import teetime.framework.Configuration;
import teetime.framework.OutputPort;
import teetime.stage.basic.distributor.Distributor;
import teetime.stage.basic.distributor.strategy.CopyByReferenceStrategy;

import org.oceandsl.analysis.generic.Table;
import org.oceandsl.analysis.generic.data.MoveOperationEntry;
import org.oceandsl.analysis.generic.source.CsvTableReaderProducerStage;
import org.oceandsl.analysis.generic.stages.SingleFileTableCsvSink;
import org.oceandsl.analysis.generic.stages.TableCsvSink;
import org.oceandsl.tools.mt.stages.ConstructTableStage;
import org.oceandsl.tools.mt.stages.ConvertTableToOpticsDataStage;
import org.oceandsl.tools.mt.stages.SortModelStage;

/**
 * Pipe and Filter configuration for the architecture creation tool.
 *
 * @author Reiner Jung
 * @since 1.4.0
 */
public class TeetimeConfiguration extends Configuration {

    public TeetimeConfiguration(final Settings settings) throws IOException {

        final String inputFileName = settings.getInputTable().getFileName().toString();

        final CsvTableReaderProducerStage<String, MoveOperationEntry> csvTableReaderStage = new CsvTableReaderProducerStage<>(
                settings.getInputTable(), ';', '"', '\\', true, MoveOperationEntry.class,
                inputFileName.substring(0, inputFileName.lastIndexOf('.')));

        OutputPort<Table<String, MoveOperationEntry>> outputPort = csvTableReaderStage.getOutputPort();

        if (settings.getClusteringDistance() != null) {
            outputPort = this.createClustering(settings, inputFileName, outputPort);
        }

        if (settings.getSortDescription() != null) {
            final SortModelStage<MoveOperationEntry> sortModelStage = new SortModelStage<>(
                    settings.getSortDescription());
            this.connectPorts(outputPort, sortModelStage.getInputPort());
            outputPort = sortModelStage.getOutputPort();
        }

        final SingleFileTableCsvSink<String, MoveOperationEntry> tableSink = new SingleFileTableCsvSink<>(
                settings.getOutputTable(), MoveOperationEntry.class, true, TableCsvSink.LF);

        this.connectPorts(outputPort, tableSink.getInputPort());
    }

    private OutputPort<Table<String, MoveOperationEntry>> createClustering(final Settings settings,
            final String inputFileName, final OutputPort<Table<String, MoveOperationEntry>> outputPort) {
        final IDistanceFunction<OpticsData<MoveOperationEntry>> distanceFunction = new IDistanceFunction<>() {

            final LevenshteinDistance distance = new LevenshteinDistance();

            @Override
            public double calculate(final OpticsData<MoveOperationEntry> data1,
                    final OpticsData<MoveOperationEntry> data2) {
                final String left = data1.getData().getOperationName();
                final String right = data2.getData().getOperationName();
                return (double) this.distance.apply(left, right) / (double) (left.length() + right.length());
            }
        };

        final ConvertTableToOpticsDataStage converterStage = new ConvertTableToOpticsDataStage();

        final Distributor<List<OpticsData<MoveOperationEntry>>> distributor = new Distributor<>(
                new CopyByReferenceStrategy());

        final MTreeGeneratorStage<OpticsData<MoveOperationEntry>> mTreeGeneratorStage = new MTreeGeneratorStage<>(
                distanceFunction);

        final OpticsStage<MoveOperationEntry> opticsStage = new OpticsStage<>(settings.getClusteringDistance(),
                settings.getMinPtr());

        final ExtractDBScanClustersStage<MoveOperationEntry> clustering = new ExtractDBScanClustersStage<>(
                settings.getClusteringDistance());

        final ConstructTableStage constructTableStage = new ConstructTableStage(inputFileName);

        this.connectPorts(outputPort, converterStage.getInputPort());
        this.connectPorts(converterStage.getOutputPort(), distributor.getInputPort());
        this.connectPorts(distributor.getNewOutputPort(), mTreeGeneratorStage.getInputPort());
        this.connectPorts(mTreeGeneratorStage.getOutputPort(), opticsStage.getMTreeInputPort());
        this.connectPorts(distributor.getNewOutputPort(), opticsStage.getModelsInputPort());
        this.connectPorts(opticsStage.getOutputPort(), clustering.getInputPort());
        this.connectPorts(clustering.getOutputPort(), constructTableStage.getInputPort());

        return constructTableStage.getOutputPort();
    }
}
