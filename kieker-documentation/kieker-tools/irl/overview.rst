.. _kieker-tools-irl:

Instrumentation Record Language
===============================

The **Kieker** Instrumentation Record Language (IRL) can be used
to define new **Kieker** records also called (monitoring) events.
To install and setup Eclipse and detailed language syntax and semantics,
you may have a look at our tutorials:

- :ref:`kieker-tools-irl-how-to-install-the-irl-in-eclipse`
- :ref:`kieker-tools-irl-how-to-setup-the-irl-in-eclipse`
- :ref:`kieker-tools-irl-syntax-semantics`

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

Annotations
"""""""""""

All event types, template types, enumerations, models and sub-models
can have two annotations. Namely **@author** and **@since**. They allow
to name the original author of an element and the version since when
this type is supported by the IRL.

::
    
    @author "Ada Lovelace"
    @since "1.0"

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

Event type consist of a set of attributes. They cen either be inherited from
other event and template types or can be declared. Basically, each attribute
consists of a *base type* or *enumeration type*, namely, byte, short, int,
long, char, float, double and string (in future also key and date), and a
name. Subsequently, you may specify a default value which will be set in case
the developer does not set that attribute. You can use either literals or a
defined constant as default values.

Attributes can also have modifiers:

- **auto-increment** is best used with integer values. This modifier
  instructs to build a getter for that particular attribute which
  automatically increments the value of the variable. This
  is helpful to create order sets of events, e.g., traces.
- **transient** instructs the generator that this attribute must not be
  stored and serialized.
- **changeable** creates an attribute which is changeable. In Java this
  means the value of an attribute can be altered after creation.

::

   event EventName {
      const string NO_SESSION = "<no-session>"
      string name
      string sessionName = NO_SESSION
      auto-increment int value
      transient int transientValue
      changeable int data // can be changed during analysis
   }

Attributes can also have semantic annotations. They defined the purpose
of the attribute. This information helps to automatically generate probes for
certain event types.

::

	event EventName {
		long value : timestamp
		string text = "empty" : operationsignature
	}

Please note that the available semantics depend on the used semantics model.

Constants
"""""""""

Constants allow to define constant values which can be used as placeholders
or defaults. They are similarly defined to attributes, but prefixed with
the keyword **const**.

Enumeration
"""""""""""

Enumeration allow to define nominal values in the IRL. This is helpful
to avoid having to use plain integers for nominal values which can
lead to all sorts of programming errors.

The basic enumeration is just a set of literals, as shown in the
upper example. The lower one also assigns values to these literals.

::
    
    @author "Ada Lovelace"
    @since "1.0"
	enum Colors {
        Blue, Red, Green, Yellow, Orange, Purple, Aubergine
	}

	enum Colors {
        Blue = 1, Red = 2, Green = 4, Yellow = 8, Orange = 16,
        Purple = 32, Aubergine = 64
	}

Model Types
"""""""""""

Model types allow to add attributes to a set of event and template types
across any inheritance structures. For example when developers want to
add additional attributes to all types used for trace analysis, like
adding attributes to store invocation and return values to traces.

Without model types, you would have to extend every type which is part of
the trace events, separately. This is quite cumbersome and might lead to
errors. Model types allow to address this in a shorter way.

Firstly, you have to define a model. A *model* has a name (below *ModelName*)
followed by a set of types. Please note that beside the specified types, all
sub-types are also included in the set.

Secondly, you declare *sub*-model. It has a name (in the example below
*SubModelName* and *SubModel2Name*) and refers to a model by name (here
*ModelName*). Subsequently, you can specify a set of attributs enclosed in
curly braces or refer to a set of template types that declare the extension.

::

	model ModelName TypeA, TypeB, TypeC
	
	sub SubModelName ModelName {
		int additionaAttribute
		const int SOME_CONSTANT = 1
	}
	
	sub SubModel2Name ModelName : ExtensionTemplate, AdditionalExtensionTemplate



