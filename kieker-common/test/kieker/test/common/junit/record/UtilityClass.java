/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.common.junit.record;

import kieker.common.record.flow.IOperationRecord;
import kieker.common.record.flow.trace.operation.CallOperationEvent;

/**
 * This class contains a set of common routines used throughout the test cases.
 *
 * @author Reiner Jung
 *
 * @since 1.10
 */
public final class UtilityClass {

	/**
	 * Utility class constructor.
	 */
	private UtilityClass() {

	}

	/**
	 * Checks if two events refer to the same operation of the same class.
	 *
	 * @param left
	 *            The first record.
	 * @param right
	 *            The second record
	 *
	 * @return true if and only if the given records refer to the same operation of the same class.
	 */
	public static boolean refersToSameOperationAs(final IOperationRecord left, final IOperationRecord right) {
		return left.getOperationSignature().equals(right.getOperationSignature())
				&& left.getClassSignature().equals(right.getClassSignature());
	}

	/**
	 * Check if a callee signature references another signature of another event.
	 * 
	 * @param left
	 *            The first record.
	 * @param right
	 *            The second record
	 *
	 * @return true if and only if the content of the first record references the content of the second record.
	 */
	public static boolean callsReferencedOperationOf(final CallOperationEvent left, final CallOperationEvent right) {
		return left.getCalleeOperationSignature().equals(right.getOperationSignature())
				&& left.getCalleeClassSignature().equals(right.getClassSignature());
	}
}
