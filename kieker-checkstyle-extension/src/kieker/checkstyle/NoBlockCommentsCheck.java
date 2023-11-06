/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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

import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Set;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TextBlock;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

/**
 * This class extends checkstyle with a new check which makes sure that no block
 * comments (expect for javadoc and comments containing {@code (non-javadoc)})
 * are used within the source files. The check detects all files with a package
 * declaration.
 *
 * @author Nils Christian Ehmke
 *
 * @since 1.7
 */
public class NoBlockCommentsCheck extends AbstractCheck {

	/**
	 * Creates a new instance of this class.
	 */
	public NoBlockCommentsCheck() {
		// Nothing to do here
		super();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.puppycrawl.tools.checkstyle.api.Check#getDefaultTokens()
	 */
	@Override
	public int[] getDefaultTokens() {
		return new int[] { TokenTypes.PACKAGE_DEF };
	}

	@Override
	public void visitToken(final DetailAST ast) {
		final Set<Entry<Integer, List<TextBlock>>> comments = this.getFileContents().getBlockComments().entrySet();

		for (final Entry<Integer, List<TextBlock>> comment : comments) {
			if (!NoBlockCommentsCheck.isJavadocComment(comment.getValue())
					&& !NoBlockCommentsCheck.isSeeJavaDoc(comment.getValue())) {
				this.log(comment.getKey(), "block comments are not allowed");
			}
		}
	}

	private static boolean isJavadocComment(final List<TextBlock> comment) {
		return comment.get(0).getText()[0].startsWith("/**");
	}

	private static boolean isSeeJavaDoc(final List<TextBlock> comment) {
		// Check whether there is a line containing (non-Javadoc) in the comment
		for (final TextBlock block : comment) {
			for (final String line : block.getText()) {
				if (line.toLowerCase(Locale.ENGLISH).contains("(non-javadoc)")) {
					return true;
				}
			}
		}

		return false;
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
