/***************************************************************************
 * Copyright (C) 2022 OceanDSL (https://oceandsl.uni-kiel.de)
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
package kieker.analysis.architecture.recovery.events;

import java.time.Duration;

import kieker.model.analysismodel.execution.EDirection;

/**
 *
 * @author Reiner Jung
 * @since 1.3.0
 */
public class DataflowEvent {

    private final GenericElementEvent source;
    private final GenericElementEvent target;
    private final EDirection direction;
    private final Duration duration;

    /**
     * Create a new dataflow event between two operations or between one operation and one storage.
     *
     * @param source
     *            source element, either an operation or a storage
     * @param target
     *            target element, either an operation or a storage
     * @param direction
     *            dataflow direction
     * @param duration
     *            duration of the dataflow (is zero in static analysis)
     */
    public DataflowEvent(final GenericElementEvent source, final GenericElementEvent target,
            final EDirection direction, final Duration duration) {
        this.source = source;
        this.target = target;
        this.direction = direction;
        this.duration = duration;
    }

    public GenericElementEvent getSource() {
        return this.source;
    }

    public GenericElementEvent getTarget() {
        return this.target;
    }

    public EDirection getDirection() {
        return this.direction;
    }

    public Duration getDuration() {
        return this.duration;
    }

}
