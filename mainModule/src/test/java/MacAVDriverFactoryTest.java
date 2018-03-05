import Drivers.*;
import Platform.Computer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by olivier on 2017-01-16.
 */
public class MacAVDriverFactoryTest {

    private MacAVDriverFactory factory;
    @Before
    public void setUp() throws Exception{
        org.junit.Assume.assumeTrue(Computer.getInstance().isMac());
        factory = new MacAVDriverFactory();
    }

    @Test
    public void getAVDrivers() throws Exception {

        AVDriver[] drivers = factory.getAVDrivers();
        assertNotNull(drivers);
        assertEquals(drivers.length, 3);
        assertEquals(drivers[0].getClass(), AVFoundationAudioDriver.class);
        assertEquals(drivers[1].getClass(), AVFoundationVideoDriver.class);
        assertEquals(drivers[2].getClass(), DeckLinkDriver.class);
    }

}
