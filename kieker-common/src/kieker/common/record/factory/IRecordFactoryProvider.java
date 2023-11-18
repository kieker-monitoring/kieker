/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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

package kieker.common.record.factory;

/**
 * Interface for record factory providers which allow to implement a particular strategy for selecting
 * the appropriate record factory for a given record type.
 *
 * @author Holger Knoche
 * @since 2.0
 */
public interface IRecordFactoryProvider {

	/**
	 * Denotes whether this factory provider is applicable to the given record class.
	 *
	 * @param recordClass
	 *            The record class in question
	 * @return {@code True} when this provider is applicable for the given class, {@code false} otherwise
	 */
	boolean isApplicableTo(Class<?> recordClass);

	/**
	 * Creates a factory for the given record class. This method may only be called if {@link #isApplicableTo(Class)} returns
	 * {@code true} for the respective record class.
	 *
	 * @param recordClass
	 *            The record class to create a factory for
	 * @return The created factory instance
	 */
	IRecordFactory<?> createFactoryFor(Class<?> recordClass);

}
