package com.servicemonitoring;

import org.apache.log4j.Logger;

import com.servicemonitoring.model.ServiceWrapper;
import com.servicemonitoring.model.State;
import com.util.Util;

import java.io.IOException;


/**
 * @author Kareem
 *         Description: Worker connects to service by Socket and updates timestamp of the last successful attempt
 *         In case if waiting threshold is exceeded - an event of ServerUp or ServerDown is fired
 */
public class ServicePollWorker implements Runnable {
    protected static Logger logger=Logger.getLogger(ServicePollWorker.class.getSimpleName());
    protected ServiceWrapper serviceWrapper;
	
    public ServicePollWorker(ServiceWrapper serviceWrapper) throws IOException {
        this.serviceWrapper = serviceWrapper;
    }
    
    private void serviceUp() {
        logger.debug("Service: "+serviceWrapper.getService().getHost()+ ":" + serviceWrapper.getService().getPort() + " IS UP " );
        System.out.println("Service: "+serviceWrapper.getService().getHost()+ ":" + serviceWrapper.getService().getPort() + " IS UP ");

        long timestamp = System.currentTimeMillis();
        serviceWrapper.getService().setLastQueryTime(timestamp);
        serviceWrapper.serviceStatusUP(timestamp);       
        serviceWrapper.notifyServiceListeners(timestamp,State.UP);
    }

    private void serviceDown() {
    		logger.debug("Service: "+serviceWrapper.getService().getHost()+ ":" + serviceWrapper.getService().getPort() + "  IS DOWN " );
        System.out.println("Service: "+serviceWrapper.getService().getHost()+ ":" + serviceWrapper.getService().getPort() + " IS DOWN ");
        long timestamp = System.currentTimeMillis();
        serviceWrapper.getService().setLastQueryTime(timestamp);
        boolean inGracePeriod = serviceWrapper.serviceStatusDOWN(timestamp);
        System.out.println("inGracePeriod : "+inGracePeriod);
        logger.info("inGracePeriod : "+inGracePeriod);
        if (!inGracePeriod) { // not in grace period
        		serviceWrapper.notifyServiceListeners(timestamp,State.DOWN);
        }
        else
        {
        	System.out.println(String.format("Service %s down in grace period: %s, No notification will be sent to listeners",
        			serviceWrapper.getService().getName()
        			, serviceWrapper.getService().getGraceTime()));
        logger.info(String.format("Service %s down in grace period: %s, No notification will be sent to listeners",
        			serviceWrapper.getService().getName()
        			, serviceWrapper.getService().getGraceTime()));
        }
    }

    public void run() {
    		boolean servicStatus;
    		try {
				 servicStatus=Util.pollService(serviceWrapper.getService().getHost(),serviceWrapper.getService().getPort());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				servicStatus = false;
			}
    		if (servicStatus)
    			serviceUp();
    		else
    			serviceDown();
    }


}
