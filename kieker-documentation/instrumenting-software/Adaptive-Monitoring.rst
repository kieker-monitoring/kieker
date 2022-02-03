.. _instrumenting-software-adaptive-monitoring:

Adaptive Monitoring 
===================

This article is a stub, as there are different technologies available to
adaptively instrument applications. However, we need a place to collect
some content.

.. note::

   There is a section in :ref:`technical-details-java-monitoring-controller-api`
   also addressing this topic. The grammar should be moved to API and
   only referenced from here.
   
.. note::
  
  Please recheck whether the grammar is still correct. 

.. note::
  
  Add list of options how to control adaptive monitoring.

Configuring Kieker for Adaptive Monitoring
------------------------------------------

Control Probes
--------------

Pattern Parsing Grammar
-----------------------

The Java pattern parser follows essentially the following grammar.
Please note that this is going to change in the future.

**Pattern parsing grammar**

.. code-block:: ANTLR

  Pattern: WS* TrimmedPattern WS*
  
  TrimmedPattern: '%' java.util.regex.Pattern | KiekerPattern
  
  KiekerPattern: '*' | Modifiers ReturnType FQName '(' WS* Parameters WS* ')' Throws?
  
  Modifiers: (Visibility WS)? (Abstract WS)? (Default WS)? (Static WS)?
    (Final WS)? (Synchronized WS)? (Native WS)?
  
  Visibility: "public" | "private" | "protected" | "package"
  
  Abstract: "abstract" | "non_abstract"
  
  Default: "default" | "non_default"
  
  Static: "static" | "non_static"
  
  Final: "final" | "non_final"
  
  Synchronized: "synchronized" | "non_synchronized"
  
  Nativ: "native" | "non_native"
  
  ReturnType: "new" | FQClassName
  
  FQName: FQClassName '.' methodName
  
  Parameters: (
    ".." |
    ".." Parameter |
    "*" |
    FQClassName ) Parameter*
  
  Parameter: ".." | "*" | FQClassName
  
  Throws: WS* "throws" Parameters WS*
  
  FQClassName: ID ('.' ID)*
 

