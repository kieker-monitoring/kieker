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
package org.oceandsl.tools.relabel;

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

import java.nio.file.Path;
import java.util.List;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.PathConverter;

/**
 * All settings including command line parameters for the analysis.
 *
 * @author Reiner Jung
 * @since 1.0
 */
public class Settings {

    @Parameter(names = { "-i",
            "--input" }, required = true, converter = PathConverter.class, description = "Directory for the input model")
    private Path inputDirectory;

    @Parameter(names = { "-o",
            "--output" }, required = true, converter = PathConverter.class, description = "Directory for the modified model")
    private Path outputDirectory;

    @Parameter(names = { "-r",
            "--replacements" }, required = true, variableArity = true, converter = ReplacementConverter.class, description = "Replacement for labels s1,...,sn:t1,...,tm")
    private List<Replacement> replacements;

    @Parameter(names = { "-e",
            "--experiment" }, required = false, description = "Set experiment name of the model repository.")
    private String experimentName;

    public Path getOutputDirectory() {
        return this.outputDirectory;
    }

    public Path getInputDirectory() {
        return this.inputDirectory;
    }

    public List<Replacement> getReplacements() {
        return this.replacements;
    }

    public String getExperimentName() {
        return this.experimentName;
    }
}
