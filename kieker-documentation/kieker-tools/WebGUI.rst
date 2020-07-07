.. _kieker-tools-webgui:

WebGUI 
======

1 Introduction
==============

The purpose of this document is to provide a technical documentation of
the Kieker.WebGUI project. It is written mostly for other developers. It
gives not only an overview over the whole project, but details the used
technologies, components, relationships and single design decisions as
well. This document is **not** designed as an user guide or user manual.

2 Project Overview
==================

2.1 Purpose and Target of the Project
-------------------------------------

The Kieker.WebGUI is a graphical user interface to assemble, control,
and observe an analysis instance of the Kieker framework. For reasons of
performance and administrability the tool is a web application. It can
be used in a common browser by multiple users at a time.

2.2 Technologies and Dependencies
---------------------------------

As the tool is a web application, it is developed as a JavaEE
application using JavaServer Faces for the most part. For advanced
visual components and themes the open source component framework
Primefaces (` http://primefaces.org <http://primefaces.org>`__) is used.
Spring
(` http://www.springsource.org/ <http://www.springsource.org/>`__) is
used as a dependency framework. Spring security
(` http://static.springsource.org/spring-security/site/index.html <http://static.springsource.org/spring-security/site/index.rst>`__)
is the security framework to check authentications and authorizations.
In order to provide human-readable URLs, the URL Rewrite Filter
PrettyFaces
(` http://ocpsoft.org/prettyfaces/ <http://ocpsoft.org/prettyfaces/>`__)
is used. The Kieler layout algorithms are used to layout graphs within
the application. Furthermore the GUI has naturally a direct dependency
to the Kieker framework itself. The used build management tool is Apache
Maven (` http://maven.apache.org <http://maven.apache.org>`__).

2.3 Licensing Issues
--------------------

The project is licensed under the Apache 2 License. Therefore all other
dependencies have to be compatible with this license. All dependencies
should furthermore have a corresponding .LICENSE file in the lib folder.

3 Design Overview
=================

The Kieker.WebGUI is a typical multi layered web application. The
architecture can be seen in the following figure.

| 

|image0|

3.1 Web Layer
-------------

3.1.1 Web Pages
~~~~~~~~~~~~~~~

