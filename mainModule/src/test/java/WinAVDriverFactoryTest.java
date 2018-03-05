import Exceptions.OSNotSupportedError;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by olivier on 2017-01-16.
 */
public class WinAVDriverFactoryTest {

    private WinAVDriverFactory factory;

    @Before
    public void setUp() throws Exception, OSNotSupportedError {
        //only run test if the workstation is a windows machine
        Assume.assumeTrue(Computer.getInstance().isWin());
        factory = new WinAVDriverFactory();
    }

    @Test
    public void getAVDrivers() throws Exception {

        AVDriver[] drivers = factory.getAVDrivers();
        assertNotNull(drivers);
        assertEquals(drivers.length, 1);
        assertEquals(drivers[0].getClass(), DShowDriver.class);
    }

}