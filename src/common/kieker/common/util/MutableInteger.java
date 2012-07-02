/***************************************************************************
 * Copyright 2012 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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

package kieker.common.util;

/**
 * This class provides a basic interface for mutable {@code int} values wrapped in objects.
 * 
 * @author Holger Knoche
 * 
 */

public class MutableInteger {

	private int value;

	/**
	 * Creates a mutable integer with value zero.
	 */
	public MutableInteger() {
		this.value = 0;
	}

	/**
	 * Creates a mutable integer with the given value.
	 * 
	 * @param value
	 *            The initial value of this mutable integer
	 */
	public MutableInteger(final int value) {
		this.value = value;
	}

	/**
	 * Returns the current value of this mutable integer.
	 * 
	 * @return See above
	 */
	public int getValue() {
		return this.value;
	}

	/**
	 * Sets the current value of this mutable integer.
	 * 
	 * @param value
	 *            The value to set
	 */
	public void setValue(final int value) {
		this.value = value;
	}

	/**
	 * Increases the value of this mutable integer by one.
	 */
	public void increase() {
		this.value++;
	}

	/**
	 * Increases the value of this mutable integer by the given amount.
	 * 
	 * @param amount
	 *            The amount to add
	 */
	public void increase(final int amount) {
		this.value += amount;
	}

	/**
	 * Decreases the value of this mutable integer by one.
	 */
	public void decrease() {
		this.value--;
	}

	/**
	 * Decreases the value of this mutable integer by the given amount.
	 * 
	 * @param amount
	 *            The amount to subtract
	 */
	public void decrease(final int amount) {
		this.value -= amount;
	};

}