The web paces within the application have a similar structure. Therefore
we use a hierarchical template structure (within the *template*
directory) for this pages. Each of the pages has also an own css file
(in the *css' directory). Additional dialogs for the pages can be found
in the* dialogs' directory. Special pages only available for
administrators should be in the *pages/admin* directory.

3.1.1.1 LoginPage.xhtml
^^^^^^^^^^^^^^^^^^^^^^^

The page to log into the system. The technical part behind the login is
performed by Spring Security. If the login succeeds, the user is
directly forwarded to the project overview page. If it fails, the user
is redirected to the login page with an additional info about the fail.

3.1.1.2 AccessDeniedPage.xhtml
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

A mostly empty page. It displays only a warning image and tells the user
that he doesn't have the necessary rights to enter the page.

3.1.1.3 AnalysisEditorPage.xhtml
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

3.1.1.4 CockpitEditorPage.xhtml
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

3.1.1.5 CockpitPage.xhtml
^^^^^^^^^^^^^^^^^^^^^^^^^

3.1.1.6 ControllerPage.xhtml
^^^^^^^^^^^^^^^^^^^^^^^^^^^^

3.1.1.7 ProjectOverviewPage.xhtml
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

3.1.2 Beans and Converter
~~~~~~~~~~~~~~~~~~~~~~~~~

3.1.2.1 Application Scoped
^^^^^^^^^^^^^^^^^^^^^^^^^^

ProjectsBean
''''''''''''

This bean provides and manages a list with the application wide
available projects. It provides furthermore methods to manage (add,
delete e.g.) projects from the JSF context.

ThemeSwitcherBean
'''''''''''''''''

This bean simply provides a map containing all available look and feels
(themes) for the application. The actual themes are injected via Spring.

3.1.2.2 Request Scoped
^^^^^^^^^^^^^^^^^^^^^^

StringBean
          

This bean is only responsible for saving one string during a request
(e.g. the name of a new project). But this bean also provides some
simple String related methods for places where a bean is necessary.

UploadFileBean
              

A simple bean which contains a file to be uploaded during a request.

NewUserBean
           

This is a simple container to store the information needed to create a
new user during a request.

3.1.2.3 Session Scoped
^^^^^^^^^^^^^^^^^^^^^^

UserBean
''''''''

3.1.2.4 View Scoped
^^^^^^^^^^^^^^^^^^^

As the view scope is technically not available in the Spring managed
bean, it was necessary to manually implement this one (see class
*ViewScope*).

CurrentAnalysisEditorBean
'''''''''''''''''''''''''

Prototype Pattern
                 

The bean uses the prototype pattern to create new components for the
model. The available components are modified to provide a copy method.
The copy method delivers a new copy of a component with all properties -
but without connected components.

CurrentAnalysisEditorGraphBean
''''''''''''''''''''''''''''''

3.2 Service Layer
-----------------

The service layer uses interfaces (marked yellow in the figure) to
abstract the actual implementation.

User Service
~~~~~~~~~~~~

The user service is merely a delegator. It passes the method calls
directly to the user DAO at the lower layer.

Project Service
~~~~~~~~~~~~~~~

This service delegates all tasks about the projects (create projects,
start analysis e.g.) to the DAO at the lower layer and the analysis
controller. It manages the synchronization between the projects by using
two maps with lock objects.

Graph Layout Service
~~~~~~~~~~~~~~~~~~~~

The graph layout service provides just one method to layout a given
graph. The most methods within the layout service are just responsible
for converting the given nodes and edges into a valid format. The actual
layouting is performed by the Kieler layout algorithms.

3.3 Persistence Layer
---------------------

3.3.1 User DAO
~~~~~~~~~~~~~~

The current implementation of the user data access object uses Apache
Derby as an embedded user database. It provides methods to add, edit,
and remove users within the system. Due to the usage of an interface it
is of course possible to replace this DAO.

3.3.2 Project DAO
~~~~~~~~~~~~~~~~~

The current implementation of the project data access objects used the
file system to store and load projects. Due to the usage of an interface
it is of course possible to replace this DAO.

3.3.2.1 Class Loader Handling
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

As it is possible to add and remove project libraries during runtime, it
is necessary to manage the resulting class loaders correctly. The
project DAO creates a new temporary directory for every new class
loader. The project libraries are copied into this folder and a new
class loader is created. The DAO uses a weak map to observe the existing
class loaders. If a class loader has been closed and disposed, the
remaining temporary files are being removed.

However, it was necessary to implement a special class loader
(*CloseableURLClassLoader*) which can be closed. A closeable URL class
loader is not available in Java 1.6.

3.4 Common and Domain Objects
-----------------------------

This is a vertical layer at many classes within this layer are used
through all other layers. Those are, for example, exception and domain
classes. Some more specific classes will be explained in more detail in
the following.

3.4.1 Plugin Decorators
~~~~~~~~~~~~~~~~~~~~~~~

3.4.2 ViewScope
~~~~~~~~~~~~~~~

This class is a manual implementation of JSF's view scope. This is
necessary, as Spring doesn't support the view scope directly.

4 Configuration and Properties Files
====================================

4.1 Spring
----------

As a lot of components are created and configured via Spring, the
configuration is split into seven different files. The files can be
found in *\\src\main\webapp\WEB-INF*

4.1.1 Kieker
~~~~~~~~~~~~

This configuration file has the name *spring-kieker-config.xml*. It is
normally used only for test purposes and can be used to easily weave
Kieker monitoring code into the WebGUI. For more details look into the
Kieker user guide.

4.1.2 Quartz
~~~~~~~~~~~~

This configuration file has the name *spring-quartz-config.xml*. It is
used to configure the quartz time scheduler used for updating the
display components. This is necessary for the analysis cockpit.

4.1.3 Spring Security
~~~~~~~~~~~~~~~~~~~~~

The configuration for the security part of the WebGUI is stored in the
two files *spring-security-config.xml* and *spring-security-taglib.xml*.
The second file maps the JSF tags to the correct methods of the spring
framework. The other file configures which urls have to be intercepted
and which pages can be accessed with the different roles. Modify those
configuration files with care.

4.1.4 Database
~~~~~~~~~~~~~~

The configuration for the database is stored in the file
*spring-database-config.xml*. It contains the (spring managed) data
source, the transaction manager, and the default available entries.
Those make sure that the tables and some default users are created.

4.2 Pretty Faces
----------------

The configuration file for Pretty Faces has the name
*pretty-config.xml*. It allows to use short and nice looking URLs
instead of long and complicated ones. Modify this configuration file
with care, as it can influence the security part of the application.

4.3 Maven
---------

Maven is used as a build tool for the project. The main configuration
can be found in the *pom.xml*. More configuration files can be found in
*\\config\descriptors*.

The files in the latter directory are responsible for packing the
correct files into the bin- and src-archives.

All further dependencies and plugins are configured in *pom.xml*.

4.4 Log4J
---------

The configuration file for Log4J is stored under
*\\src\main\resources\log4j.properties*. The current implementation
avoids a console output and uses instead a single log file. Only
messages with level WARN or higher are logged.

4.5 JSF
-------

4.6 Web.xml
-----------

4.7 Localization
----------------

The localized messages and texts are stored in the files within
*\\src\main\resources\lang*. Currently only German and English are
supported.

4.8 Static Tests
----------------

We use Findbugs, Checkstyle and PMD to test the code during the package
phase. The tools are configured in the files under
*\\config\quality-config*.

5 Conventions
=============

5.1 Security Annotations
------------------------

Security annotations should be used on the interface level.

5.2 Transaction Annotations
---------------------------

Transaction annotations should be used on the implementation level. An
implementation of the *IUserDAO* interface for example, is responsible
for a valid transaction management.

5.3 Exception and Log Handling
------------------------------

Exceptions should be caught, refined, and thrown if necessary. However,
the methods on the web level should not throw any exceptions.

Every exception that is not thrown, should be logged.

