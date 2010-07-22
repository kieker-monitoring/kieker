package kieker.test.monitoring.aspectJ.compileTimeWeaving.bookstore.synchron;
import kieker.monitoring.annotation.OperationExecutionMonitoringProbe;

/**
 * kieker.tests.compileTimeWeaving.bookstore.Catalog.java
 *
 * ==================LICENCE=========================
 * Copyright 2006-2008 Matthias Rohr and the Kieker Project
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
 * ==================================================
 *
 *
 * A simple test and demonstration scenario for Kieker's 
 * monitoring component tpmon. See the kieker tutorial 
 * for more information 
 * (http://www.matthias-rohr.com/kieker/tutorial.html)
 * 
 * THIS VARIANT IS nearly identical TO kieker.tests.compileTimeWeaving.bookstore.Catalog.
 * Method getBook is synchronized here. This allows to test the (negative) performance 
 * influence of synchronized method invocation.
 *
 * @author Matthias Rohr
 * History:
 * 2008/20/10: Created this variant based on kieker.tests.compileTimeWeaving.bookstore.Catalog
 * 2008/01/09: Refactoring for the first release of
 *             Kieker and publication under an open source licence
 * 2007-04-18: Initial version
 *
 */


public class Catalog {
    
    @OperationExecutionMonitoringProbe()
    public static synchronized void getBook(boolean complexQuery){
    	if (complexQuery) {
		//System.out.println("  complex query");
		Bookstore.waitabit(20);
	}
	else 	{			
		//System.out.println("  simple query"); 
		Bookstore.waitabit(2);	
	}

    }
   
}
