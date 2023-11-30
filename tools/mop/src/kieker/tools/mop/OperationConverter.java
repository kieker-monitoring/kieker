/***************************************************************************
 * Copyright (C) 2023 Kieker Project (https://kieker-monitoring.net)
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
package kieker.tools.mop;

import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.ParameterException;

/**
 *
 * @author Reiner Jung
 * @since 2.0.0
 *
 */
public class OperationConverter implements IStringConverter<EOperation> {

	@Override
	public EOperation convert(final String value) {
		for (final EOperation operation : EOperation.values()) {
			if (operation.name().equalsIgnoreCase(value.replace("-", "_"))) {
				return operation;
			}
		}
		throw new ParameterException(String.format("%s is not a valid model operation.", value)); // NOPMD
	}

}
