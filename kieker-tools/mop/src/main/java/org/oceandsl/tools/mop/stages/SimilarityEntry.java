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
package org.oceandsl.tools.mop.stages;

import org.csveed.annotations.CsvCell;
import org.csveed.annotations.CsvConverter;
import org.csveed.annotations.CsvIgnore;

import kieker.model.analysismodel.type.ComponentType;

/**
 *
 * @author Reiner Jung
 * @since 2.0.0
 *
 */
public class SimilarityEntry {

    @CsvCell(columnIndex = 1, columnName = "left-type")
    @CsvConverter(converter = ComponentTypeConverter.class)
    private ComponentType left;
    @CsvCell(columnIndex = 2, columnName = "right-type")
    @CsvConverter(converter = ComponentTypeConverter.class)
    private ComponentType right;
    @CsvCell(columnIndex = 3, columnName = "similarity")
    private double similarity;
    @CsvIgnore
    private transient int equalNamesCount;

    public SimilarityEntry() {
        // csveed API
    }

    public SimilarityEntry(final ComponentType left, final ComponentType right, final double similarity) {
        this.left = left;
        this.right = right;
        this.similarity = similarity;
        this.equalNamesCount = 0;
    }

    public ComponentType getLeft() {
        return this.left;
    }

    public void setLeft(final ComponentType left) {
        this.left = left;
    }

    public ComponentType getRight() {
        return this.right;
    }

    public void setRight(final ComponentType right) {
        this.right = right;
    }

    public double getSimilarity() {
        return this.similarity;
    }

    public void setSimilarity(final double similarity) {
        this.similarity = similarity;
    }

    public int getEqualNamesCount() {
        return this.equalNamesCount;
    }

    public void incrementEqualNamesCount() {
        this.equalNamesCount++;
    }
}