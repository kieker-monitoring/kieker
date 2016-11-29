/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysisteetime.modeltests.hardcoded;

/**
 * @author Sören Henning
 *
 * @since 1.13
 */
public class ModelTester {

	public ModelTester() {}

	public static void main(final String[] args) {

		final ComponentType componentType = new ComponentType();
		componentType.setSignature("Signature");

		if (componentType.containsOperationType("public void foo()")) {
			final OperationType operationType1 = new OperationType();
			operationType1.setSignature("public void foo()");
			operationType1.setComponent(componentType);
			// TODO 2x String
		}

		if (componentType.containsOperationType("public void bar()")) {
			final OperationType operationType2 = new OperationType();
			operationType2.setSignature("public void bar()");
			componentType.addOperationType(operationType2);
			// TODO 2x String
		}

	}

}
