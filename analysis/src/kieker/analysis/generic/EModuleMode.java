/***************************************************************************
 * Copyright (C) 2022 OceanDSL (https://oceandsl.uni-kiel.de)
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
package kieker.analysis.generic;

import java.util.Arrays;

/**
 *
 * @author Reiner Jung
 * @since 1.3.0
 *
 */
public enum EModuleMode {
	MAP_MODE, MODULE_MODE, JAVA_CLASS_MODE, JAVA_CLASS_LONG_MODE, PYTHON_CLASS_MODE, FILE_MODE;

	public static String getModesInfo() {
		return Arrays.asList(EModuleMode.values()).stream().map(e -> e.name()).reduce(", ", String::concat);
	}
}
