/***************************************************************************
 * Copyright 2011 by
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

package main;

/**
 * This class supplies the possibility to save two (different) objects in one
 * object.
 * 
 * @author Nils Christian Ehmke
 * 
 * @version 1.0 The first implementation of the class.
 */
public class Pair<FST, SND> {

	/**
	 * The field for the first object.
	 */
	public FST first;
	/**
	 * The field for the second object.
	 */
	public SND second;

	/**
	 * Creates a new instance of the class <code>Pair</code> with the given
	 * parameters.
	 * 
	 * @param first
	 *            The first object.
	 * @param second
	 *            The second object.
	 * @since 1.0
	 */
	public Pair(FST first, SND second) {
		this.first = first;
		this.second = second;
	}
}
