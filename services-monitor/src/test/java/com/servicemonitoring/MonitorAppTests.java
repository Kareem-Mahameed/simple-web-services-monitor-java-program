package com.servicemonitoring;

import static org.junit.Assert.assertEquals;

import java.util.logging.Logger;

import org.apache.log4j.PropertyConfigurator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.servicemonitoring.model.ServiceListenerWrapper;
import com.servicemonitoring.model.ServiceWrapper;

/**
 * @author Kareem
 * Description: unit tests Service Monitor
 */
public class MonitorAppTests  {
    public static final String SERVICE_ALPHA = "alpha";
    public static final String SERVICE_BETA = "beta";
    public static final String SERVICE_GAMA = "gama";
    public static final String SERVICE_DELTA = "delta";

    public static final String LOGGER_TAG = "unit test";
    public static  Logger logger  ;//= Logger.getLogger(ConnectionTesterNewTests.class.getSimpleName());//Logger.getLogger(LOGGER_TAG);
    private MonitorApp server;

    ServiceListenerWrapper echoListener ;

    @Before
    public void initServicesMonitor() throws Exception {
    	
    		PropertyConfigurator.configure("log4j.properties");  // initialize the logger 
        server = new MonitorApp();
        logger = Logger.getLogger(this.getClass().getName());
        echoListener=new  ServiceListenerWrapper("Kareem", 4,"kareem@kareem.com");
    }

    @After
    public void stopServicesMonitor() throws Exception {
        if (server != null) {
            server.serviceList.reset();
            server.stopMonitoring();
            server = null;
        }
    }
    
    @Test
    public void testServiceOutage() throws Exception {
        logger.info("testServiceOutage");
        System.out.println("testServiceOutage");
        ServiceWrapper serviceWrapper = new ServiceWrapper("www.globalrelay.com", 80,  1, 1);
        ServiceWrapper serviceWrapper2 = new ServiceWrapper("www.google.com", 80,  1, 1,System.currentTimeMillis()-1*1000
        		,System.currentTimeMillis()+10*1000);
        ServiceWrapper serviceWrapper3 = new ServiceWrapper("www.cnn.com", 80,  1, 1,System.currentTimeMillis()+5*1000
        		,System.currentTimeMillis()+15*1000);
        server.serviceList.addServiceWrapper(serviceWrapper);
        server.serviceList.addServiceWrapper(serviceWrapper2);
        server.serviceList.addServiceWrapper(serviceWrapper3);

        // register listener
        for (String name : server.serviceList.getServiceNames()) {
            ServiceWrapper sc = server.serviceList.getServiceWrapper(name);
            sc.addServiceListener(new  ServiceListenerWrapper("Kareem", 1,"kareem@kareem.com"));
            sc.addServiceListener(new  ServiceListenerWrapper("ToTah", 3,"ToTah@ToTah.com"));
            sc.addServiceListener(new  ServiceListenerWrapper("Zegleg", 8,"Zegleg@Zegleg.com"));
        }
        server.start();
        Thread.sleep(20*1000);
        server.stopMonitoring();
    }
    
