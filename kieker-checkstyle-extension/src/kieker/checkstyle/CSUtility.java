/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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

import java.util.ArrayList;
import java.util.Collection;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.FileContents;
import com.puppycrawl.tools.checkstyle.api.TextBlock;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import com.puppycrawl.tools.checkstyle.checks.javadoc.JavadocTag;
import com.puppycrawl.tools.checkstyle.checks.javadoc.JavadocTags;
import com.puppycrawl.tools.checkstyle.utils.JavadocUtils;
import com.puppycrawl.tools.checkstyle.utils.JavadocUtils.JavadocTagType;

/**
 * This is a simple utility class providing some methods which can be useful for other checkstyle checks as well.
 *
 * @author Nils Christian Ehmke
 *
 * @since 1.7
 */
public final class CSUtility {

	private static final String SINCE_TAG_NAME = "since";

	private CSUtility() {
		// private default constructor
	}

	/**
	 * This method checks whether the given component has a javadoc comment with a valid since tag.
	 *
	 * @param check
	 *            The current check.
	 * @param ast
	 *            The component to check for the tag.
	 *
	 * @return true if and only if there is a since tag in the javadoc comment of the given component.
	 */
	public static boolean sinceTagAvailable(final AbstractCheck check, final DetailAST ast) {
		// Get the corresponding javadoc block
		final FileContents contents = check.getFileContents();
		final TextBlock cmt = contents.getJavadocBefore(ast.getFirstChild().getLineNo());

		// Make sure that there is a comment block available
		if (cmt != null) {
			// Now extract the tags
			final JavadocTags tags = JavadocUtils.getJavadocTags(cmt, JavadocTagType.ALL);

			// Run through the tags and find the potential since tag
			for (final JavadocTag tag : tags.getValidTags()) {
				if (SINCE_TAG_NAME.equals(tag.getTagName())) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * This method extracts all method definitions from the given class (or interface).
	 *
	 * @param ast
	 *            The class (or interface).
	 *
	 * @return A collection of available methods.
	 */
	public static Collection<DetailAST> getMethodsFromClass(final DetailAST ast) {
		final Collection<DetailAST> result = new ArrayList<DetailAST>();

		final DetailAST objBlock = ast.findFirstToken(TokenTypes.OBJBLOCK);

		DetailAST child = objBlock.getFirstChild();
		while (child != null) {
			if (child.getType() == TokenTypes.METHOD_DEF) {
				result.add(child);
			}
			child = child.getNextSibling();
		}

		return result;
	}

	/**
	 * Checks whether the "parent" (the containing element) of the method is an interface or not.
	 *
	 * @param ast
	 *            The method to check.
	 *
	 * @return true if and only if the method is contained in an interface.
	 */
	public static boolean parentIsInterface(final DetailAST ast) {
		return ast.getParent().getParent().getType() == TokenTypes.INTERFACE_DEF;
	}

	/**
	 * Checks whether the given class is marked as private or not.
	 *
	 * @param clazz
	 *            The class to check.
	 *
	 * @return true if and only if the class contains a private modifier.
	 */
	public static boolean isPrivate(final DetailAST clazz) {
		final DetailAST modifiers = clazz.findFirstToken(TokenTypes.MODIFIERS);

		return modifiers.findFirstToken(TokenTypes.LITERAL_PRIVATE) != null;
	}

	/**
	 * Checks whether the given class is marked as abstract or not.
	 *
	 * @param clazz
	 *            The class to check.
	 *
	 * @return true if and only if the class contains an abstract modifier.
	 */
	public static boolean isAbstract(final DetailAST clazz) {
		final DetailAST modifiers = clazz.findFirstToken(TokenTypes.MODIFIERS);

		return modifiers.findFirstToken(TokenTypes.ABSTRACT) != null;
	}
}
