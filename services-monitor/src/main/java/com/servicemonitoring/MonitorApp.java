package com.servicemonitoring;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.servicemonitoring.model.ServiceListenerWrapper;
import com.servicemonitoring.model.ServiceWrapper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 
 *

 */

public class MonitorApp extends Thread {
    public static final int MonitorApp_MINIMUM_INTERVAL = 1000;
    protected boolean monitoringStatus = true;
    protected ServiceList serviceList = ServiceList.getInstance();

    public static Logger logger=Logger.getLogger(MonitorApp.class.getSimpleName());
    
    // It's unbounded, but suitable for testing a short-lived asynchronous tasks like our example
    protected ExecutorService cachedThreadPool = Executors.newCachedThreadPool();  
    /**
     * @author Kareem
     *for production like scale so we limit the number of threads and and extra tasks will be queued 
     *or we can use ThreadPoolExecutor()  for full control
     *protected ExecutorService ProductionFixedThreadPool =Executors.newFixedThreadPool(8);
     *
     */ 
     
    public MonitorApp() {
        logger.info("Started MonitorApp "+System.currentTimeMillis());
        System.out.println("Started MonitorApp  "+System.currentTimeMillis());
    }
    
    public void run() {
            while (monitoringStatus) {
                try {
                		long timestamp = System.currentTimeMillis();
                    for (String serviceName : serviceList.getServiceNames()) {
                        ServiceWrapper serviceWrapper = serviceList.getServiceWrapper(serviceName);
                        // if service not on Outage time and pooling interval passed
                        if (!serviceWrapper.serviceOutageStatus(timestamp) 
                        		&& (timestamp > serviceWrapper.getService().getLastQueryTime() + serviceWrapper.getPollingInterval() * 1000))
                        {
                        		// ProductionFixedThreadPool.submit(new ServicePollWorker(serviceName));  // for production scale
                            cachedThreadPool.submit(new ServicePollWorker(serviceWrapper));
                            //cachedThreadPoo.execute(new ServicePollWorker(serviceName));
                        }                     
                    }
                    Thread.sleep(MonitorApp_MINIMUM_INTERVAL);
                } catch (InterruptedException e) {
                    logger.error("Exception in `monitorApp main Thread", e);
                    System.out.println("Exception on `monitorApp main Thread"+ e);
                }catch (Exception e) {
                    logger.error("Exception in servicePollWorker Thread", e);
                    System.out.println("xception in servicePollWorker Thread"+ e);
                }
            }
        logger.info("MonitorApp Stopped "+System.currentTimeMillis());
        System.out.println("MonitorApp Stopped "+System.currentTimeMillis());;
    }

    public void stopMonitoring() {
    		monitoringStatus = false;
    }

    public static void main(String argv[]) throws Exception {

    		PropertyConfigurator.configure("log4j.properties");    
    		MonitorApp server = new MonitorApp();
        
        server.serviceList.addServiceWrapper(new ServiceWrapper("www.cnn.com", 80, 1, 3));
        server.serviceList.addServiceWrapper(new ServiceWrapper( "www.bbc.com", 80,  1, 6));
        server.serviceList.addServiceWrapper(new ServiceWrapper( "www.globalrelay.com", 80,  1, 1));
        
        server.serviceList.addServiceWrapper(new ServiceWrapper("www.111toto.com", 334,  1, 3));
        server.serviceList.addServiceWrapper(new ServiceWrapper("www.yahoo.com", 80,  1, 3));
        server.serviceList.addServiceWrapper(new ServiceWrapper("www.222to22to.com", 3234,  1, 3));
        server.serviceList.addServiceWrapper(new ServiceWrapper("www.google.com", 80,  1, 3));
        server.serviceList.addServiceWrapper(new ServiceWrapper("www.333tot2ddo.com", 33334,  1, 3));
        server.serviceList.addServiceWrapper(new ServiceWrapper("www.amazon.com", 80,  1, 3));
        server.serviceList.addServiceWrapper(new ServiceWrapper("www.444toew22to.com", 333234,  1, 3));
        
        for (String name : server.serviceList.getServiceNames()) {
            ServiceWrapper sw = server.serviceList.getServiceWrapper(name);
            sw.addServiceListener(new  ServiceListenerWrapper("Kareem", 2,"kareem@kareem.com"));
            sw.addServiceListener(new  ServiceListenerWrapper("Scott", 1,"Scott@Scott.com"));
            sw.addServiceListener(new  ServiceListenerWrapper("Totah", 7,"Totah@Totah.com"));
            
            // logging the services and its listeners 
            MonitorApp.logger.info(String.format("Monitor service:  %s, with QueryInterval: %s, and GraceInterval: %s,"
            		+ " Listeners: (Kareem, 2), (Scott, 1), (Totah, 7) "
            		, sw.getService().getName()
            		,sw.getPollingInterval()
            		,sw.getService().getGraceTime()));
        }



        server.start();
        Thread.sleep(20*1000);
        
        
    }
}
