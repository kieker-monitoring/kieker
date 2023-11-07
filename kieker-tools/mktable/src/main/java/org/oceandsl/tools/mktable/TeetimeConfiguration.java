/***************************************************************************
 * Copyright (C) 2021 OceanDSL (https://oceandsl.uni-kiel.de)
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
package org.oceandsl.tools.mktable;

import java.nio.file.Path;

import teetime.framework.Configuration;
import teetime.stage.InitialElementProducer;
import teetime.stage.basic.distributor.Distributor;
import teetime.stage.basic.distributor.strategy.CopyByReferenceStrategy;

import org.oceandsl.analysis.generic.Table;
import org.oceandsl.analysis.generic.data.MoveOperationEntry;
import org.oceandsl.analysis.generic.source.CsvTableReaderStage;
import org.oceandsl.analysis.generic.source.FileNameLabelMapper;
import org.oceandsl.tools.mktable.stages.CompactTableStage;
import org.oceandsl.tools.mktable.stages.CreateLaTeXTable2;
import org.oceandsl.tools.mktable.stages.SortTableStage;

/**
 * Pipe and Filter configuration generating latex tables from operation movements.
 *
 * @author Reiner Jung
 * @since 1.3.0
 */
public class TeetimeConfiguration extends Configuration {

    public TeetimeConfiguration(final Settings settings) {
        final InitialElementProducer<Path> pathSource = new InitialElementProducer<>(settings.getInputPaths());
        final CsvTableReaderStage<String, MoveOperationEntry> csvReaderStage = new CsvTableReaderStage<>(';', '"', '\\',
                true, MoveOperationEntry.class, new FileNameLabelMapper());
        final SortTableStage sortTableStage = new SortTableStage();
        final Distributor<Table<String, MoveOperationEntry>> distributor = new Distributor<>(
                new CopyByReferenceStrategy());
        final CompactTableStage compactTableStage = new CompactTableStage();

        final CreateLaTeXTable2 createFullLaTeXTable = new CreateLaTeXTable2(
                settings.getOutputPath().resolve("full.tex"));
        final CreateLaTeXTable2 createCompactLaTeXTable = new CreateLaTeXTable2(
                settings.getOutputPath().resolve("compact.tex"));

        this.connectPorts(pathSource.getOutputPort(), csvReaderStage.getInputPort());
        this.connectPorts(csvReaderStage.getOutputPort(), sortTableStage.getInputPort());
        this.connectPorts(sortTableStage.getOutputPort(), distributor.getInputPort());
        this.connectPorts(distributor.getNewOutputPort(), createFullLaTeXTable.getInputPort());
        this.connectPorts(distributor.getNewOutputPort(), compactTableStage.getInputPort());
        this.connectPorts(compactTableStage.getOutputPort(), createCompactLaTeXTable.getInputPort());
    }
}
