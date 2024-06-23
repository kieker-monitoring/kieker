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
package kieker.analysis.behavior.acceptance.matcher;

import java.util.Locale;

import org.junit.Assert;
import org.junit.Test;

import com.beust.jcommander.ParameterException;

public class AcceptanceModeConverterTest { // NOCS no constructor

	private static final String INVALID = "invalid";

	@Test
	public void testValid() {
		final AcceptanceModeConverter converter = new AcceptanceModeConverter();
		for (final EAcceptanceMode mode : EAcceptanceMode.values()) {
			final EAcceptanceMode foundMode = converter.convert(mode.name().toLowerCase(Locale.getDefault()));
			Assert.assertEquals(mode, foundMode);
		}
	}

	@Test
	public void testInvalid() {
		final AcceptanceModeConverter converter = new AcceptanceModeConverter();
		try {
			final EAcceptanceMode mode = converter.convert(AcceptanceModeConverterTest.INVALID);
			Assert.fail("Converter should throw exception, but produces mode " + mode.name());
		} catch (final ParameterException e) {
			// throwing an exception is the expected behavior
		}
	}

}
