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

package kieker.checkstyle;

import com.puppycrawl.tools.checkstyle.api.Check;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.repository.annotation.Repository;
import kieker.common.configuration.Configuration;

/**
 * This is an additional checkstyle check which makes sure that all analysis components supply the default constructor (using a {@link Configuration} and an
 * {@link IProjectContext} object) we need for the framework.<br>
 * </br>
 *
 * Keep in mind that the check is not perfect, as checkstyle has some limitations. The main drawback is that we cannot check types. We can therefore not recognize
 * whether a class inherits directly or indirectly from {@link kieker.analysis.analysisComponent.AbstractAnalysisComponent}. Instead we use the annotations
 * {@link Plugin} and {@link Repository} to check whether a class is an analysis component or not. This can lead to false positives. Furthermore we cannot check the
 * types of the parameters for the constructors either. We use the names of the types to check this. This can lead to false positives as well.<br>
 * </br>
 *
 * The check provides a property to ignore abstract classes.
 *
 * @author Nils Christian Ehmke
 *
 * @since 1.7
 */
public class AnalysisComponentConstructorCheck extends Check {

	private static final String REPOSITORY_ANNOTATION_NAME = Repository.class.getSimpleName();
	private static final String PLUGIN_ANNOTATION_NAME = Plugin.class.getSimpleName();
	private static final String CONSTRUCTOR_SND_PARAMETER = IProjectContext.class.getSimpleName();
	private static final String CONSTRUCTOR_FST_PARAMETER = Configuration.class.getSimpleName();

	private boolean ignoreAbstractClasses;

	/**
	 * Creates a new instance of this class.
	 */
	public AnalysisComponentConstructorCheck() {
		// Nothing to do here
		super();
	}

	@Override
	public int[] getDefaultTokens() {
		// This here makes sure that we just get classes
		return new int[] { TokenTypes.CLASS_DEF };
	}

	@Override
	public void visitToken(final DetailAST ast) {
		// Check whether we are interested in the class (whether it is an analysis component or not)
		if (!(this.ignoreAbstractClasses && CSUtility.isAbstract(ast)) && AnalysisComponentConstructorCheck.isAnalysisComponent(ast)) {
			// Now check the constructors
			DetailAST child = ast.findFirstToken(TokenTypes.OBJBLOCK).findFirstToken(TokenTypes.CTOR_DEF);
			while (child != null) {
				if ((child.getType() == TokenTypes.CTOR_DEF) && AnalysisComponentConstructorCheck.isValidConstructor(child)) {
					// We found a valid constructor
					return;
				}

				child = child.getNextSibling();
			}

			// Seems like there is no valid constructor
			this.log(ast.getLineNo(), "invalid analysis component constructor");
		}
	}

	/**
	 * Checks whether the given constructor seems to be a valid one or not.
	 *
	 * @param constructor
	 *            The constructor in question.
	 *
	 * @return true if and only if the constructor seems to be valid.
	 */
	private static boolean isValidConstructor(final DetailAST constructor) {
		// Find the available parameters of the constructor
		final DetailAST parameters = constructor.findFirstToken(TokenTypes.PARAMETERS);

		// Make sure that there are exactly TWO parameters (two parameters plus a comma in the middle)
		if (parameters.getChildCount() == 3) {
			final DetailAST fstParType = parameters.getFirstChild().findFirstToken(TokenTypes.TYPE);
			final DetailAST sndParType = parameters.getLastChild().findFirstToken(TokenTypes.TYPE);

			final DetailAST fstParIdent = fstParType.findFirstToken(TokenTypes.IDENT);
			final DetailAST sndParIdent = sndParType.findFirstToken(TokenTypes.IDENT);

			return fstParIdent.getText().equals(CONSTRUCTOR_FST_PARAMETER) && sndParIdent.getText().equals(CONSTRUCTOR_SND_PARAMETER);
		}

		return false;
	}

	/**
	 * This method finds out whether the given class is an analysis component or not (by searching for annotations with the names "Plugin" or "Repository").
	 *
	 * @param clazz
	 *            The class in question.
	 *
	 * @return true if and only if the class is an analysis component.
	 */
	private static boolean isAnalysisComponent(final DetailAST clazz) {
		// Get the list of modifiers as the annotations are within those
		final DetailAST modifiers = clazz.findFirstToken(TokenTypes.MODIFIERS);

		DetailAST modifier = modifiers.getFirstChild();

		// Run through the modifiers
		while (modifier != null) {
			// If we find an annotation we check the name
			if ((modifier.getType() == TokenTypes.ANNOTATION) && (modifier.findFirstToken(TokenTypes.IDENT) != null)) {
				final String annotationName = modifier.findFirstToken(TokenTypes.IDENT).getText();
				if (annotationName.equals(PLUGIN_ANNOTATION_NAME) || annotationName.equals(REPOSITORY_ANNOTATION_NAME)) {
					// We found an annotation with the correct name
					return true;
				}
			}

			modifier = modifier.getNextSibling();
		}

		// Seems like there is no such annotation
		return false;
	}

	/**
	 * Setter for the property {@link #ignoreAbstractClasses}.
	 *
	 * @param ignoreAbstractClasses
	 *            The new value of the property.
	 */
	public void setIgnoreAbstractClasses(final boolean ignoreAbstractClasses) {
		this.ignoreAbstractClasses = ignoreAbstractClasses;
	}
}
