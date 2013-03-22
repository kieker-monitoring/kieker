/***************************************************************************
 * Copyright 2013 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.common.cs;

import com.puppycrawl.tools.checkstyle.api.Check;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.FullIdent;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

import kieker.test.common.junit.AbstractKiekerTest;

/**
 * This class extends checkstyle with a new check which makes sure that classes, which contain {@code junit} in their package name, extend the class
 * {@link AbstractKiekerTest}. It is assumed that the package name contains at least one dot.<br>
 * </br>
 * 
 * Keep in mind that the check is not perfect, as checkstyle has some limitations. There can therefore be some false positives and some false negatives.<br>
 * </br>
 * 
 * The check provides a property to ignore abstract classes.
 * 
 * @author Nils Christian Ehmke
 * 
 * @since 1.7
 */
public class JUnitTestsExtendingAbstractKiekerTestCheck extends Check {

	private static final String JUNIT_NAME = "junit";

	private static final String ABSTRACT_KIEKER_TEST_CLASS_NAME = AbstractKiekerTest.class.getSimpleName();

	private boolean ignoreAbstractClasses;

	/**
	 * Creates a new instance of this class.
	 */
	public JUnitTestsExtendingAbstractKiekerTestCheck() {
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
		// Check whether we are interested in the class (whether it is not abstract).
		if (this.ignoreAbstractClasses && CSUtility.isAbstract(ast)) {
			return;
		}
		// Now check whether the package contains junit and - if this is the case - whether the class extends the class in question.
		if (!this.isJUnitTest(ast) || this.extendsAbstractKiekerTest(ast)) {
			return;
		}

		this.log(ast.getLineNo(), "JUnit test has to extend AbstractKiekerTest");
	}

	/**
	 * This method checks whether the given class extends {@link AbstractKiekerTest}.
	 * 
	 * @param clazz
	 *            The class to check.
	 * 
	 * @return true if and only if the class extends {@link AbstractKiekerTest}.
	 */
	private boolean extendsAbstractKiekerTest(final DetailAST clazz) {
		// Get the extend clause if it exists...
		final DetailAST extClause = clazz.findFirstToken(TokenTypes.EXTENDS_CLAUSE);
		if (extClause == null) {
			return false;
		}
		// ... and check the name of the identifier if it exists
		final DetailAST ident = extClause.findFirstToken(TokenTypes.IDENT);
		if (ident == null) {
			return false;
		}
		final String classname = ident.getText();
		if (ABSTRACT_KIEKER_TEST_CLASS_NAME.equals(classname)) {
			return true;
		}

		// // Try to find the package definition. It has to be a sibling of the class definition node.
		// DetailAST packageDef = clazz;
		// while ((packageDef != null) && (packageDef.getType() != TokenTypes.PACKAGE_DEF)) {
		// packageDef = packageDef.getPreviousSibling();
		// }
		// // Check whether there is a package definition or not
		// if (packageDef == null) {
		// this.log(0, "No package def");
		// return false;
		// }
		// // Check the package name. We do assume that there is at least one dot in the package-name.
		// final DetailAST packageStart = packageDef.findFirstToken(TokenTypes.DOT);
		// final String packageName = FullIdent.createFullIdent(packageStart).getText();

		// this does not work, yet:

		// try {
		// final Class<?> extendedClass = Class.forName(packageName + '.' + classname);
		// this.log(0, extendedClass.toString());
		// if (extendedClass.isInstance(AbstractKiekerTest.class)) {
		// return true;
		// }
		// } catch (final ClassNotFoundException ignore) {
		// this.log(0, ignore.toString());
		// return false;
		// }

		return false;
	}

	/**
	 * This method checks whether the package name of the given class contains the name {@code JUnit}.
	 * If this is true it is further checked whether the class name contains {@code Test}
	 * 
	 * @param clazz
	 *            The class to check.
	 * 
	 * @return true if the class is a JUnit test
	 */
	private boolean isJUnitTest(final DetailAST clazz) {
		// Try to find the package definition. It has to be a sibling of the class definition node.
		DetailAST packageDef = clazz;
		while ((packageDef != null) && (packageDef.getType() != TokenTypes.PACKAGE_DEF)) {
			packageDef = packageDef.getPreviousSibling();
		}

		// Check whether there is a package definition or not
		if (packageDef == null) {
			return false;
		}

		// Check the package name. We do assume that there is at least one dot in the package-name.
		final DetailAST packageStart = packageDef.findFirstToken(TokenTypes.DOT);
		final String packageName = FullIdent.createFullIdent(packageStart).getText();

		if (!packageName.contains(JUNIT_NAME)) {
			return false;
		}
		return clazz.findFirstToken(TokenTypes.IDENT).getText().contains("Test");
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
