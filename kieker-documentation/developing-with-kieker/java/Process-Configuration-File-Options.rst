.. _developing-with-kieker-java-process-configuration-file-options:

Process Configuration File Options
==================================

Kieker has switched to **JCommander** for command line parsing in
version 1.14 which greatly simplified command line parameter handling.
However, its configuration file facilities relied only on the
``java.lang.Properties`` API. To make handling easier and utilize the
type system to correctly represent settings, e.g., numbers are numbers
and file-handles are file-handles, we implemented a configuration 
parser in ``kieker.tools.settings.ConfigurationPaerser``. The whole
API mimicks the approach of `JCommander <https://jcommander.org>`_ and
shares concepts, e.g., ``IStringconverter`` and ``IValueValidator``.

To utilize the new API implement your configuration parsing as follows:

.. code-block :: java
  
  final ConfigurationParser parser =
     new ConfigurationParser(PREFIX, this.settings);
  
  try {
     parser.parse(configuration);
  } catch (final ParameterException e) {
     this.logger.error(e.getLocalizedMessage());
     return false;
  }
     
  ...

The configuration parser checks the settings object for attributes
annotated with ``@Setting`` and searched for matching properties in the
configuration.

the parameter in the configuration file follow one of two schemes:

 * ``<package path>.<property> = <value>``
 * ``<package path>.<ClassName>.<property> = <value>``
 
In the past this allowed to
configure analysis stages via the same mechanism, as the monitoring, but
has several downsides. Stages must check validate settings and add default
by themselves scattering checks and when settings depend on each other,
parameters have to be processed outside of the stages beforehand. 
Furthermore, parameter names tend to be long when they mirror the
fully qualified names of parameters in classes.


Standard Property Handling
--------------------------

For the standard scheme ``<package path>.<property> = <value>`` the
configuration parser uses the ``PREFIX`` parameter as prefix for
parameters in the configuration file. For example, you have an entry

``my.analysis.experimentId = 5``

the ``my.analysis`` is the prefix and the attribute in the settings
object would be named ``experimentId``. The corresponding settings class
would lokk like:

.. code-block :: java
  
  public class Settings {
  
     @Setting
     private Integer experimentId;
     
     public Integer getExperimentId() {
        return this.experimentId;
     }
  }

The parser automatically tries to convert the value string "5" from the
configuration file to an integer, as the attribute is of type integer.
This works also for other types including numeric, boolean, character
and String.


Class-Name-based Property Handling
----------------------------------

To class name based properties, i.e., ``<package path>.<ClassName>.<property> = <value>``
can have different prefix. Thus, the prefix approach would not work here.
Therefore, you can specify the class name for the attribute.

.. code-block :: java
  
  import my.analysis.ExperimentDescriptor;
  
  public class Settings {
  
     @Setting(classMapping = ExperimentDescriptor.class)
     private Integer experimentId;
     
     public Integer getExperimentId() {
        return this.experimentId;
     }
  }

The example setting will then require ``my.analysis.ExperimentDescriptor.``
as prefix before the experimentId.


Utilizing Converters
--------------------

For certain types, there is no default converter stored registered with
the ``ConfigurationParser`` or in case you want to convert a value
differently, you have to add a value converter. The value converter uses
the ``IStringconverter`` interface from `JCommander <https://jcommander.org/#_custom_types_converters_and_splitters>`_.
Thus, you can use their predefined converters or implement your own
following that interface.

In the following example, we use the ``PathConverter``.

.. code-block :: java
    
  public class Settings {
  
     @Setting(converter = PathConverter.class)
     private Path inputPath;
     
     public Integer getInputPath() {
        return this.inputPath;
     }
  }

The ``PathConverter`` look like this.

.. code-block :: java
  
  public class PathConverter implements IStringConverter<Path> {
  
  public Path convert(String value) {
    return Paths.get(value);
  }
  
}


Validating Parameters
---------------------

In **JCommander** different validation APIs are supported. However,
we only support ``IValueValidator``.
The `interface can be found here <https://github.com/cbeust/jcommander/blob/master/src/main/java/com/beust/jcommander/IValueValidator.java>`_.

Lets use the previous example and check whether the file exists.

.. code-block :: java
    
  public class Settings {
  
     @Setting(converter = PathConverter.class, validators = ReadFileValueValidator.class)
     private Path inputPath;
     
     public Path getInputPath() {
        return this.inputPath;
     }
  }

This validator from the Kieker project, checks whether the given path
exists and can be read. It is possible to add multiple validators.


Required Settings
-----------------

Not all settings are rquired. Thus, we need a way to specify which 
parameters must be present in the configuration. To make the previous
example a required setting, we add ``required = true``.

.. code-block :: java
    
  public class Settings {
  
     @Setting(required = true, converter = PathConverter.class,
         validators = ReadFileValueValidator.class)
     private Path inputPath;
     
     public Path getInputPath() {
        return this.inputPath;
     }
  }

In case the configuration file does not contain a required setting, the
parser will fail and trigger a ``ParameterException`` in the same way
**JCommander** does. This is to enable us to share API with JCommander.


Handle Multiple Values
----------------------

To handle parameters that take multiple values as input, e.g. lists,
we can specify ``variableArity = true``. This will invoke a splitter and
split up a value string form the configuration file into a list of
string values, which are then converted using a specified value
converter or a default value converter.

The default splitter is the ``CommaParameterSplitter`` class from
**JCommander**.

.. code-block :: java
  
  public class Settings {
  
     @Setting(required = true, converter = PathConverter.class, 
         validators = ReadFileValueValidator.class,
         splitter = PathParameterSplitter.class)
     private List<Path> inputPaths;
     
     public List<Path> getInputPaths() {
        return this.inputPaths;
     }
  }

The ``PathParameterSplitter`` splits a multi-path-value based on the
path separator used on your plattform. That is a semicolon (;) on
Windows and a colon (:) on other platfroms, like Linux and Mac.