    @Test
    public void testServiceAndListeners() throws Exception {
        logger.info("testServiceListeners");
        System.out.println("testServiceListeners");
        server.serviceList.addServiceWrapper(new ServiceWrapper("www.globalrelay.com", 80, 1, 3));
        ServiceWrapper serviceWrapper = new ServiceWrapper("www.google.com", 80,  1, 5);
        server.serviceList.addServiceWrapper(serviceWrapper);
        server.serviceList.addServiceWrapper(new ServiceWrapper("www.111toto.com", 334,  1, 3));
        server.serviceList.addServiceWrapper(new ServiceWrapper("www.cnn.com", 80, 1, 3));

        // register listener
        for (String name : server.serviceList.getServiceNames()) {
            ServiceWrapper sc = server.serviceList.getServiceWrapper(name);
            sc.addServiceListener(new  ServiceListenerWrapper("Kareem", 1,"kareem@kareem.com"));
            sc.addServiceListener(new  ServiceListenerWrapper("ToTah", 3,"ToTah@ToTah.com"));
            sc.addServiceListener(new  ServiceListenerWrapper("Zegleg", 8,"Zegleg@Zegleg.com"));
        }
        server.start();
        
        //server.serviceList.addServiceWrapper(new ServiceWrapper("www.111toto.com", 334, 1, 3));
        //server.serviceList.addServiceWrapper(new ServiceWrapper("www.cnn.com", 80, 1, 3));
        //server.serviceList.addServiceWrapper(new ServiceWrapper("www.222to22to.com", 3234, 1, 3));
        //server.serviceList.addServiceWrapper(new ServiceWrapper("www.bbc.com", 80, 1, 3));
        //server.serviceList.addServiceWrapper(new ServiceWrapper("www.333tot2ddo.com", 33334, 1, 3));
       // server.serviceList.addServiceWrapper(new ServiceWrapper("www.globalrelay.com", 80, 1, 3));
        //server.serviceList.addServiceWrapper(new ServiceWrapper("www.444toew22to.com", 333234, 1, 3));
        Thread.sleep(10*1000);
        server.stopMonitoring();
    }
    
    @Test
    public void test_4Services_1NotWorking() throws Exception {
        logger.info("test_3Good_1Bad_Connections");
        System.out.println("test_3Good_1Bad_Connections");
        ServiceWrapper serviceWrapper = new ServiceWrapper("www.google.com", 80,  1, 3);
        server.serviceList.addServiceWrapper(serviceWrapper);

        serviceWrapper = new ServiceWrapper("www.yahoo.com", 80,  1, 3);
        server.serviceList.addServiceWrapper(serviceWrapper);

        serviceWrapper = new ServiceWrapper("www.bing.com", 80,  1, 3);
        server.serviceList.addServiceWrapper(serviceWrapper);

        serviceWrapper = new ServiceWrapper("www.wrongaddress.address", 80, 1, 3);
        server.serviceList.addServiceWrapper(serviceWrapper);

        // register listener
        for (String name : server.serviceList.getServiceNames()) {
            ServiceWrapper sc = server.serviceList.getServiceWrapper(name);
            sc.addServiceListener(new  ServiceListenerWrapper("Kareem", 2,"kareem@kareem.com"));
            sc.addServiceListener(new  ServiceListenerWrapper("Scott", 4,"Scott@Scott.com"));
        }
        server.start();
        Thread.sleep(20*1000);
    }

    @Test
    public void testServiceGoDown() throws Exception {
        logger.info("testConnectionsSwitch");
        System.out.println("testConnectionsSwitch");
        ServiceWrapper serviceWrapper = new ServiceWrapper("www.google.com", 80,  1, 3);
        server.serviceList.addServiceWrapper(serviceWrapper);

        serviceWrapper = new ServiceWrapper("www.yahoo.com", 80,  1, 3);
        server.serviceList.addServiceWrapper(serviceWrapper);

        serviceWrapper = new ServiceWrapper("www.bing.com", 80,  1, 3);
        server.serviceList.addServiceWrapper(serviceWrapper);

        serviceWrapper = new ServiceWrapper("www.meta.ua", 80,  1, 3);
        server.serviceList.addServiceWrapper(serviceWrapper);

        // register listener
        for (String name : server.serviceList.getServiceNames()) {
            ServiceWrapper sc = server.serviceList.getServiceWrapper(name);
            sc.addServiceListener(new  ServiceListenerWrapper("Kareem", 2,"Kareem@kareem.com"));
            sc.addServiceListener(new  ServiceListenerWrapper("Ali", 1,"Ali@Ali.com"));
            sc.addServiceListener(new  ServiceListenerWrapper("Totah", 4,"Totah@Totah.com"));
        }
        server.start();
        Thread.sleep(20*1000);

        serviceWrapper = server.serviceList.getServiceWrapper("www.bing.com:80");
        serviceWrapper.getService().setHost("www.wrongaddress.address");
        serviceWrapper.getService().setPort(80);
        server.serviceList.addServiceWrapper(serviceWrapper);

        Thread.sleep(20*1000);
    }




}
