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
package kieker.tools.mvis;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.PathConverter;

import org.oceandsl.analysis.generic.validators.PathIsDirectoryValidator;
import org.oceandsl.analysis.generic.validators.PathIsModelDirectoryValidator;
import org.oceandsl.analysis.graph.EGraphGenerationMode;
import org.oceandsl.analysis.graph.IGraphElementSelector;

/**
 * All settings including command line parameters for the analysis.
 *
 * @author Reiner Jung
 * @since 1.0
 */
public class Settings { // NOPMD

    @Parameter(names = { "-i", "--input" }, required = true, converter = PathConverter.class, validateWith = {
            PathIsDirectoryValidator.class,
            PathIsModelDirectoryValidator.class }, description = "Input model directory")
    private Path inputDirectory;

    @Parameter(names = { "-o",
            "--output" }, required = true, converter = PathConverter.class, description = "Output directory to store graphics and statistics")
    private Path outputDirectory;

    @Parameter(names = { "-M",
            "--component-map" }, required = false, converter = PathConverter.class, description = "Component, file and function map file")
    private Path componentMapFile;

    @Parameter(names = { "-s",
            "--selector" }, required = true, converter = SelectorKindConverter.class, description = "Set architecture graph selector")
    private IGraphElementSelector selector;

    @Parameter(names = { "-g",
            "--graphs" }, required = false, variableArity = true, converter = GraphTypeConverter.class, description = "Specify which output graphs must be generated")
    private List<EOutputGraph> outputGraphs;

    @Parameter(names = { "-c",
            "--compute-statistics" }, variableArity = true, required = false, converter = StatisticsConverter.class, description = "Generate the listed statistics.")
    private Set<EStatistics> computeStatistics;

    @Parameter(names = { "-m",
            "--mode" }, required = true, variableArity = true, converter = GraphGenerationConverter.class, description = "Mode deciding whether an edge is added when its nodes are not selected")
    private EGraphGenerationMode graphGenerationMode;

    @Parameter(names = "--eol", required = false, description = "End of line symbol")
    private String lineSeparator = System.lineSeparator();

    public Path getInputDirectory() {
        return this.inputDirectory;
    }

    public Path getOutputDirectory() {
        return this.outputDirectory;
    }

    public Path getComponentMapFile() {
        return this.componentMapFile;
    }

    public List<EOutputGraph> getOutputGraphs() {
        return this.outputGraphs;
    }

    public IGraphElementSelector getSelector() {
        return this.selector;
    }

    public Set<EStatistics> getComputeStatistics() {
        return this.computeStatistics;
    }

    public EGraphGenerationMode getGraphGenerationMode() {
        return this.graphGenerationMode;
    }

    public char[] getLineSeparator() {
        return this.lineSeparator.toCharArray();
    }

    public void setLineSeparator(final String lineSeparator) {
        this.lineSeparator = lineSeparator;
    }
}
