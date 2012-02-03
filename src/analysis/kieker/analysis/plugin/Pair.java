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

package kieker.analysis.plugin;

/**
 * A helper class which can be used if for example a method should return two elements.
 * 
 * @author Nils Christian Ehmke
 * 
 * @param <T1>
 *            The type of the first element.
 * @param <T2>
 *            The type of the second element.
 */
public class Pair<T1, T2> {
	private T1 fst;
	private T2 snd;

	/**
	 * Creates an instance of this class whereas both elements of this container are null.
	 */
	public Pair() {
		this.fst = null;
		this.snd = null;
	}

	/**
	 * Creates an instance of this class using the given parameters.
	 * 
	 * @param fst
	 *            The content for the first element.
	 * @param snd
	 *            The content for the second element.
	 */
	public Pair(final T1 fst, final T2 snd) {
		this.fst = fst;
		this.snd = snd;
	}

	public T1 getFst() {
		return this.fst;
	}

	public T2 getSnd() {
		return this.snd;
	}

	public void setFst(final T1 fst) {
		this.fst = fst;
	}

	public void setSnd(final T2 snd) {
		this.snd = snd;
	}

}
