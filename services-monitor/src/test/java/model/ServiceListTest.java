
package model;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.servicemonitoring.ServiceList;
import com.servicemonitoring.model.ServiceWrapper;

import static org.junit.Assert.*;

public class ServiceListTest {
    
    public ServiceListTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    	
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getServiceWrapper
     */
    @Test
    public void testgetServiceWrapper() {
    		ServiceList serviceList = ServiceList.getInstance();
    		ServiceWrapper serviceWrapper = new ServiceWrapper("www.yahoo.com", 80,  1, 3);
    		serviceList.addServiceWrapper(serviceWrapper);
    		
    		assertEquals("serviceWrapper.getService().getPort() dont return expected value 80: ",serviceList.getServiceWrapper("www.yahoo.com:80").getService().getPort(), 80);
    		
        //assertTrue(serviceWrapper.getService().getName() == "www.yahoo.com:80");
        //assertTrue(serviceWrapper.getService().getHost() == "www.yahoo.com");
        //assertTrue(serviceWrapper.getService().getPort() == 80);
        //assertTrue(serviceList.getServiceWrapper("www.yahoo.com:80").getService().getName() == "www.yahoo.com:80");
       
    }


    
}
