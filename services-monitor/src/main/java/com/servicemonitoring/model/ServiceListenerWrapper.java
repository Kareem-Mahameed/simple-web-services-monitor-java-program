/**
 * 
 */
package com.servicemonitoring.model;

import org.apache.log4j.Logger;

import com.util.Util;

/**
 * @author Kareem
 *
 */
public class ServiceListenerWrapper  {
	private ServiceListener serviceListener;

	public static Logger logger=Logger.getLogger(ServiceListenerWrapper.class.getSimpleName());

	/* (non-Javadoc)
	 * @see com.servicemonitoring.ServiceListener#notifyListener(ServiceWrapper, long)
	 */
	public  ServiceListenerWrapper(String listenerName,int listenerPollingInterval, String listenerEmail)
	{
		serviceListener=new ServiceListener( listenerName, listenerPollingInterval,  listenerEmail);

	}
    /**
     * Description : notify the listener for service status change
     * its send notification only if the new status is not equal to lastNotifiedState status and listenerPollingInterval exceeded
     * function can also configured to send email or make asynchronous call to listener callback API 
     * @param timestamp of the checking the service status 
     * @param state the service new status
     */
	public void notifyListener(Service service, long timestamp, State state) {
		
		if (serviceListener.getLastNotifiedState()!=state && timestamp > (serviceListener.getLastNotificationTimestamp() + serviceListener.getListenerPollingInterval() * 1000))
		{					
			System.out.println("Notifing "+serviceListener.getListenerName()+" that service: "+service.getName()
					+"  is "+state+" at "+String.valueOf(timestamp)+", last notification sent since: "
					+(timestamp-serviceListener.getLastNotificationTimestamp())/1000+" seconds" );
			
			logger.info("Notifing "+serviceListener.getListenerName()+" that service: "+service.getName()
			+"  is "+state+" at "+String.valueOf(timestamp)+", last notification sent since: "
			+(timestamp-serviceListener.getLastNotificationTimestamp())/1000+" seconds");
			
			serviceListener.setLastNotificationTimestamp (timestamp);
			serviceListener.setLastNotifiedState(state);
			// TODO Send Email notification
			Util.sendNotificationEmail(serviceListener.getListenerEmail(),  service.getHost(), service.getPort());
			// TODO make an asynchronous API call for Listener callback API
		}
	}


	}


