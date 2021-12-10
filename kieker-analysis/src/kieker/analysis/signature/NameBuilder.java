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
 * This is a wrapper class around a {@link IOperationNameBuilder} and a {@link IComponentNameBuilder}.
 *
 * @author Sören Henning
 *
 * @since 1.14
 *
 */
public final class NameBuilder {

	private final IOperationNameBuilder operationNameBuilder;
	private final IComponentNameBuilder componentNameBuilder;

	private NameBuilder(final IOperationNameBuilder operationNameBuilder, final IComponentNameBuilder componentNameBuilder) {
		this.operationNameBuilder = operationNameBuilder;
		this.componentNameBuilder = componentNameBuilder;
	}

	public IOperationNameBuilder getOperationNameBuilder() {
		return this.operationNameBuilder;
	}

	public IComponentNameBuilder getComponentNameBuilder() {
		return this.componentNameBuilder;
	}

	public static NameBuilder of(final IOperationNameBuilder operationNameBuilder, final IComponentNameBuilder componentNameBuilder) { // NOPMD
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
