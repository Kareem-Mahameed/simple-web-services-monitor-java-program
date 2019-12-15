package model;

import org.apache.log4j.PropertyConfigurator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.servicemonitoring.MonitorApp;
import com.servicemonitoring.model.ServiceListenerWrapper;
import com.servicemonitoring.model.ServiceWrapper;
import com.servicemonitoring.model.State;

import static org.junit.Assert.*;

import java.util.logging.Logger;

public class ServiceListenerWrapperTest {
	
	public static  Logger logger  ;
    
    public ServiceListenerWrapperTest() {
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
    

    



    
}
