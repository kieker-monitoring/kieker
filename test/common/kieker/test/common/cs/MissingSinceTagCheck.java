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
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

/**
 * This is an additional checkstyle check which makes sure that classes, interfaces, annotations and methods within interfaces have a since tag.
 * 
 * @author Nils Christian Ehmke
 * 
 * @since 1.7
 * 
 * @see NotAllowedSinceTagCheck
 */
public class MissingSinceTagCheck extends Check {

	/**
	 * Creates a new instance of this class.
	 */
	public MissingSinceTagCheck() {
		// Nothing to do here
		super();
	}

	@Override
	public int[] getDefaultTokens() {
		// This here makes sure that we just get the correct components
		return new int[] { TokenTypes.CLASS_DEF, TokenTypes.INTERFACE_DEF, TokenTypes.ANNOTATION_DEF, TokenTypes.ENUM_DEF };
	}

	@Override
	public void visitToken(final DetailAST ast) {
		// Do not check private classes etc.
		if (CSUtility.isPrivate(ast)) {
			return;
		}

		// Check whether the since tag is there
		if (!CSUtility.sinceTagAvailable(this, ast)) {
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

}
