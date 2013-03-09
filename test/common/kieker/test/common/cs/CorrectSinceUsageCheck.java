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

package kieker.test.common.cs;

import com.puppycrawl.tools.checkstyle.api.Check;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.FileContents;
import com.puppycrawl.tools.checkstyle.api.TextBlock;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import com.puppycrawl.tools.checkstyle.checks.javadoc.JavadocTag;
import com.puppycrawl.tools.checkstyle.checks.javadoc.JavadocTags;
import com.puppycrawl.tools.checkstyle.checks.javadoc.JavadocUtils;
import com.puppycrawl.tools.checkstyle.checks.javadoc.JavadocUtils.JavadocTagType;

/**
 * This is an additional checkstyle check which makes sure that the since tag is used in a correct way. This means more precisely that a since tags is used for a
 * element if and only if the element is a class, an interface, an annotation or a method within an interface. All other appearances (or not appearances) will be
 * reported.<br>
 * </br>
 * 
 * The current version of this check checks only whether the since tags are there or not. It doesn't check the other direction.
 * 
 * @author Nils Christian Ehmke
 * 
 * @since 1.7
 */
// TODO Check the other direction as well
public class CorrectSinceUsageCheck extends Check {

	/**
	 * Creates a new instance of this class.
	 */
	public CorrectSinceUsageCheck() {
		// Nothing to do here
		super();
	}

	@Override
	public int[] getDefaultTokens() {
		// This here makes sure that we just get the correct components
		return new int[] { TokenTypes.CLASS_DEF, TokenTypes.INTERFACE_DEF, TokenTypes.ANNOTATION_DEF };
	}

	@Override
	public void visitToken(final DetailAST ast) {
		// Check whether the since tag is there
		if (!this.sinceTagAvailable(ast)) {
			this.log(ast.getLineNo(), "@since tag missing");
		}

		// If we have an interface, the methods have to get the tag as well
		if (ast.getType() == TokenTypes.INTERFACE_DEF) {
			final DetailAST objBlock = ast.findFirstToken(TokenTypes.OBJBLOCK);
			DetailAST child = objBlock.getFirstChild();
			// Run through all children
			while (child != null) {
				if (child.getType() == TokenTypes.METHOD_DEF) {
					this.visitToken(child);
				}
				child = child.getNextSibling();
			}
		}
	}

	/**
	 * Checks whether the given component has a since tag in its javadoc comment.
	 * 
	 * @param ast
	 *            The component to check for the tag.
	 * 
	 * @return true if and only if there is a since tag in the javadoc comment of the given component.
	 */
	private boolean sinceTagAvailable(final DetailAST ast) {
		final FileContents contents = this.getFileContents();
		final TextBlock cmt = contents.getJavadocBefore(ast.getFirstChild().getLineNo());

		final JavadocTags tags = JavadocUtils.getJavadocTags(cmt, JavadocTagType.ALL);

		for (final JavadocTag tag : tags.getValidTags()) {
			if ("since".equals(tag.getTagName())) {
				return true;
			}
		}

		return false;
	}

}
