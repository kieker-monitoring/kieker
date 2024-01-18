.. _developing-with-kieker-general-language-and-platform-support:

General Language and Platform Support 
=====================================

Kieker can be used to instrument and monitor different programming
languages and protocols. Depending on the language and platform
different methods of structuring and naming data structures, functions,
methods, object and modules exist. However, we want to ensure some
familiarity between all languages. Thus, this page contains some
guidelines how to define and structure a language support projects.

Conceptual Directory Structure
------------------------------

*  common

   *  records
   
      *  All the records generated automatically with the IRL compiler
         or crafted by hand

   *  utilities

      *  Utility functions, e.g., for serialization
*  monitoring

   *  controller

      *  Functionality and data structures to create and setup a
         monitoring controller, including a way to gain the current
         time, thread, process etc.
      *  **Monitoring controller** governs everything, including a
         shutdown hook if this is possible
      *  **Writer controller** which coordinates sending data somewhere
         or storing data in a file following the Kieker file formats
         (see :ref:`architecture-file-and-serialization-formats`).
         The writer controller might support different **writers** for
         different purposes which can be configured at runtime or
         compile time, depending on the language.
      *  In some setups, it is helpful to be able to store data in
         probe. Therefore, it is an optional feature to provide the
         setup of runtime properties in a **probe controller**. A
         Typical feature is to activate and deactivate probes at
         runtime. However, this could also be achieved by other means
         depending on the language. This features is required in case
         beside monitoring also actors should be implemented.
      *  The **probe controller** can get its information from a file or
         by other means. Like the Java implementation of Kieker which
         accepts data events via TCP. Be aware that using this option
         can be a vulnerability issue. Therefore, such TCP connection
         must be governed by security measures.

   *  probes

      *  Probes contain ready to use implementations of probes for a
         specific language.
         
*  `README.md <http://README.md>`_ (containing basic information about
   the project, build instructions and references to the wiki to explain
   how the package is used)
*  License

Language Pack Naming
--------------------

The language pack should be named kieker-lang-pack-LANGUAGE or if
monitoring and common are packed in different libraries
kieker-monitoring-lang-pack-LANGUAGE and
kieker-common-lang-pack-LANGUAGE.

Supported Languages
-------------------

See :ref:`instrumenting-software`


