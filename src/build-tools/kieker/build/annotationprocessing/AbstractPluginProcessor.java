/***************************************************************************
 * Copyright 2012 Kieker Project (http://kieker-monitoring.net)
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

package kieker.build.annotationprocessing;

import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.SimpleTypeVisitor6;
import javax.tools.Diagnostic.Kind;

/**
 * Abstract superclass for both plugin and repository annotation processors.
 * 
 * @author Holger Knoche
 * 
 */
public abstract class AbstractPluginProcessor extends AbstractProcessor {

	private static final String MISSING_MANDATORY_CONSTRUCTOR_MESSAGE_TEMPLATE = "A %s must define a constructor that only takes a configuration as input";

	private static final String EXPECTED_CONSTRUCTOR_PARAMETER_TYPE = "kieker.common.configuration.Configuration";

	@Override
	public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv) {
		for (final TypeElement annotation : annotations) {
			for (final Element element : roundEnv.getElementsAnnotatedWith(annotation)) {
				this.handleElement(element, roundEnv);
			}
		}

		return true;
	}

	/**
	 * Template method to handle a given element in the given round environment.
	 * 
	 * @param element
	 *            The element to process
	 * @param roundEnvironment
	 *            The current round environment
	 */
	protected abstract void handleElement(Element element, RoundEnvironment roundEnvironment);

	/**
	 * Checks whether the current element has a constructor with a single configuration parameter.
	 * 
	 * @param element
	 *            The element to check for a mandatory constructor
	 * @param typeName
	 *            The type name (plugin or repository) to report in the error message
	 * @return {@code True} if a matching constructor exists, {@code false} otherwise
	 */
	protected boolean mandatoryConstructorExists(final Element element, final String typeName) {
		for (final Element enclosedElement : element.getEnclosedElements()) {
			// Skip non-constructors
			if (enclosedElement.getKind() != ElementKind.CONSTRUCTOR) {
				continue;
			}

			// Skip non-public constructors
			if (!enclosedElement.getModifiers().contains(Modifier.PUBLIC)) {
				continue;
			}

			final ExecutableType type = (ExecutableType) enclosedElement.asType();
			if (this.checkConstructor(type)) {
				return true;
			}
		}

		this.processingEnv.getMessager().printMessage(Kind.ERROR, String.format(MISSING_MANDATORY_CONSTRUCTOR_MESSAGE_TEMPLATE, typeName), element);
		return false;
	}

	private boolean checkConstructor(final ExecutableType constructor) {
		final List<? extends TypeMirror> parameters = constructor.getParameterTypes();
		if (parameters.size() != 1) {
			return false;
		}

		final TypeMirror parameterMirror = parameters.get(0);
		return parameterMirror.accept(new CheckConfigurationParameterVisitor(), this.processingEnv);
	}

	private static class CheckConfigurationParameterVisitor extends SimpleTypeVisitor6<Boolean, ProcessingEnvironment> {

		@Override
		public Boolean visitDeclared(final DeclaredType declaredType, final ProcessingEnvironment environment) {
			final Element typeElement = declaredType.asElement();

			// Assemble the fully qualified class name
			final Name packageName = environment.getElementUtils().getPackageOf(typeElement).getQualifiedName();
			final String qualifiedName = packageName.toString() + "." + typeElement.getSimpleName();

			return EXPECTED_CONSTRUCTOR_PARAMETER_TYPE.equals(qualifiedName);
		}

	}

}
