/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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
import com.puppycrawl.tools.checkstyle.utils.AnnotationUtil;

/**
 * This is an additional checkstyle check which makes sure that JUnit tests do
 * not use the {@code @ignore} annotation.
 *
 * @author Nils Christian Ehmke
 *
 * @since 1.10
 */
public class NotAllowedIgnoreAnnotationCheck extends AbstractCheck {

	/**
	 * Creates a new instance of this class.
	 */
	public NotAllowedIgnoreAnnotationCheck() {
		// Nothing to do here
		super();
	}

	@Override
	public int[] getDefaultTokens() {
		// This here makes sure that we just get the correct components
		return new int[] { TokenTypes.METHOD_DEF };
	}

	@Override
	public void visitToken(final DetailAST ast) {
		if (this.hasTestAnnotation(ast) && this.hasIgnoreAnnotation(ast)) {
			this.log(ast.getLineNo(), "@ignore annotation not allowed");
		}
	}

	private boolean hasTestAnnotation(final DetailAST ast) {
		return AnnotationUtil.containsAnnotation(ast, "Test");
	}

	private boolean hasIgnoreAnnotation(final DetailAST ast) {
		return AnnotationUtil.containsAnnotation(ast, "Ignore");
	}

	@Override
	public int[] getAcceptableTokens() {
		return this.getDefaultTokens();
	}

	@Override
	public int[] getRequiredTokens() {
		return this.getDefaultTokens();
	}
}
