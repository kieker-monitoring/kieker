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
package kieker.tools.relabel;

import java.util.Arrays;
import java.util.List;

import com.beust.jcommander.IStringConverter;

/**
 *
 * @author Reiner Jung
 * @since 1.1
 */
public class ReplacementConverter implements IStringConverter<Replacement> {

    @Override
    public Replacement convert(final String value) {
        final String[] rule = value.split(":");
        if (rule.length == 2) {
            final List<String> sources = Arrays.asList(rule[0].split(","));
            final List<String> targets = Arrays.asList(rule[1].split(","));
            return new Replacement(sources, targets);
        } else {
            System.err.printf("Format of replacement rule is not supported: %s", value); // NOPMD
            return null;
        }
    }

}
