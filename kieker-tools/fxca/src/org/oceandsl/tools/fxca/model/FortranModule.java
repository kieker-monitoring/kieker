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
package org.oceandsl.tools.fxca.model;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.w3c.dom.Document;

import lombok.Getter;

/**
 * @author Henning Schnoor
 * @since 1.3.0
 */
public class FortranModule extends MMObject implements IContainable {

    private static final long serialVersionUID = 870011289028135834L;

    @Getter
    private final Set<String> usedModules = new HashSet<>();
    @Getter
    private final Map<String, FortranOperation> operations = new ContainmentHashMap<>(this);
    @Getter
    private final String moduleName;
    @Getter
    private final boolean namedModule;
    @Getter
    private final Document document;
    @Getter
    private final String fileName;
    @Getter
    private final Map<String, CommonBlock> commonBlocks = new ContainmentHashMap<>(this);
    @Getter
    private final Map<String, FortranVariable> variables = new ContainmentHashMap<>(this);

    public FortranModule(final String moduleName, final String fileName, final boolean namedModule,
            final Document document) {
        this.moduleName = moduleName;
        this.fileName = fileName;
        this.namedModule = namedModule;
        this.document = document;
    }

    @Override
    public String toString() {
        return this.fileName + ":" + this.moduleName;
    }

}
