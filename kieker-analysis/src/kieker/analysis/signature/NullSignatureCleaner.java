/***************************************************************************
 * Copyright 2021 Kieker Project (http://kieker-monitoring.net)
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
package kieker.analysis.signature;

/**
 * Do nothing cleaner. However, supports conversion to lower case.
 *
 * @author Reiner Jung
 * @since 1.15
 */
public class NullSignatureCleaner extends AbstractSignatureCleaner {

	public NullSignatureCleaner(final boolean caseInsensitive) {
		super(caseInsensitive);
	}

	@Override
	public String processComponentSignature(final String signature) {
		return this.convertToLowerCase(signature);
	}

}
