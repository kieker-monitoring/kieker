/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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

import lombok.Getter;
import lombok.Setter;

import kieker.model.analysismodel.execution.EDirection;

/**
 * @author Reiner Jung
 * @since 1.3.0
 *
 */
public class DataflowObject extends MMObject {

    private static final long serialVersionUID = -4196503062613279597L;

    @Getter
    private final String name;

    @Getter
    @Setter
    private String type;

    @Getter
    @Setter
    private EDirection direction;

    public DataflowObject(final String name) {
        this.direction = EDirection.NONE;
        this.name = name;
    }

    public void addDirection(final EDirection value) {
        if (this.direction == null) {
            this.direction = value;
        } else {
            switch (this.direction) {
            case READ:
                if ((value == EDirection.WRITE) || (value == EDirection.BOTH)) {
                    this.direction = EDirection.BOTH;
                } else {
                    this.direction = EDirection.READ;
                }
                break;
            case WRITE:
                if ((value == EDirection.READ) || (value == EDirection.BOTH)) {
                    this.direction = EDirection.BOTH;
                } else {
                    this.direction = EDirection.WRITE;
                }
                break;
            case BOTH:
                this.direction = EDirection.BOTH;
                break;
            default:
                this.direction = value;
                break;
            }
        }
    }
}
