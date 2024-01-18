.. _developing-with-kieker-java-kieker-architecture-model:

Kieker Architecture Model
=========================

The Kieker Analysis Model (KAM) provides different metamodels for
- Types of Components, Interfaces, Operations and Data/Storage

- General architecture of a software, alias Assembly which contains instances of
  the Component-types, Operation-types and Storage-types of the Types model

- Deployed architecture of a software, alias Deployment model which contains
  instances of the Assmbly-model elements.

- Information calls within the architecture are stored in the Execution model

- Statistical information, e.g., number of calls, response time etc. are stored
  in the Statistics model

- The Trace model can contain information of call traces

- Source model is a hash map that maps objects of all previous models to
  source labels. Each element can have different sources, as they can be derived
  from information from different sources.

The KAM is specified in analysismodel.ecore file and a associated
analysismodel.genmodel is used to generate Java classes for it. This makes these
different metamodels for Types, Assembly, Deployment, etc. parts of the KAM
metamodel. Each part of the metamodel has its own package. While this may change
in future to separate the respective metamodel parts in their own ecore files,
currently they are in one ecore model. In the remainder, we refer to them
as metamodels.

General Patterns used in the Model
----------------------------------

- Maps instead of lists

- **signature** A signature comprises a package or file indicator with the
  name of the element. In Java this is usually a package name in lowercase dot
  notation for the package, e.g., org.example.pkg, followed by the element name,
  e.g., MyClass, or myOperation. The signature is a fullyqualified name and
  unique in the model for its respective element.

Types Metamodel
---------------

Package: analysismodel.type

Each element of this model is called a type of something, as they represent the
type level in the metamodel. In OOP languages, this would correspond to the actual
class and methods defined by the programmer.

++ add image here ++

TypeModel
~~~~~~~~~

The TypeModel class is the root class of the Type metamodel. It contains a
set of ComponentType-elements. As this is a map, the ecore metamodel uses a
map class EStringToComponentTypeMapEntry to store key-value-pairs where
the key is the component type signature and the value is the actual component type.

- **componentTypes** map of component types

ComponentType
~~~~~~~~~~~~~

Represents one component type, e.g., a Java class, a python class, a Fortran file,
or a source code directory, in case file names are not sufficient component type
boundaries.

- **signature** of the component type comprising package name and component name
- **name** the name of the component type, derived from the signature
- **package** the package name of the component type, derived from the signature
- **providedOperations** map of operation types of this component type
- **providedStorages** map of storage types of this component type
- **containedComponents** some component types might be used by this component type
  these are listed in this list of component typess
- **providedInterfaceTypes** map of provided interface types
- **requiredInterfaceTypes** map of required interface types

OperationType
~~~~~~~~~~~~~

On operation type represents a function, procedure, method, or subroutine.

- **signature** of the operation type comprising return type, name, modifieres, 
  parameters, and exceptions. The name is unique within the component type
- **name** of the operation type, e.g., the method name in Java
- **returnType** of the operation type
- **modifiers** of the operation type as an list of strings
- **parameteTypes** of the operation type as a list of strings, names of the
  parameter are not stored, as they do not play a relevant role in the erasure.
  Note: This might be a Java based oversimplification.

StorageType
~~~~~~~~~~~

Storage types represent in memory global or component global entities or databases
that can store data and persist at least over the execution of the software. As a
storage is always located in a component type, global memory is depicted by a
global component type with no further functionality holding these storage types.

- **name** of the storage type (must be unique in context of a component type)
- **type** is the data type of the storage

ProvidedInterfaceType
~~~~~~~~~~~~~~~~~~~~~

A provided interface type is a collection of operation types of a component type.
In this notion, all operation types that are not listed in a provided interface
type can be considered private regarding the component type, regardless of their
operation modifiers. We do not use this strict, as provided interface types are
optional. However, in a model containing provided interfaces types only operation
types listed on one provided interface must be called by external operation types
in other component types.

- **signature** Fully qualified name comprising package name and interface name
- **name** of the interface type, must be unique within the component type
- **package** name of the interface type
- **providedOperationTypes** map containing all the operation types exposed by
  this provided interface type

RequiredInterfaceType
~~~~~~~~~~~~~~~~~~~~~

A required interface type indicates that a component type is accessing an
provided interface of another component type.

- **requires** referencd to the provided interface type of another component type
 
