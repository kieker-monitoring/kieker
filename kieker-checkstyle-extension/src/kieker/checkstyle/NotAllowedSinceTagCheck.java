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

package kieker.checkstyle;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

/**
 * This is an additional checkstyle check which makes sure that <b>only</b>
 * classes, interfaces, annotations, enums and methods within interfaces have a
 * since tag.
 *
 * @author Nils Christian Ehmke
 *
 * @since 1.7
 *
 * @see MissingSinceTagCheck
 */
public class NotAllowedSinceTagCheck extends AbstractCheck {

	/**
	 * Creates a new instance of this class.
	 */
	public NotAllowedSinceTagCheck() {
		// Nothing to do here
		super();
	}

	@Override
	public int[] getDefaultTokens() {
		// This here makes sure that we just get the correct components
		return new int[] { TokenTypes.METHOD_DEF, TokenTypes.VARIABLE_DEF };
	}

	@Override
	public void visitToken(final DetailAST ast) {
		if (CSUtility.sinceTagAvailable(this, ast)
				&& ((ast.getType() == TokenTypes.VARIABLE_DEF) || !(CSUtility.parentIsInterface(ast)))) {
			this.log(ast.getLineNo(), "@since tag not allowed");
		}
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
