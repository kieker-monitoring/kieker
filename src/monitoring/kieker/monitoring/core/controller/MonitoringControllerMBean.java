package kieker.monitoring.core.controller;

/**
 * Simply an additional wrapper around IMonitoring Controller.
 * All methods declared there, will be visible and accessible in
 * the MBeans-Server.
 * 
 * To activate this MBean, the property 'kieker.monitoring.MBean'
 * has to be set to 'true'.
 * 
 * @author Jan Waller
 */
public interface MonitoringControllerMBean extends IMonitoringController {}
