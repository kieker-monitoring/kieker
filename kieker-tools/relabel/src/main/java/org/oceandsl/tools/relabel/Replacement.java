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

import java.util.List;

/**
 * Description for one replacement rule.
 *
 * @author Reiner Jung
 * @since 1.1
 */
public class Replacement {

    private final List<String> sources;

    private final List<String> targets;

    public Replacement(final List<String> sources, final List<String> targets) {
        this.sources = sources;
        this.targets = targets;
    }

    public List<String> getSources() {
        return this.sources;
    }

    public List<String> getTargets() {
        return this.targets;
    }
}
