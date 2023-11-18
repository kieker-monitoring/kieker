/***************************************************************************
 * Copyright (C) 2023 Kieker Project (http://kieker-monitoring.net)
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
package kieker.tools.restructuring.stages;

import org.csveed.annotations.CsvCell;

public class ModelEditDistanceEntry {

	@CsvCell(columnIndex = 1, columnName = "original-model")
	private String originalModelName;
	@CsvCell(columnIndex = 2, columnName = "goal-model")
	private String goalModelName;
	@CsvCell(columnIndex = 3, columnName = "number-of-steps")
	private int numberOfSteps;

	public ModelEditDistanceEntry(final String originalModelName, final String goalModelName, final int numberOfSteps) {
		this.originalModelName = originalModelName;
		this.goalModelName = goalModelName;
		this.numberOfSteps = numberOfSteps;
	}

	public String getOriginalModelName() {
		return this.originalModelName;
	}

	public void setOriginalModelName(final String originalModelName) {
		this.originalModelName = originalModelName;
	}

	public String getGoalModelName() {
		return this.goalModelName;
	}

	public void setGoalModelName(final String goalModelName) {
		this.goalModelName = goalModelName;
	}

	public int getNumberOfSteps() {
		return this.numberOfSteps;
	}

	public void setNumberOfSteps(final int numberOfSteps) {
		this.numberOfSteps = numberOfSteps;
	}

	@Override
	public boolean equals(final Object object) {
		if (object instanceof ModelEditDistanceEntry) {
			final ModelEditDistanceEntry other = (ModelEditDistanceEntry) object;
			return this.checkString(this.goalModelName, other.getGoalModelName())
					&& this.checkString(this.originalModelName, other.getOriginalModelName())
					&& (this.numberOfSteps == other.getNumberOfSteps());
		} else {
			return false;
		}
	}

	private boolean checkString(final String left, final String right) {
		if ((left == null) && (right == null)) {
			return true;
		} else if ((left != null) && (right != null)) {
			return left.equals(right);
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return this.goalModelName.hashCode() ^ this.originalModelName.hashCode() ^ Integer.hashCode(this.numberOfSteps);
	}

}
