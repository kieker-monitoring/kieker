.. _technical-details-java-records-api:

Monitoring Records API
======================

Monitoring records are objects that contain the monitoring data, e.g.,
timestamps, operation names, and resource utilization values. Typically,
an instance of a monitoring record is constructed in a :ref:`technical-details-java-probes-api`
passed to the :ref:`technical-details-java-monitoring-controller-api`,
serialized and deserialized by a :ref:`technical-details-java-writer-api` and :ref:`technical-details-java-reader-api`.
In :ref:`getting-started`, we already introduced and used the monitoring
record type **OperationExecutionRecord**. **Kieker** allows to use custom
monitoring record types. Corresponding classes must implement the
interface **IMonitoringRecord** and serialization and deserialization must
use **IValueSerializer** and **IValueDeserializer** to serialize and
deserialize values based on a specific theme.

.. todo::
  
  References in the text block above are not resolved.

IMonitoringRecord API
---------------------

In order to use the abstract class for implementing your own monitoring
record type, you need to: Create a class that extends 
**AbstractMonitoringRecord** and imlements **IMonitoringRecord**.
In detail the following methods must be defined.

.. note::

   Please note that we provide a code generator which automatically
   generates Kieker compatible record structures for you based on a
   simple DSL. It is easier and saver to use our DSL to generate
   record types than to create them by hand.
 
- Getter and setter for the logging timestamp.  
  - ``public long getLoggingTimestamp();``
  - ``public void setLoggingTimestamp(long timestamp);``
- A propper ``toString`` method for debugging purposes.
  - ``public String toString();``
- A serializer method to serialize values. The method
  ``public void serialize(IValueSerializer serializer) throws BufferOverflowException;``
  must use a ``IValueSerializer`` to serialize values. For all simple
  types `boolean`, `byte`, `short`, `char`, `int`, `long`, `float`, 
  `double`, and `string`, the proper serializer functions must be used
  and for all enumerations `putEnumeration`.
  
  Arrays come in two flavors in **Kieker**. Fixed sized arrays are
  written as a sequence of entries based on the primitive type. In
  multi dimensional arrays the right most array represents the inner
  loop, while the left most the outer loop. In variable arrays, the
  writing of array entries is preceded by an `int` containing the
  length of the array.
- Return an array of the types used in the record and the names of the
  attributes of the record 
  - ``public Class<?>[] getValueTypes();``
  - ``public String[] getValueNames();``
- Record size method. Provides the size of the record as defined by ``SIZE``.
  ``public int getSize();`` 

Additionally, the class must contain three static variables called
- ``SIZE`` representing the size of the record based on its static part. Thus, it does not reflect the real size when dynamic arrays are used.
- ``TYPES`` array of all types which is returned by ``getValueTypes``.
- ``VALUE_NAMES`` array of all attribute names which is returned by ``getValueNames``.

The class **EntryLevelBeforeOperationEvent** in the Listing below, is an
example of a custom monitoring record type. It is based on an existing
type **BeforeOperationEvent** and used to collect call trace information
and entry level parameters used in servlet requests and similar service
endpoints.

In our DSL, this record looks like this:

.. code-block:: irl
  
  template IPayloadCharacterization {
          string[] parameters
          string[] values
          int requestType
  }
  
  event EntryLevelBeforeOperationEvent extends BeforeOperationEvent : 
      IPayloadCharacterization

The event **EntryLevelBeforeOperationEvent** extends
**BeforeOperationEvent** and inherits parameter from the template
**IPayloadCharacterization**. The template define two dynamic `string`
arrays and one `integer` value. 

More details on the DSL can be found in the DSL documentation.

.. todo::

   Add reference to DSL documentation when available.

