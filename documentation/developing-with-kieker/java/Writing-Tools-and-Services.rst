.. _developing-with-kieker-java-writing-tools-and-services:

Writing Tools and Services 
==========================

When we want to write new analysis tools and services with Kieker and
Teetime, a lot of boilerplate code to

-  parse command line parameters,
-  read configuration files,
-  check parameters from both sources,
-  assemble a pipe and filter configuration,
-  provide shutdown hooks for services,
-  and execute everything.

Therefore, we created an ``AbstractService`` class providing the
necessary code and also ensuring certain basic functions. Alongside this
class, we envision a specific architecture. It comprises

1. a main class, e.g., ``MyToolMain``, extending ``AbstractService``,
2. a settings class for the parameters utilizing JCommander for all
   command line properties and Kieker configuration file settings
   processed by the Kieker configuration file parser. It can also
   include additional attributes that are not set by Jcommander and
   Kieker configuration file parser.
3. and a ``TeetimeConfiguration`` class, e.g.,
   ``MyTeetimeConfiguration``.

For smaller applications you can place all command line parameters in
your main class and use it as both settings and main class.

To support checking of parameters and configuration file options, we
provide a helper class ``ParameterEvaluation`` to further analyze
properties.

A typical main class looks like:

.. code-block:: java
  
  public MyToolMain extends AbstractService<MyTeetimeConfiguration,
     MyParameterSettings> {
     
    /*
     * This is a simple main class which does not need to be instantiated.
     */
    private MyToolMain() {
    }
    
    /*
     * main functions.
     *
     * @param args arguments are ignored
     */
    public static void main(final String[] args) {
       java.lang.System.exit(new MyToolMain().run("Application Title",
          "Logger Label", args, new MyParameterSettings()));
    }
    
    @Override
    protected MyTeetimeConfiguration createTeetimeConfiguration() throws
       ConfigurationException {
       /* do some preparatory stuff. */
       return new MyTeetimeConfiguration(...);
    }
    
    @Override
    protected boolean checkParameters(final JCommander commander) throws
       ConfigurationException {
       return true; // only if all parameters check out
    }
    
    @Override
    protected void shutdownService() {
       // empty, no special shutdown required
    }
    
    @Override
    protected File getConfigurationFile() {
       return this.settings.getConfigurationFile();
    }
    
    @Override
    protected boolean checkConfiguration(final Configuration configuration,
       final JCommander commander) {
       return true; // only if every configuration file option checks out
    }
  }

A example Teetime configuration looks like:

.. code-block:: java
  
  public class MyTeetimeConfiguration extends Configuration {
     
     public MyTeetimeConfiguration(...) {
        SomeStange stage1 = new SomeStage();
        SomeOtherStage stage2 = new SomeOtherStage();
        
        this.connectPorts(stage1.getOutputPort(), stage2.getInputPort());
     }
  }

For parameter settings, Kieker utilizes JCommander for command line
parameter processing. The parameters set by JCommander require an
annotation ``@Parameter``. In addition Kieker can utilize a standard
Java property file with name value pairs. In addition, Kieker comes
since 2.0.0 with a facility to process the Java properties and fill in
settings in the parameter settings object.

In the following, we will give a brief description how to use all three
facilities for parameters.

JCommander - Command Line Parameters
------------------------------------

The documentation of JCommander can be found `here <https://jcommander.org/>`_.
A typical setup with JCommander may look like:

.. code-block :: java
  
  public class Settings {
  
     @Parameter(names = { "-e", "--experiment-id" }, required = true,
         description = "The experiment id")
     private String experimentId;
     
     public String getExperimentId() {
         return this.experimentId;
     }
  }

The parameter is defined with a simple private attribute annoteded with
``@Parameter``. In this example we follow the GNU command line scheme.
That is, there is a long parameter name and a short version. It is
common to give more often used parameters also short parameter names,
whie others may only have a long name.

`The POSIX and GNU guidelines can be found
here <https://www.gnu.org/prep/standards/html_node/Command_002dLine-Interfaces.html>`_.

Typical names are:

===== ========= =====================================
Short Long      Description
===== ========= =====================================
-i    --input   Main input file or source
-o    --output  Main output file or source
-V    --version Version of the tool
-h    --help    Show help information
-v    --verbose Print more information about progress
===== ========= =====================================

Note these are suggestions and may be used differently in your context.

Using a Configuration File
--------------------------

