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

package kieker.analysis.util;

import org.junit.Assert;

/**
 * @author Christian Wulf
 *
 * @since 1.10
 */
public final class AssertHelper {

	private AssertHelper() {
		// utility class
	}

	/**
	 * Assert that the actual instance is really from the specified expected class.
	 *
	 * @param <T>
	 *            java type
	 *
	 * @param expectedClazz
	 *            class type to expect
	 * @param actualInstance
	 *            instance to be checked
	 * @return true when the type matches
	 */
	@SuppressWarnings("unchecked")
	public static <T> T assertInstanceOf(final Class<T> expectedClazz, final Object actualInstance) {
		Assert.assertEquals(expectedClazz, actualInstance.getClass());
		return (T) actualInstance;
	}
}
