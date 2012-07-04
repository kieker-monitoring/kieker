/***************************************************************************
 * Copyright 2012 by
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

package kieker.examples.userguide.cxfrestful;

import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;

public final class BookstoreServer {	

    protected BookstoreServer() throws Exception {
        JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();
        sf.setResourceClasses(Bookstore.class);
        sf.setResourceProvider(Bookstore.class, 
            new SingletonResourceProvider(new Bookstore()));
        sf.setAddress("http://localhost:9090/");
        sf.getInInterceptors().add(new LoggingInInterceptor());
        //TODO add kieker interceptors here.

        sf.create();
    }

    public static void main(String args[]) throws Exception {
        new BookstoreServer();
        System.out.println("Server ready...");

        Thread.sleep(1 * 60 * 1000);
        System.out.println("Server exiting");
        System.exit(0);
    }
}
