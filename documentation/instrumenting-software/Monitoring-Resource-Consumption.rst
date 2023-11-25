.. _instrumenting-software-resource-consumption:

Monitoring Resource Consumption
===============================

Kieker comes with a wide range of samplers to collect resource
consumption. Currently, we use OSHI for these samplers.

.. note::
  
  Please note that there might be new OSHI feature available not listed
  here.

Setting Up Samplers
-------------------

Samplers can be added to software manually, via AspectJ probes and as
listeners in Java servlet setups. Of course it is possible to use other
means of injection. Feel free to create a probe based on the existing
sampler API. To create your own probe, you have to create a proper
injection setup and use the OshiSamplerFactory to create samplers.

Manual Setup
~~~~~~~~~~~~

Sampler must be run in a separate thread otherwise they would block
execution. Kieker comes, therefore, with the ability to schedule samples
within the controller.

.. code-block:: Java
  
  private static final IMonitoringController CTRL =
      MonitoringController.getInstance();
  
  final IOshiSamplerFactory oshiFactory = OshiSamplerFactory.INSTANCE;
  
  ISampler sampler = oshiFactory.createSensorCPUsDetailedPerc();
  
  CTRL.schedulePeriodicSampler(sampler, 0, 100, TimeUnit.MILLISECONDS);
 

Available Samplers
------------------

Currently, the following sampler are available:

**CPUsCombinedPercSampler.java**

Collects the combined utilization of each CPU in a system (user, system,
nice and wait). Each value is stored in a ``ResourceUtilizationRecord``

.. code-block:: irl
  
  entity ResourceUtilizationRecord {
    long timestamp = 0
    
    /* Name of the host, the resource belongs to. */
    string hostname = ""
    
    /* Name of the resource. */
    string resourceName = ""
    
    /* Value of utilization. The value should be in the range [0,1] */
    double utilization = 0.0
  }
 

**CPUsDetailedPercSampler.java**

Collects detailed utilization values differentiated by user, system,
nice and wait for each CPU in the system.

.. code-block:: irl
  
  entity CPUUtilizationRecord {
  
    /* Date/time of measurement. The value should be interpreted as the
     * number of nano-seconds elapsed since Jan 1st, 1970 UTC. */
    long timestamp = 0
    
    /* Name of the host, the resource belongs to. */
    string hostname = ''
    
    /* Identifier which is unique for a CPU on a given host. */
    string cpuID = ''
    
    /* Fraction of time during which the CPU was used for user-space
     * processes. The value should be in the range [0,1] */
    double user = 0.0
    
    /* Fraction of time during which the CPU was used by the kernel. The
     * value should be in the range [0,1] */
    double system = 0.0
    
    /* Fraction of CPU wait time. The value should be in the range [0,1] */
    double wait = 0.0
    
    /* Fraction of time during which the CPU was used by user space
     * processes with a high nice value. The value should be in the
     * range [0,1] */
    double nice = 0.0
    
    /* Fraction of time during which the CPU was used by user space
     * processes with a high nice value. The value should be in the
     * range [0,1] */
    double irq = 0.0
    
    /* Fraction of time during which the CPU was utilized. Typically,
     * this is the sum of {@link #user}, {@link #system}, {@link #wait},
     * and {@link #nice}. The value should be in the range [0,1] */
    double totalUtilization = 0.0
    
    /* Fraction of time during which the CPU was idle. The value should
     * be in the range [0,1] */
    double idle = 0.0
  }

**DiskUsageSampler.java**

Collect persistent memory (disc) usage.

.. code-block:: irl
  
  entity DiskUsageRecord {
    long timestamp = 0
    string hostname = ""
    string deviceName = ""
    double queue = 0.0
    double readBytesPerSecond = 0.0
    double readsPerSecond = 0.0
    double serviceTime = 0.0
    double writeBytesPerSecond = 0.0
    double writesPerSecond = 0.0
  }

**LoadAverageSampler.java**

Collects load averages of the system.

.. code-block:: irl
  
  entity LoadAverageRecord {
    long timestamp = 0
    string hostname = ""
    double oneMinLoadAverage = 0.0
    double fiveMinLoadAverage = 0.0
    double fifteenMinLoadAverage = 0.0
  }

**MemSwapUsageSampler.java**

Collect information on memory and swap space.

.. code-block:: irl
  
  entity MemSwapUsageRecord {
    long timestamp = 0
    string hostname = ""
    long memTotal = 0
    long memUsed = 0
    long memFree = 0
    long swapTotal = 0
    long swapUsed = 0
    long swapFree = 0
  }


**NetworkUtilizationSampler.java**

.. code-block:: irl
  
  entity NetworkUtilizationRecord {
    long timestamp = 0
    string hostname = ""
    string interfaceName = ""
    long speed = 0
    double txBytesPerSecond = 0.0
    double txCarrierPerSecond = 0.0
    double txCollisionsPerSecond = 0.0
    double txDroppedPerSecond = 0.0
    double txErrorsPerSecond = 0.0
    double txOverrunsPerSecond = 0.0
    double txPacketsPerSecond = 0.0
    double rxBytesPerSecond = 0.0
    double rxDroppedPerSecond = 0.0
    double rxErrorsPerSecond = 0.0
    double rxFramePerSecond = 0.0
    double rxOverrunsPerSecond = 0.0
    double rxPacketsPerSecond = 0.0
  }

