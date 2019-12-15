package com.servicemonitoring;

import java.util.HashMap;
import java.util.Map;

import com.servicemonitoring.model.ServiceWrapper;

/**
 * @author Kareem
 * Description: Singleton hold map of services and their respective  for the MonitorApp 
 * they can be saved as models in DB and query them
 */
public class ServiceList {
    private Map<String, ServiceWrapper> serviceList = new HashMap<String, ServiceWrapper>();
    private static final ServiceList instance = new ServiceList();

    private ServiceList() {}

    public static ServiceList getInstance() {
        return instance;
    }

    public ServiceWrapper getServiceWrapper(String name) {
        return serviceList.get(name);
    }

    public void addServiceWrapper(ServiceWrapper serviceWrapper) {
        serviceList.put(serviceWrapper.getService().getName(), serviceWrapper);
    }

    public void reset() {
        serviceList.clear();
    }

    public Iterable<String> getServiceNames() {
        return serviceList.keySet();
    }
}
