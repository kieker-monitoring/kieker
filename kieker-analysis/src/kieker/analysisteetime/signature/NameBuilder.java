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
package kieker.analysisteetime.signature;

/**
 * This is a wrapper class around a {@link OperationNameBuilder} and a {@link ComponentNameBuilder}.
 *
 * @author Sören Henning
 *
 * @since 1.13
 *
 */
public final class NameBuilder {

	private final OperationNameBuilder operationNameBuilder;
	private final ComponentNameBuilder componentNameBuilder;

	private NameBuilder(final OperationNameBuilder operationNameBuilder, final ComponentNameBuilder componentNameBuilder) {
		this.operationNameBuilder = operationNameBuilder;
		this.componentNameBuilder = componentNameBuilder;
	}

	public OperationNameBuilder getOperationNameBuilder() {
		return this.operationNameBuilder;
	}

	public ComponentNameBuilder getComponentNameBuilder() {
		return this.componentNameBuilder;
	}

	public static NameBuilder of(final OperationNameBuilder operationNameBuilder, final ComponentNameBuilder componentNameBuilder) {
		return new NameBuilder(operationNameBuilder, componentNameBuilder);
	}

	public static NameBuilder forJavaShort() {
		return new NameBuilder(new JavaShortOperationNameBuilder(), new JavaShortComponentNameBuilder());
	}

	public static NameBuilder forJavaShortOperations() {
		return new NameBuilder(new JavaShortOperationNameBuilder(), new JavaFullComponentNameBuilder());
	}

	public static NameBuilder forJavaFull() {
		return new NameBuilder(new JavaFullOperationNameBuilder(), new JavaFullComponentNameBuilder());
	}

}