The Kieker ``Configuration`` is an extension of the ``java.lang.Properties``
facility of Java. It allows to store name value pairs. The Kieker
Configuration is utilized on the monitoring side of Kieker in Java to
set up probes and logging. It can also be used for analysis tools.
In case a tool requires many settings and the command line may look
too complex, users would rather use configuration files to provide
parameters than specifying all of it on command line.

To be able to specify a configuration file, we need a command line
parameter. Therefore, we add one parameter to the settings object
(remember this can also just be the main class).

.. code-block :: java
  
  public class Settings {
  
     @Parameter(names = { "-c", "--configuration" }, required = true,
        description = "Configuration file.",
        converter = PathConverter.class)
     private Path configurationPath;
     
     public Path getConfigurationPath() {
        return this.configurationPath;
     }
  }

The specified converter automatically converts the command line string
into a ``Path`` object in the settings. The Kieker framework automatically
reads a configuration when a path is specified. To connect both you
have to implement the ``getConfigurationPath`` method in your main
class. In case you use the main class also for settings, this would
look like:

.. code-block :: java
  
  @Override
  public Path getConfigurationPath() {
     return this.configurationPath;
  }

In case you have a separate settings class:

.. code-block :: java
  
  @Override
  public Path getConfigurationPath() {
     return this.settings.configurationPath;
  }

The tool 
`behavior-analysis <https://github.com/kieker-monitoring/kieker/tree/master/kieker-tools/behavior-analysis/src/kieker/tools/behavior/analysis>`_ 
is a good example in code how to use a configuration file with a
separate settings class, while the 
`collector <https://github.com/kieker-monitoring/kieker/tree/master/kieker-tools/collector/src/kieker/tools/collector>'_
is a good example for a tool that uses the main class also to store the
settings.


Checking Configuration Options
------------------------------

To check and process configuration parameters from the Kieker
``Configuration``, you add checks to the ``checkConfiguration`` method
in the main class.

.. code-block :: java
  
    @Override
    protected boolean checkConfiguration(final Configuration configuration,
       final JCommander commander) {
       ...
       return true; // only if every configuration file option checks out
    }

The class `ParameterEvaluationUtils <https://github.com/kieker-monitoring/kieker/blob/master/kieker-tools/src/kieker/tools/common/ParameterEvaluationUtils.java>`_
can be used to check whether files and directories, are readable and
perform conversion operation.

Alternatively, you can use the new parameter parsing API of Kieker, as
described in the next section.


Parsing Configuration File Parameter
------------------------------------

Kieker supports a new API to parse configuration settings. In future,
it will become the default way to handle configuration files.

Analog to **JCommander**'s ``@Parameter`` annotation, Kieker supports
an ``@Setting`` annotation which can be used similarily to their
JCommander counterparts, but designed for configuration files. To use
this facility, you implement the ``checkConfiguration`` in your main
class method as follows:

.. code-block :: java
  
  @Override
  protected boolean checkConfiguration(final kieker.common.configuration.Configuration configuration,
     final JCommander commander) {
     final ConfigurationParser parser =
        new ConfigurationParser(ConfigurationKeys.PREFIX, this.settings);
  
     try {
        parser.parse(configuration);
     } catch (final ParameterException e) {
        this.logger.error(e.getLocalizedMessage());
        return false;
     }
     
     ...
  }
  
The configuration parser runs over the settings object and fills in all
settings from the Kieker configuration object. Like **JCommander** it
automatically converts string values to the correct settings type using
convertes from **JCommander** and validates their content with validators.

An example settings class from the ``behavior-analysis`` tool looks like

.. code-block :: java
  
  public final class BehaviorAnalysisSettings {
  
     @Parameter(names = { "-c", "--configuration" }, required = true, description = "Configuration file")
     private File configurationFile;
     
     @Setting(converter = PathConverter.class, validators = ParentPathValueValidator.class)
     private Path clusterOutputPath;
     
     ...
  }

in the example, you can see one parameter, which will be handled by
JCommander, and the ``clusterOutputPath`` which is handled by the
``ConfigurationParser``. The latter applies the ``PathConverter`` and
on validator to ensure the parent directory for the output file exists.

As it may be necessary to add more checks after the processing of
configuration settings, these can be applied below the try catch block.

**Note:** future versions of the Kieker ``AbstractService`` will
automatically run the ``ConfigurationParser``.

