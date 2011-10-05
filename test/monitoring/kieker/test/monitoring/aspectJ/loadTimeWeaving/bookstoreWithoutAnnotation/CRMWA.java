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

package kieker.test.monitoring.aspectJ.loadTimeWeaving.bookstoreWithoutAnnotation;

/**
 * A simple test and demonstration scenario for Kieker's
 * monitoring component.
 * 
 * @author Matthias Rohr
 *         History:
 *         2008/08/30: Created based on CRM.java without Annotations
 * 
 */

public class CRMWA {

	public static void getOffers() {
		CatalogWA.getBook(true);
	}
}
