/***************************************************************************
 * Copyright 2011 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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

package kieker.common.util;

/*
 * Copyright 2006-2011 Kieker Project
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * This class provides the method getVersion() which returns the version number
 * set during build (String replacement task within build.xml).
 * 
 * @author Andre van Hoorn
 */
public final class Version {
	/*
	 * The VERSION string is updated by the Ant build file, which looks for the
	 * pattern: VERSION = <quote>.*<quote>
	 */

	private static final String VERSION = "1.4-dev-SNAPSHOT-20111004"; // NOPMD
	private static final String COPYRIGHT = "Copyright (c) 2006-2011 Kieker Project";

	/**
	 * Returns the version String.
	 * 
	 * @return the version String.
	 */
	public final static String getVERSION() {
		return Version.VERSION;
	}

	/**
	 * Returns the version String.
	 * 
	 * @return the version String.
	 */
	public final static String getCOPYRIGHT() {
		return Version.COPYRIGHT;
	}

	private Version() {}
}
