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
package kieker.tools.mop.merge;

import java.util.Map.Entry;

import kieker.model.analysismodel.statistics.StatisticRecord;
import kieker.model.analysismodel.statistics.StatisticsFactory;

/**
 * @author Reiner Jung
 * @since 1.1
 */
public final class StatisticsModelCloneUtils {

    private StatisticsModelCloneUtils() {
        // Utility class
    }

    public static StatisticRecord duplicate(final StatisticRecord value) {
        final StatisticRecord newValue = StatisticsFactory.eINSTANCE.createStatisticRecord();

        for (final Entry<String, Object> statistics : value.getProperties()) {
            newValue.getProperties().put(statistics.getKey(),
                    StatisticsModelCloneUtils.duplicateObject(statistics.getValue()));
        }

        return newValue;

    }

    public static Object duplicateObject(final Object value) {
        if (value instanceof Long) {
            return ((Long) value).longValue();
        } else if (value instanceof Double) {
            return ((Double) value).doubleValue();
        } else {
            throw new InternalError("Unknown type");
        }
    }

    public static Object compute(final Object newValue, final Object value) {
        if (newValue instanceof Long) {
            return (Long) newValue + (Long) value;
        } else {
            throw new InternalError("Unknown type");
        }
    }

}
