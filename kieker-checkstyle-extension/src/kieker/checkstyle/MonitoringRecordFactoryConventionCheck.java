/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

/**
 * This class extends checkstyle with a new check which makes sure that classes,
 * which implement the {@link kieker.common.record.IMonitoringRecord.Factory}
 * interface, supply the necessary static field (for the types) and the
 * constructor (working with an array of {@link Object}) for the framework.<br>
 * </br>
 *
 * Keep in mind that the check is not perfect, as checkstyle has some
 * limitations. There can therefore be some false positives.<br>
 * </br>
 *
 * The check provides a property to ignore abstract classes.
 *
 * @author Nils Christian Ehmke
 *
 * @since 1.7
 */
public class MonitoringRecordFactoryConventionCheck extends AbstractCheck {

	/**
	 * This field contains the (constant) tree to detect the type (this is necessary
	 * as this is a little bit more nested).
	 */
	private static final DetailAST TYPE_AST;

	private static final String FACTORY_FST_NAME = "IMonitoringRecord";
	private static final String FACTORY_SND_NAME = "Factory";
	private static final String FIELD_TYPE_NAME = Class.class.getSimpleName();
	private static final String FIELD_NAME = "TYPES";
	private static final String CONSTRUCTOR_PARAMETER = Object.class.getSimpleName();

	private boolean ignoreAbstractClasses;

	static {
		// Assemble the type tree
		TYPE_AST = new DetailAST();
		TYPE_AST.initialize(TokenTypes.TYPE, "");

		final DetailAST arrDecl = new DetailAST();
		arrDecl.initialize(TokenTypes.ARRAY_DECLARATOR, "");
		TYPE_AST.addChild(arrDecl);

		final DetailAST ident = new DetailAST();
		arrDecl.addChild(ident);
		ident.initialize(TokenTypes.IDENT, FIELD_TYPE_NAME);

		final DetailAST typeArgs = new DetailAST();
		arrDecl.addChild(typeArgs);
		typeArgs.initialize(TokenTypes.TYPE_ARGUMENTS, "");

		final DetailAST genStart = new DetailAST();
		typeArgs.addChild(genStart);
		genStart.initialize(TokenTypes.GENERIC_START, "");

		final DetailAST typeArg = new DetailAST();
		typeArgs.addChild(typeArg);
		typeArg.initialize(TokenTypes.TYPE_ARGUMENT, "");

		final DetailAST wildcard = new DetailAST();
		typeArg.addChild(wildcard);
		wildcard.initialize(TokenTypes.WILDCARD_TYPE, "");

		final DetailAST genEnd = new DetailAST();
		typeArgs.addChild(genEnd);
		genEnd.initialize(TokenTypes.GENERIC_END, "");

		final DetailAST rBrack = new DetailAST();
		arrDecl.addChild(rBrack);
		rBrack.initialize(TokenTypes.RBRACK, "");
	}

	/**
	 * Creates a new instance of this class.
	 */
	public MonitoringRecordFactoryConventionCheck() {
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
		// Check whether we are interested in the class (whether it is an analysis
		// component or not)
		if (!(this.ignoreAbstractClasses && CSUtility.isAbstract(ast))
				&& MonitoringRecordFactoryConventionCheck.implementsFactory(ast)) {
			this.checkConstructors(ast);
			this.checkFields(ast);
		}
	}

	/**
	 * This method checks the fields of the record.
	 *
	 * @param ast
	 *            The record class.
	 */
	private void checkFields(final DetailAST ast) {
		DetailAST child = ast.findFirstToken(TokenTypes.OBJBLOCK).findFirstToken(TokenTypes.VARIABLE_DEF);
		while (child != null) {
			if ((child.getType() == TokenTypes.VARIABLE_DEF)
					&& MonitoringRecordFactoryConventionCheck.isValidTypeField(child)) {
				// We found a valid field
				return;
			}

			child = child.getNextSibling();
		}

		// Seems like there is no valid field
		this.log(ast.getLineNo(), "invalid factory field");
	}