.. code-block:: java

  package org.example.record;
  
  import java.nio.BufferOverflowException;
  
  import kieker.common.exception.RecordInstantiationException;
  import kieker.common.record.flow.trace.operation.BeforeOperationEvent;
  import kieker.common.record.io.IValueDeserializer;
  import kieker.common.record.io.IValueSerializer;
 
  public class EntryLevelBeforeOperationEvent extends BeforeOperationEvent implements IMonintoringRecord {               

The class inherits attributes from **BeforeOperationEvent** which in
turn inherits attributes from different types. These are
- ``timestamp`` time when the event occured (as opposed to loggingTimeStamp which represents the time when the event was logged.
- ``traceId`` the id of the trace this record belongs to.
- ``orderIndex`` the sequence number of the event within the trace.
- ``operationSignature`` the operation to be executed.
- ``classSignature`` the signature of the class the operation belongs to.

The ``SIZE``, ``TYPES`` and ``VALUE_NAMES`` constant. As you can see 
from the comments, these attributes originate from other types. Still
they must be listed here, as we use this list as a fast lookup.
Similarily, the attribute names must all be listed in ``VALUE_NAMES``.

.. code-block:: java

       public static final int SIZE = TYPE_SIZE_LONG // IEventRecord.timestamp
                  + TYPE_SIZE_LONG // ITraceRecord.traceId
                  + TYPE_SIZE_INT // ITraceRecord.orderIndex
                  + TYPE_SIZE_STRING // IOperationSignature.operationSignature
                  + TYPE_SIZE_STRING // IClassSignature.classSignature
                  + TYPE_SIZE_STRING // IPayloadCharacterization.parameters
                  + TYPE_SIZE_STRING // IPayloadCharacterization.values
                  + TYPE_SIZE_INT; // IPayloadCharacterization.requestType
       
       public static final Class<?>[] TYPES = {
            long.class, // IEventRecord.timestamp
            long.class, // ITraceRecord.traceId
            int.class, // ITraceRecord.orderIndex
            String.class, // IOperationSignature.operationSignature
            String.class, // IClassSignature.classSignature
            String[].class, // IPayloadCharacterization.parameters
            String[].class, // IPayloadCharacterization.values
            int.class, // IPayloadCharacterization.requestType
       };

       /** property name array. */
       public static final String[] VALUE_NAMES = {
            "timestamp",
            "traceId",
            "orderIndex",
            "operationSignature",
            "classSignature",
            "parameters",
            "values",
            "requestType",
       };
              
       private static final long serialVersionUID = -3583783831259543534L;

Declaration of additional parameters which cannot be inherited.

.. code-block:: java
  
       /** property declarations. */
       private final String[] parameters;
       private final String[] values;
       private final int requestType;

Constructor for value based initialization. Normally used inside of
probes.

.. code-block:: java
  
       public EntryLevelBeforeOperationEvent(final long timestamp, final long traceId, final int orderIndex, final String operationSignature, final String classSignature, final String[] parameters, final String[] values, final int requestType) {
            super(timestamp, traceId, orderIndex, operationSignature, classSignature);
            this.parameters = parameters;
            this.values = values;
            this.requestType = requestType;
       }

Constructor used to initialize the record using a deserializer. Note
also in this constructor inherited attributes can be deserialized by the
constructor of the inherited class.

In this example, the record has two attributes with a dynamic string
array type. Thus, the constructor first reads the array size, before
reading the string values.

.. code-block:: java

       public EntryLevelBeforeOperationEvent(final IValueDeserializer deserializer) throws RecordInstantiationException {
            super(deserializer);
            // load array sizes
            final int _parameters_size0 = deserializer.getInt();
            this.parameters = new String[_parameters_size0];
            for (int i0=0;i0<_parameters_size0;i0++)
                 this.parameters[i0] = deserializer.getString();
            
            // load array sizes
            final int _values_size0 = deserializer.getInt();
            this.values = new String[_values_size0];
            for (int i0=0;i0<_values_size0;i0++)
                 this.values[i0] = deserializer.getString();
            
            this.requestType = deserializer.getInt();
       }

To be able to send or store records, they must be serialized. This is
implemented by the following function. 

.. code-block:: java
  
       @Override
       public void serialize(final IValueSerializer serializer) throws BufferOverflowException {
            serializer.putLong(this.getTimestamp());
            serializer.putLong(this.getTraceId());
            serializer.putInt(this.getOrderIndex());
            serializer.putString(this.getOperationSignature());
            serializer.putString(this.getClassSignature());
            // store array sizes
            int _parameters_size0 = this.getParameters().length;
            serializer.putInt(_parameters_size0);
            for (int i0=0;i0<_parameters_size0;i0++)
                 serializer.putString(this.getParameters()[i0]);
            
            // store array sizes
            int _values_size0 = this.getValues().length;
            serializer.putInt(_values_size0);
            for (int i0=0;i0<_values_size0;i0++)
                 serializer.putString(this.getValues()[i0]);
            
            serializer.putInt(this.getRequestType());
       }

Further API functions.

.. code-block:: java
  
       @Override
       public Class<?>[] getValueTypes() {
            return TYPES; // NOPMD
       }
       
       @Override
       public String[] getValueNames() {
            return VALUE_NAMES; // NOPMD
       }
       
       @Override
       public int getSize() {
            return SIZE;
       }
  
       @Override
       public boolean equals(final Object obj) {
            if (obj == null) {
                 return false;
            }
            if (obj == this) {
                 return true;
            }
            if (obj.getClass() != this.getClass()) {
                 return false;
            }
            
            final EntryLevelBeforeOperationEvent castedRecord = (EntryLevelBeforeOperationEvent) obj;
            if (this.getLoggingTimestamp() != castedRecord.getLoggingTimestamp()) {
                 return false;
            }
            if (this.getTimestamp() != castedRecord.getTimestamp()) {
                 return false;
            }
            if (this.getTraceId() != castedRecord.getTraceId()) {
                 return false;
            }
            if (this.getOrderIndex() != castedRecord.getOrderIndex()) {
                 return false;
            }
            if (!this.getOperationSignature().equals(castedRecord.getOperationSignature())) {
                 return false;
            }
            if (!this.getClassSignature().equals(castedRecord.getClassSignature())) {
                 return false;
            }
            // get array length
            int _parameters_size0 = this.getParameters().length;
            if (_parameters_size0 != castedRecord.getParameters().length) {
                 return false;
            }
            for (int i0=0;i0<_parameters_size0;i0++)
                 if (!this.getParameters()[i0].equals(castedRecord.getParameters()[i0])) {
                      return false;
                 }
            
            // get array length
            int _values_size0 = this.getValues().length;
            if (_values_size0 != castedRecord.getValues().length) {
                 return false;
            }
            for (int i0=0;i0<_values_size0;i0++)
                 if (!this.getValues()[i0].equals(castedRecord.getValues()[i0])) {
                      return false;
                 }
            
            if (this.getRequestType() != castedRecord.getRequestType()) {
                 return false;
            }
            
            return true;
       }
       
       @Override
       public int hashCode() {
            int code = 0;
            code += ((int)this.getTimestamp());
            code += ((int)this.getTraceId());
            code += ((int)this.getOrderIndex());
            code += this.getOperationSignature().hashCode();
            code += this.getClassSignature().hashCode();
            // get array length
            for (int i0=0;i0 < this.parameters.length;i0++) {
                 for (int i1=0;i1 < this.parameters.length;i1++) {
                      code += this.getParameters()[i0].hashCode();
                 }
            }
            
            // get array length
            for (int i0=0;i0 < this.values.length;i0++) {
                 for (int i1=0;i1 < this.values.length;i1++) {
                      code += this.getValues()[i0].hashCode();
                 }
            }
            
            code += ((int)this.getRequestType());
            
            return code;
       }


Getters and (setters if necessary) for every new attribute.

.. code-block:: java
  
       public final String[] getParameters() {
            return this.parameters;
       }
       
       
       public final String[] getValues() {
            return this.values;
       }
       
       
       public final int getRequestType() {
            return this.requestType;
       }
       
  }
  

