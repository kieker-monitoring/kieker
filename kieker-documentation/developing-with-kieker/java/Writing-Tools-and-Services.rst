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
2. a configuration class for the command line parameter utilizing
   JCommander for all command line properties (it may include additional
   parameter which is up to you),
3. and a ``TeetimeConfiguration`` class, e.g.,
   ``MyTeetimeConfiguration``.

For smaller applications you can place all command line parameters in
your main class and use it as both configuration and main class.

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
       return this.parameterConfiguration.getConfigurationFile();
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

