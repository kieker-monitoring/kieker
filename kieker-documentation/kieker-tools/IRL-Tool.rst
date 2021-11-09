.. _kieker-tools-irl:

Instrumentation Record Language
===============================

The **Kieker** Instrumentation Record Language (IRL) can be used
to define new **Kieker** records also called (monitoring) events.

Language Features
-----------------

- Record types called **event** with scalar and array attributes
- Template types which allow to group attributes and can be used as marker
  analog to interfaces in Java. They are not storable.  
- Transient and serializable attributes
- Auto incrementing attributes
- Aliasing attributes
- Multi-inheritance from templates
- Events can inherit only from one other event, but multiple templates
- Linking attributes with foreign key
- Default values for attributes
- Assigning logging semantics
- Model types, they allow to group event types and then extend all event
  types belonging to the model type together.
  
Event Types
"""""""""""

::

   event EventName {
      string name
   }
   
   event ChildEvent extends EventName {
      int children
   }


**EventName** has one attribute name of type string and **ChildEvent** inherits
the name attribute and adds a children attribute.

Template Types
""""""""""""""

::

   template TraceEvent {
      int traceId
   }
   template OperationEvent {
      string operationName
   }
   template ClassEvent {
      string className
   }
   template BeforeOperationEvent : TraceEvent, OperationEvent, ClassEvent
   event SpecialChild extends EventName : BeforeOperationEvent


Templates allow to group attributes. **TraceEvent** defines the attribute
traceId. **OperationEvent** and **ClassEvent** define operationName and
className. **BeforeOperationEvent** inherits from all templates attributes.
Finally, **SpecialChild** inherits the **EventName** from above and all
attributes from the templates.

Attributes
""""""""""

::

   event EventName {
      const string NO_SESSION = "<no-session>"
      string name
      string sessionName = NO_SESSION
      auto-increment int value
      transient int key
      changeable int data // can be changed during analysis
   }

Here an example with semantic annotations ::

	event EventName {
		long value : timestamp
		string text = "empty" : operationsignature
	}

Please note that the available semantics depend on the used semantics model.


Model Types
"""""""""""

::

	model ModelName TypeA, TypeB, TypeC
	
	sub SubModelName ModelName {
		int additionaAttribute
		const int SOME_CONSTANT = 1
	}
	
	sub SubModel2Name ModelName : ExtensionTemplate, AdditionalExtensionTemplate


Language Syntax
---------------

:ref:`kieker-tools-irl-syntax-semantics`

Generating Records
------------------

- Commandline

Eclipse Installation
""""""""""""""""""""

- Install package from: https://maui.se.informatik.uni-kiel.de/repo/kdt/
- You may choose the repository under snapshot or a more stable releast from
  the releases folder.
- Add the Xtext nature to the Eclipse project of Kieker
- Configuration of IRL can be found in the Project Properties and
  Workspace Preferences. It is necessary to setup the properties/preferences to
  ensure correct generation of event types. You can select the target language
  and Kieker Version you want to support.


