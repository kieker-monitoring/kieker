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
package kieker.analysis.code;

import kieker.model.analysismodel.execution.EDirection;

/**
 * @author Reiner Jung
 * @since 2.0.0
 */
public final class CodeUtils {

	public static final String UNKNOWN_OPERATION = "++unknown-operation++";
	public static final String UNKNOWN_COMPONENT = "++unknown-component++";
	public static final String NO_FILE = "++no-file++";
	public static final String NO_PACKAGE = "++no-package++";

	private CodeUtils() {
		// utility class
	}

	// duplicate from AggregateDataflowStage
	/**
	 * Merge two direction values.
	 *
	 * @param left
	 *            left operand
	 * @param right
	 *            right operand
	 * @return returns the merged value
	 */
	public static EDirection merge(final EDirection left, final EDirection right) {
		switch (left) {
		case READ:
			switch (right) {
			case READ:
				return EDirection.READ;
			case WRITE:
			case BOTH:
				return EDirection.BOTH;
			case NONE:
			default:
				return left;
			}
		case WRITE:
			switch (right) {
			case READ:
			case BOTH:
				return EDirection.BOTH;
			case WRITE:
				return EDirection.WRITE;
			case NONE:
			default:
				return left;
			}
		case NONE:
			return right;
		case BOTH:
		default:
			return EDirection.BOTH;
		}
	}
}
