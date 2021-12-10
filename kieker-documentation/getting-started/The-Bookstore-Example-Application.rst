.. _gt-the-bookstore-example-application:

The Bookstore Example Application 
=================================

The Bookstore application is a small sample application resembling a
simple bookstore with a market-place facility where users can search for
books in an online catalog, and subsequently get offers from different
book sellers.

.. contents::

Architecture of the Bookstore
-----------------------------

The following figure shows a class diagram describing the structure of
the bookstore and a sequence diagram illustrating the dynamics of the
application.

.. list-table::
   
   * - .. figure:: ../images/kieker_bookstoreclassdiagram.svg
             :height: 300px
             
             Bookstore Class Diagram
     - .. figure:: ../images/kieker_SequenceDiagram-manually-changed.svg  
             :height: 300px
             
             Boockstore Sequence Diagram


The bookstore contains a catalog for books and a Customer Relationship
Management System (CRM) for the book sellers. To provide this service,
the different classes provide operations to initialize the application,
search for books, and get offers or searched books. In this example, the
methods implementing these operations are merely stubs. However, for the
illustration of Kieker, they are sufficient and the inclined reader may
extend the application into a real bookstore.

Introducing the Implementation
------------------------------

The directory structure of the Bookstore example is

-  ``ch2-bookstore-application``

   -  ``src/kieker/examples/userguide/ch2bookstore/``

      -  ``Bookstore.java``
      -  ``BookstoreStarter.java``
      -  ``Catalog.java``
      -  ``CRM.java``

   -  ``build.gradle``
   -  ``gradlew``
   -  ``graldew.bat``
   -  ``README.txt``

It comprises four Java classes in its source directory (ADD LOCATION
HERE), which are explained in detail below.

The Java sources and a pre-compiled binary of the uninstrumented
Bookstore application can be found in the
examples/userguide/ch2-bookstore-application/ directory.

The class ``BookstoreStarter`` contains the application's ``main``
method (shown in the listing below), i.e., the program start routine. It
initializes the ``Bookstore`` and issues five search requests by calling
the ``searchBook`` method of the ``bookstore`` object.

Main method from BookstoreStarter.java
''''''''''''''''''''''''''''''''''''''

.. code:: java
   
   public static void main(final String[] args) {
   
      final Bookstore bookstore = new Bookstore();
   
      for (int i = 0; i < 5; i++) {
         System.out.println("Bookstore.main: Starting request " + i);
         bookstore.searchBook();
      }
   }

The ``Bookstore``, shown below, contains a catalog and a CRM object,
representing the catalog of the bookstore and a customer relationship
management system which can provide offers for books out of the catalog.
The business method of the bookstore is ``searchBook()`` which will
first check the catalog for books and then check for offers.

In a real application these methods would pass objects to ensure the
results of the catalog search will be available to the offer collecting
method. However, for our example we omitted such code.

Bookstore.java
''''''''''''''

.. code:: java
	
	public class Bookstore {
		
		private final Catalog catalog = new Catalog();
		private final CRM crm = new CRM(this.catalog);
		
		public void searchBook() {
			this.catalog.getBook(false);
			this.crm.getOffers();
		}
	
	}

The customer relationship management for this application is modeled in
the ``CRM`` class shown the next listing. It provides only a business
method to collect offers by using the catalog for some lookup. The
additional catalog lookup is later used to illustrate different traces
in the application.

CRM.java
''''''''

.. code:: java
	
	public class CRM {
		
		private final Catalog catalog;
		
		public CRM(final Catalog catalog) {
			this.catalog = catalog;
		}
		
		public void getOffers() {
			this.catalog.getBook(false);
		}
	}

Finally, the class ``Catalog``, below, resembles the catalog component
in the application.

.. code:: java
	
	public class Catalog {
		
		public void getBook(final boolean complexQuery) {
			// nothing to do here
		}
	}

Compile and Run the Bookstore
-----------------------------

After this brief introduction of the application and its implementation,
the next step is to see the example running. To compile and run the
example, the commands in the next listing can be executed. This document
assumes that the reader enters the commands in the example directory.
For this first example this
is\ ``examples/userquide/ch2-bookstore-application/``.

To compile and start the code, enter the
``examples/userquide/ch2-bookstore-application/`` and run

-  ``./gradlew run`` for Linux, MacOS and others
-  ``gradlew.bat run`` in Windows

Recompiling can be triggered with

-  ``./gradlew jar`` for Linux, MacOS and others
-  ``gradlew.bat jar`` in Windows

Also the bundle already includes a pre-compiled version which can be
started with

-  ``java -jar build/libs/BookstoreApplication.jar``

When executed, the application should print the following lines:

Example output
''''''''''''''

.. code:: 
	
	Bookstore.main: Starting request 0
	Bookstore.main: Starting request 1
	Bookstore.main: Starting request 2
	Bookstore.main: Starting request 3
	Bookstore.main: Starting request 4
 

