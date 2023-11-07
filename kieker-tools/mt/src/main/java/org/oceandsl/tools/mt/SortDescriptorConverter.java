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
package org.oceandsl.tools.mt;

import com.beust.jcommander.IStringConverter;

/**
 * Convert input string into a correct setup for the sort descriptor.
 *
 * @author Reiner Jung
 * @since 1.4.0
 */
public class SortDescriptorConverter implements IStringConverter<SortDescriptor> {

    @Override
    public SortDescriptor convert(final String value) {
        final SortDescriptor sortDescriptor = new SortDescriptor();
        final String[] sortCriteriums = value.split(",");
        for (final String sortCriterium : sortCriteriums) {
            final String[] params = sortCriterium.split(":");
            sortDescriptor.getSortCriteria().add(new SortCriterium(params[0], EOrder.valueOf(params[1].toUpperCase())));
        }
        return sortDescriptor;
    }

}
