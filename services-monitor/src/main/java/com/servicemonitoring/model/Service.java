package com.servicemonitoring.model;


/**
 * 
 *
 * Description: Model to Hold the service parameters!
 */
public class Service {
    private String name;
    private String host;
    private int port;   
    private int pollingInterval;       
    private int graceTime;      
    private long serviceOutageFromTime;
    private long serviceOutageToTime;
    private long lastQueryTime;
    private State state = State.NEW;
    private long serviceStateChangeTime;

    public Service(String host, int port, int pollingInterval, int graceTime) {
        this.name = host+":"+port;
        this.host = host;
        this.port = port;
        this.pollingInterval = pollingInterval;
        this.graceTime = graceTime;
        this.serviceOutageFromTime = 0;
		this.serviceOutageToTime = 0;
    }
    // second constructor with service outage times
    public Service(String host, int port,int pollingInterval, int graceTime, long serviceOutageFromTime, long serviceOutageToTime) {
    			this.name = host+":"+port;
    			this.host = host;
    			this.port = port;
    			this.pollingInterval = pollingInterval;
    			this.graceTime = graceTime;
    			this.serviceOutageFromTime = serviceOutageFromTime;
    			this.serviceOutageToTime = serviceOutageToTime;
    }

    public String getName() {
        return name;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getGraceTime() {
        return graceTime;
    }

    public long getLastQueryTime() {
        return lastQueryTime;
    }

    public void setLastQueryTime(long lastQueryTime) {
        this.lastQueryTime = lastQueryTime;
    }

    public State getState() {
        return state;
    }

    public int getPollingInterval() {
		return pollingInterval;
	}
	public long getServiceOutageFromTime() {
		return serviceOutageFromTime;
	}
	public long getServiceOutageToTime() {
		return serviceOutageToTime;
	}
	public long getServiceStateChangeTime() {
		return serviceStateChangeTime;
	}

	public void setServiceStateChangeTime(long serviceStateChangeTime) {
		this.serviceStateChangeTime = serviceStateChangeTime;
	}
	public void setState(State state) {
		this.state = state;
	}
}
