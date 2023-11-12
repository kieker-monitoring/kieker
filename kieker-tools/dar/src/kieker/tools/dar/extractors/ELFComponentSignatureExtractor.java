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
package kieker.tools.dar.extractors;

import java.nio.file.Path;
import java.nio.file.Paths;

import kieker.analysis.architecture.recovery.signature.IComponentSignatureExtractor;
import kieker.model.analysismodel.type.ComponentType;

/**
 * @author Reiner Jung
 * @since 1.2
 *
 */
public class ELFComponentSignatureExtractor implements IComponentSignatureExtractor {

    private final String experimentName;

    public ELFComponentSignatureExtractor(final String experimentName) {
        this.experimentName = experimentName;
    }

    @Override
    public void extract(final ComponentType componentType) {
        String signature = componentType.getSignature();
        if (signature == null) {
            signature = "-- none --";
        }
        final Path path = Paths.get(signature);
        final String name = path.getName(path.getNameCount() - 1).toString();
        final String rest = path.getParent() != null ? this.experimentName + "." + path.getParent().toString()
                : this.experimentName;
        componentType.setName(name);
        componentType.setPackage(rest);
    }

}
