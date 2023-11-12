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

import com.beust.jcommander.IStringConverter;

import org.oceandsl.analysis.graph.EGraphGenerationMode;

/**
 * Convert command line parameter strings to @{link EGraphGenerationMode}s.
 *
 * @author Reiner Jung
 * @since 1.1
 */
public class GraphGenerationConverter implements IStringConverter<EGraphGenerationMode> {

    @Override
    public EGraphGenerationMode convert(final String value) {
        for (final EGraphGenerationMode mode : EGraphGenerationMode.values()) {
            if (mode.getKey().equals(value)) {
                return mode;
            }
        }
        throw new IllegalArgumentException(String.format("Graph generation mode %s is not supported.", value));
    }

}
