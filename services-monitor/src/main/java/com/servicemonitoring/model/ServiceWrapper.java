package com.servicemonitoring.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 *
 * Description: Hold the service parameters!
 */
public class ServiceWrapper {
    private Service service;

	private List<ServiceListenerWrapper> serviceListenerWrappers = new ArrayList<ServiceListenerWrapper>();

    public ServiceWrapper(String host, int port,  int pollingInterval, int graceTime) {
    		service=new Service( host,  port,  pollingInterval,  graceTime);
    }
    // second constructor with service outage times
    public ServiceWrapper(String host, int port,  int pollingInterval, int graceTime, long serviceOutageFromTime, long serviceOutageToTime) {
    		service=new Service( host,  port,  pollingInterval,  graceTime,serviceOutageFromTime,  serviceOutageToTime);

    }
    public Service getService() {
		return service;
	}

    
     //As requested  at the end of the assignment If the grace-time is less than the polling frequency time, the monitor should schedule extra checks of the service.
     //this mean if #graceTime is less than #pollingInterval then #graceTime is returned
     //
    public synchronized int getPollingInterval() {
        if (service.getGraceTime() < service.getPollingInterval()) {
            return service.getGraceTime();
        } else {
            return service.getPollingInterval();
        }
    }

    synchronized public boolean serviceOutageStatus(long timestamp) {
    		if (timestamp >= service.getServiceOutageFromTime() && timestamp <= service.getServiceOutageToTime())
    			return true;
    		return false;
        
    }

    synchronized public void addServiceListener(ServiceListenerWrapper listener) {
        if (listener != null) {
        	serviceListenerWrappers.add(listener);
        }
    }

    /**
     * Description : make service status as UP and record  state change timestamp in serviceStateChangeTime
     * If service status already UP do nothing   
     * @param timestamp of the checking the service status 
     */
    synchronized public void serviceStatusUP(long timestamp) {
        switch (service.getState()) {
            case NEW:
            	service.setState(State.UP); 
            	service.setServiceStateChangeTime(timestamp);
             break;

            case DOWN:
            			service.setState(State.UP); 
            			service.setServiceStateChangeTime(timestamp);
                    break;

            case UP:
            default:
            	break;
        }
    }
    /**
     * Description : make service status as DOWN and record  state change timestamp in serviceStateChangeTime
     * If service status already DOWN do nothing  
     * @param timestamp of checking the service status 
     * @return false only if the service outage exceeded the grace time so service workers will notify service's listeners
     */
    synchronized public boolean serviceStatusDOWN(long timestamp) {
        switch (service.getState()) {
            case NEW:
            		service.setState(State.DOWN);
                service.setServiceStateChangeTime(timestamp);
                break;

            case UP:
            	service.setState(State.DOWN);
                    service.setServiceStateChangeTime(timestamp);
                    break;

            case DOWN:
            default:
            			break;
        }
        if ((service.getServiceStateChangeTime() + service.getGraceTime() * 1000 )>= timestamp) {
        	System.out.println("stateTimestamp "+service.getServiceStateChangeTime()+" , timestamp: "+timestamp
        			+", differense: "+((service.getServiceStateChangeTime() + service.getGraceTime() * 1000 )- timestamp));
        	return true; // in the grace period, no notification will be sent to any listener 
        }
        else
        	return false;
    }
    
    synchronized public void notifyServiceListeners(long timestamp,State state) {
        for (ServiceListenerWrapper listener : serviceListenerWrappers) {
            listener.notifyListener(this.getService(), timestamp,state);
        }
    }

}