	/**
	 * This method checks the constructors of the record.
	 *
	 * @param ast
	 *            The record class.
	 */
	private void checkConstructors(final DetailAST ast) {
		DetailAST child = ast.findFirstToken(TokenTypes.OBJBLOCK).findFirstToken(TokenTypes.CTOR_DEF);
		while (child != null) {
			if ((child.getType() == TokenTypes.CTOR_DEF)
					&& MonitoringRecordFactoryConventionCheck.isValidConstructor(child)) {
				// We found a valid constructor
				return;
			}

			child = child.getNextSibling();
		}

		// Seems like there is no valid constructor
		this.log(ast.getLineNo(), "invalid factory constructor");
	}

	/**
	 * Checks whether the given field is valid or not.
	 *
	 * @param field
	 *            The field to check.
	 *
	 * @return true if and only if the field is a valid one.
	 */
	private static boolean isValidTypeField(final DetailAST field) {
		final String ident = field.findFirstToken(TokenTypes.IDENT).getText();

		// Is the name correct?
		if (FIELD_NAME.equals(ident)) {
			final DetailAST modifiers = field.findFirstToken(TokenTypes.MODIFIERS);

			// Check whether the field is static and final
			if (modifiers.branchContains(TokenTypes.LITERAL_STATIC) && modifiers.branchContains(TokenTypes.FINAL)) {
				return MonitoringRecordFactoryConventionCheck.treeCompare(field.findFirstToken(TokenTypes.TYPE),
						TYPE_AST);
			}
		}

		return false;
	}

	/**
	 * This method checks whether the given trees are more or less equal. This is
	 * necessary as the available methods do not work as expected for this special
	 * case.
	 *
	 * @param actual
	 *            The actual tree.
	 * @param expected
	 *            The expected tree.
	 *
	 * @return true if and only if the trees are (more or less) equal.
	 */
	private static boolean treeCompare(final DetailAST actual, final DetailAST expected) {
		if ((actual != null) && (expected != null) && (actual.getType() == expected.getType())) {
			// If they are identifiers, we check the texts
			if ((actual.getType() == TokenTypes.IDENT) && (!actual.getText().equals(expected.getText()))) {
				return false;
			}

			// Everything fine so far. Check the equality of the children
			DetailAST actChild = actual.getFirstChild();
			DetailAST expChild = expected.getFirstChild();

			while (actChild != null) {
				// The treeCompare method checks whether one of the children is null (we
				// recognize therefore if the parents have different number of children).
				if (!MonitoringRecordFactoryConventionCheck.treeCompare(actChild, expChild)) {
					return false;
				}

				actChild = actChild.getNextSibling();
				expChild = expChild.getNextSibling();
			}

			// Everything seems to be fine - just make sure that expChild is null as well or
			// otherwise the parents had a different number of children
			return expChild == null;
		}

		return false;
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

		// Make sure that there is exactly ONE parameter
		if (parameters.getChildCount() == 1) {
			final DetailAST fstParType = parameters.getFirstChild().findFirstToken(TokenTypes.TYPE);
			final DetailAST fstParArr = fstParType.findFirstToken(TokenTypes.ARRAY_DECLARATOR);
			if (fstParArr != null) {
				final DetailAST fstParIdent = fstParArr.findFirstToken(TokenTypes.IDENT);
				return fstParIdent.getText().equals(CONSTRUCTOR_PARAMETER);
			}
		}

		return false;
	}

	/**
	 * This method finds out whether the given class implements the record factory
	 * or not.
	 *
	 * @param clazz
	 *            The class in question.
	 *
	 * @return true if and only if the class implements the record factory.
	 */
	private static boolean implementsFactory(final DetailAST clazz) {
		final DetailAST implementsClause = clazz.findFirstToken(TokenTypes.IMPLEMENTS_CLAUSE);
		if (implementsClause != null) {
			DetailAST clause = implementsClause.getFirstChild();

			// Run through the whole implements clause
			while (clause != null) {
				// It has to be a combination of two names
				if ((clause.getType() == TokenTypes.DOT) && (clause.getChildCount() == 2)) {
					final String fstClauseIdent = clause.getFirstChild().getText();
					final String sndClauseIdent = clause.getLastChild().getText();

					return (FACTORY_FST_NAME.equals(fstClauseIdent)) && (FACTORY_SND_NAME.equals(sndClauseIdent));
				}

				clause = clause.getNextSibling();
			}
		}
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

	@Override
	public int[] getAcceptableTokens() {
		return getDefaultTokens();
	}

	@Override
	public int[] getRequiredTokens() {
		return getDefaultTokens();
	}
}
