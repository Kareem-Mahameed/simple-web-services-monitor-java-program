/**
 * 
 */
package com.servicemonitoring.model;


/**
 * @author Kareem
 *
 */
public class ServiceListener {
	private String listenerName;
	private String listenerEmail;
	private int listenerPollingInterval;       
	private long lastNotificationTimestamp;
	private State lastNotifiedState;

	public  ServiceListener(String listenerName,int listenerPollingInterval, String listenerEmail)
	{
		 this.listenerName=listenerName;
		 this.listenerEmail=listenerEmail;
		 this.listenerPollingInterval=listenerPollingInterval;
		 this.lastNotificationTimestamp=0;
		 this.lastNotifiedState=State.NEW;
	}
	
	public String getListenerName() {
		return listenerName;
	}

	public String getListenerEmail() {
		return listenerEmail;
	}

	public int getListenerPollingInterval() {
		return listenerPollingInterval;
	}

	public long getLastNotificationTimestamp() {
		return lastNotificationTimestamp;
	}

	public State getLastNotifiedState() {
		return lastNotifiedState;
	}

	public void setLastNotificationTimestamp(long lastNotificationTimestamp) {
		this.lastNotificationTimestamp = lastNotificationTimestamp;
	}

	public void setLastNotifiedState(State lastNotifiedState) {
		this.lastNotifiedState = lastNotifiedState;
	}


	}


