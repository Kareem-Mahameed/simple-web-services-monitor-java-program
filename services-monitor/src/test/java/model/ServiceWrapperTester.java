package model;

import org.apache.log4j.PropertyConfigurator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.servicemonitoring.model.ServiceWrapper;
import com.servicemonitoring.model.State;

import static org.junit.Assert.*;

import java.util.logging.Logger;

public class ServiceWrapperTester {
	
	public static  Logger logger  ;
    
    public ServiceWrapperTester() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    		PropertyConfigurator.configure("log4j.properties");  // initialize the logger 
        logger = Logger.getLogger(this.getClass().getName());
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getServiceWrapper
     */
    @Test
    public void testServiceWrapper() {
    		ServiceWrapper serviceWrapper = new ServiceWrapper("www.yahoo.com", 80,  1, 3);
    		
    		assertEquals("serviceWrapper.getService().getName() dont return expected value www.yahoo.com:8: ",serviceWrapper.getService().getName(), "www.yahoo.com:80");
    		assertEquals("serviceWrapper.getService().getHost() dont return expected value \"www.yahoo.com\": ",serviceWrapper.getService().getHost(), "www.yahoo.com");
    		assertEquals("serviceWrapper.getService().getPort() dont return expected value 80: ",serviceWrapper.getService().getPort(), 80);
    }
    
    @Test
    public void testGraceTimesmallerThanPoolInterval() throws Exception {
        ServiceWrapper serviceWrapper = new ServiceWrapper("www.google.com", 80,  10, 5);
        assertEquals("serviceWrapper.getQueryInterval() dont equal expected value 5: ",serviceWrapper.getPollingInterval(), 5);
    }
    
    @Test
    public void testServiceOutage() throws Exception {
        logger.info("testServiceOutage");
        System.out.println("testServiceOutage");
        long timestamp = System.currentTimeMillis();
        ServiceWrapper serviceWrapper = new ServiceWrapper("www.globalrelay.com", 80,  1, 1);
        ServiceWrapper serviceWrapper2 = new ServiceWrapper("www.google.com", 80,  1, 1,timestamp-1*1000
        		,System.currentTimeMillis()+10*1000);
        ServiceWrapper serviceWrapper3 = new ServiceWrapper("www.cnn.com", 80,  1, 1,timestamp+5*1000
        		,System.currentTimeMillis()+15*1000);
        //long timestamp = System.currentTimeMillis();
        assertEquals("serviceWrapper.serviceOutageStatus(timestamp) dont return expected value false: ",serviceWrapper.serviceOutageStatus(timestamp), false);
        assertEquals("serviceWrapper.serviceOutageStatus(timestamp) dont return expected value true: ",serviceWrapper2.serviceOutageStatus(timestamp), true);
        assertEquals("serviceWrapper.serviceOutageStatus(timestamp) dont return expected value false: ",serviceWrapper3.serviceOutageStatus(timestamp), false);
		
        Thread.sleep(6*1000);
        timestamp = System.currentTimeMillis();
        assertEquals("serviceWrapper.serviceOutageStatus(timestamp) dont return expected value false: ",serviceWrapper.serviceOutageStatus(timestamp), false);
        assertEquals("serviceWrapper.serviceOutageStatus(timestamp) dont return expected value true: ",serviceWrapper2.serviceOutageStatus(timestamp), true);
        assertEquals("serviceWrapper.serviceOutageStatus(timestamp) dont return expected value false: ",serviceWrapper3.serviceOutageStatus(timestamp), true);
		
        Thread.sleep(5*1000);
        timestamp = System.currentTimeMillis();
        assertEquals("serviceWrapper.serviceOutageStatus(timestamp) dont return expected value false: ",serviceWrapper.serviceOutageStatus(timestamp), false);
        assertEquals("serviceWrapper.serviceOutageStatus(timestamp) dont return expected value true: ",serviceWrapper2.serviceOutageStatus(timestamp), false);
        assertEquals("serviceWrapper.serviceOutageStatus(timestamp) dont return expected value false: ",serviceWrapper3.serviceOutageStatus(timestamp), true);
		
        Thread.sleep(5*1000);
        timestamp = System.currentTimeMillis();
        assertEquals("serviceWrapper.serviceOutageStatus(timestamp) dont return expected value false: ",serviceWrapper.serviceOutageStatus(timestamp), false);
        assertEquals("serviceWrapper.serviceOutageStatus(timestamp) dont return expected value true: ",serviceWrapper2.serviceOutageStatus(timestamp), false);
        assertEquals("serviceWrapper.serviceOutageStatus(timestamp) dont return expected value false: ",serviceWrapper3.serviceOutageStatus(timestamp), false);	    
    }
    
    
    @Test
    public void testServiceChangeStatus() throws InterruptedException {
    		ServiceWrapper serviceWrapper = new ServiceWrapper("www.yahoo.com", 80,  1, 3);
    		
    		assertEquals("serviceWrapper.getService().getState() dont return expected value NEW: ",serviceWrapper.getService().getState(), State.NEW);
    		long timestamp = System.currentTimeMillis();
    		serviceWrapper.serviceStatusUP(timestamp);
    		assertEquals("serviceWrapper.getService().getState() dont return expected value UP ",serviceWrapper.getService().getState(), State.UP);
    		assertEquals("serviceWrapper.getService().getServiceStateChangeTime( dont return expected value: "+timestamp,serviceWrapper.getService().getServiceStateChangeTime() ,timestamp);
    		
    		timestamp = System.currentTimeMillis();
    		boolean gracePeriod=serviceWrapper.serviceStatusDOWN(timestamp);
    		assertEquals("serviceWrapper.getService().getState() dont return expected value DOWN: ",serviceWrapper.getService().getState(), State.DOWN);
    		assertEquals("serviceWrapper.getService().getServiceStateChangeTime( dont return expected value: "+timestamp,serviceWrapper.getService().getServiceStateChangeTime() ,timestamp);
    		assertEquals("gracePeriod dont return expected value true ",gracePeriod, true);
    		
    		Thread.sleep(1*1000);
    		long timestamp2 = System.currentTimeMillis();
    		 gracePeriod=serviceWrapper.serviceStatusDOWN(timestamp2);
    		assertEquals("serviceWrapper.getService().getState() dont return expected value DOWN: ",serviceWrapper.getService().getState(), State.DOWN);
    		assertEquals("serviceWrapper.getService().getServiceStateChangeTime( dont return expected value: "+timestamp,serviceWrapper.getService().getServiceStateChangeTime() ,timestamp);
    		assertEquals("gracePeriod dont return expected value true ",gracePeriod, true);
    		
    		Thread.sleep(3*1000);
    		 timestamp2 = System.currentTimeMillis();
    		 gracePeriod=serviceWrapper.serviceStatusDOWN(timestamp2);
    		assertEquals("serviceWrapper.getService().getState() dont return expected value DOWN: ",serviceWrapper.getService().getState(), State.DOWN);
    		assertEquals("serviceWrapper.getService().getServiceStateChangeTime( dont return expected value: "+timestamp,serviceWrapper.getService().getServiceStateChangeTime() ,timestamp);
    		assertEquals("gracePeriod dont return expected value false ",gracePeriod, false);
    		
    }


    
}
