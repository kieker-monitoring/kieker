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
package kieker.tools.maa;

import java.nio.file.Path;
import java.util.List;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.PathConverter;

/**
 * @author Reiner Jung
 * @since 1.2
 */
public class Settings {

    @Parameter(names = { "-i",
            "--input" }, required = true, variableArity = false, converter = PathConverter.class, description = "Input architecture model directory")
    private Path inputModelPath;

    @Parameter(names = { "-o",
            "--output" }, required = true, variableArity = false, converter = PathConverter.class, description = "Output architecture model directory")
    private Path outputModelPath;

    @Parameter(names = { "-E", "--experiment-name" }, required = false, description = "Name of the output model")
    private String experimentName;

    @Parameter(names = { "-I",
            "--compute-interfaces" }, required = false, description = "Compute interfaces based on aggregated invocations")
    private boolean computeInterfaces;

    @Parameter(names = { "-g",
            "--hierarchy-grouping" }, required = false, description = "Generate a component hierarchy based on a map file")
    private List<Path> mapFiles;

    @Parameter(names = { "-gs", "--separator" }, required = false, description = "Sparator string for CSV inputs")
    private String separator;

    @Parameter(names = { "-c", "--operation-calls" }, required = false, description = "Output the list of calls")
    private boolean operationCalls;

    @Parameter(names = { "-s",
            "--component-statistics" }, required = false, description = "Output numerous component statistics")
    private boolean componentStatistics;

    @Parameter(names = "--eol", required = false, description = "End of line symbol")
    private String lineSeparator = System.lineSeparator();

    public Path getInputModelPath() {
        return this.inputModelPath;
    }

    public Path getOutputModelPath() {
        return this.outputModelPath;
    }

    public boolean isComputeInterfaces() {
        return this.computeInterfaces;
    }

    public List<Path> getMapFiles() {
        return this.mapFiles;
    }

    public boolean isOperationCalls() {
        return this.operationCalls;
    }

    public boolean isComponentStatistics() {
        return this.componentStatistics;
    }

    public String getSeparator() {
        return this.separator;
    }

    public String getExperimentName() {
        return this.experimentName;
    }

    public char[] getLineSeparator() {
        return this.lineSeparator.toCharArray();
    }

    public void setLineSeparator(final String lineSeparator) {
        this.lineSeparator = lineSeparator;
    }
}
