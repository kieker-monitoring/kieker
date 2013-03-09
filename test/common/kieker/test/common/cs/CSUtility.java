package kieker.test.common.cs;

import com.puppycrawl.tools.checkstyle.api.Check;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.FileContents;
import com.puppycrawl.tools.checkstyle.api.TextBlock;
import com.puppycrawl.tools.checkstyle.checks.javadoc.JavadocTag;
import com.puppycrawl.tools.checkstyle.checks.javadoc.JavadocTags;
import com.puppycrawl.tools.checkstyle.checks.javadoc.JavadocUtils;
import com.puppycrawl.tools.checkstyle.checks.javadoc.JavadocUtils.JavadocTagType;

/**
 * This is a simple utility class providing some methods which can be useful for other checkstyle checks as well.
 * 
 * @author Nils Christian Ehmke
 * 
 * @since 1.7
 */
public class CSUtility {

	private final static String SINCE_TAG_NAME = "since";

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
	public static boolean sinceTagAvailable(final Check check, final DetailAST ast) {
		try {
			// Get the corresponding javadoc block
			final FileContents contents = check.getFileContents();
			final TextBlock cmt = contents.getJavadocBefore(ast.getFirstChild().getLineNo());

			// Now extract the tags
			final JavadocTags tags = JavadocUtils.getJavadocTags(cmt, JavadocTagType.ALL);

			// Run through the tags and find the potential since tag
			for (final JavadocTag tag : tags.getValidTags()) {
				if (SINCE_TAG_NAME.equals(tag.getTagName())) {
					return true;
				}
			}
		} catch (final NullPointerException ex) {
			// Something is not available
		}

		return false;
	}
}
