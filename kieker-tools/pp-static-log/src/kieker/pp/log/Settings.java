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
package kieker.pp.log;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.FileConverter;
import com.beust.jcommander.converters.PathConverter;

/**
 * All settings including command line parameters for the analysis.
 *
 * @author Reiner Jung
 * @since 1.0
 */
public class Settings {

    @Parameter(names = { "-i",
            "--input" }, required = true, converter = PathConverter.class, description = "Input 3 column static log file")
    private Path inputPath;

    @Parameter(names = { "-m",
            "--map" }, required = true, variableArity = true, converter = PathConverter.class, description = "Function to file map")
    private List<Path> mapPaths;

    @Parameter(names = { "-o",
            "--output" }, required = true, converter = FileConverter.class, description = "Output file for 4 column log")
    private File outputFile;

    public Path getInputPath() {
        return this.inputPath;
    }

    public List<Path> getMapPaths() {
        return this.mapPaths;
    }

    public File getOutputFile() {
        return this.outputFile;
    }

}
