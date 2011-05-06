package periodicSigarExample;

import java.util.concurrent.TimeUnit;

import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.probe.sigar.ISigarSamplerFactory;
import kieker.monitoring.probe.sigar.SigarSamplerFactory;
import kieker.monitoring.probe.sigar.samplers.CPUsCombinedPercSampler;
import kieker.monitoring.probe.sigar.samplers.MemSwapUsageSampler;

public class MonitoringStarter {

    public static void main(final String[] args) throws InterruptedException {
        System.out.println("Monitoring CPU and Mem/Swap for 30 seconds in 5 seconds steps");
    	
    	final IMonitoringController monitoringController = 
    		MonitoringController.getInstance();
    	
		final ISigarSamplerFactory sigarFactory = SigarSamplerFactory.getInstance();
    	
    	final CPUsCombinedPercSampler cpuSampler = sigarFactory.createSensorCPUsCombinedPerc();
    	final MemSwapUsageSampler memSwapSampler = sigarFactory.createSensorMemSwapUsage();
    	
    	final long offset = 2; // start after 2 seconds
    	final long period = 5; // monitor every 5 seconds
    	
    	monitoringController.schedulePeriodicSampler(cpuSampler, offset, period, TimeUnit.SECONDS);
    	monitoringController.schedulePeriodicSampler(memSwapSampler, offset, period, TimeUnit.SECONDS);
    	
    	Thread.sleep(30000);
    	
        System.out.println("Terminating");
        monitoringController.terminateMonitoring();
    }
}
