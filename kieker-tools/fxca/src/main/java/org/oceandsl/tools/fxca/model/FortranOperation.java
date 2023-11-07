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

import org.w3c.dom.Node;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Reiner Jung
 * @since 1.3.0
 */
public class FortranOperation extends MMObject implements IDataflowEndpoint, IContainable {

    private static final long serialVersionUID = -3656752458538237388L;

    @Getter
    private final String name;

    @Getter
    private final Map<String, CommonBlock> commonBlocks = new ContainmentHashMap<>(this);

    @Getter
    private final Map<String, FortranVariable> variables = new ContainmentHashMap<>(this);

    @Getter
    private final Map<String, FortranParameter> parameters = new ContainmentHashMap<>(this);

    @Getter
    private final Set<String> usedModules = new HashSet<>();

    @Getter
    private final Node node;

    @Getter
    @Setter
    private FortranModule module;

    @Getter
    @Setter
    private boolean implicit;

    @Getter
    private final boolean variableArguments;

    @Getter
    private final boolean function;

    public FortranOperation(final String name, final Node node, final boolean function) {
        this.name = name;
        this.node = node;
        this.variableArguments = false;
        this.function = function;
    }

    public FortranOperation(final String name, final Node node, final boolean function,
            final boolean variableArguments) {
        this.name = name;
        this.node = node;
        this.variableArguments = variableArguments;
        this.function = function;
    }

    @Override
    public String toString() {
        return "op " + this.name;
    }

}
