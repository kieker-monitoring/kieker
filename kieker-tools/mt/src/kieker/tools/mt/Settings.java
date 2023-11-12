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
package kieker.tools.mt;

import java.nio.file.Path;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.PathConverter;

/**
 * All settings including command line parameters.
 *
 * @author Reiner Jung
 * @since 1.4.0
 */
public class Settings { // NOPMD data class

    @Parameter(names = { "-i",
            "--input" }, required = true, variableArity = true, converter = PathConverter.class, description = "Input table")
    private Path inputTable;

    @Parameter(names = { "-o",
            "--output" }, required = true, converter = PathConverter.class, description = "Output table")
    private Path outputTable;

    @Parameter(names = { "-s",
            "--sort" }, required = false, converter = SortDescriptorConverter.class, description = "Sort description")
    private SortDescriptor sortDescription;

    @Parameter(names = { "-p", "--min-ptr" }, required = false, description = "Min ptr value for clustering")
    private Integer minPtr;

    @Parameter(names = { "-d", "--cluster-distance" }, required = false, description = "Min cluster distance")
    private Double clusteringDistance;

    public Path getInputTable() {
        return this.inputTable;
    }

    public Path getOutputTable() {
        return this.outputTable;
    }

    public SortDescriptor getSortDescription() {
        return this.sortDescription;
    }

    public int getMinPtr() {
        if (this.minPtr == null) {
            return 1;
        } else {
            return this.minPtr;
        }
    }

    public Double getClusteringDistance() {
        return this.clusteringDistance;
    }

}
