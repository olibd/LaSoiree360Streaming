import Exceptions.OSNotSupportedError;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by olivier on 2017-01-15.
 */
public class AVDriverFactoryTest {

    @Test
    public void testGetOrInitInstance() throws Exception, OSNotSupportedError {
        if(Computer.getInstance().isMac()) {
            assertEquals(AVDriverFactory.getOrInitInstance(Computer.OSType.MAC).getClass(), MacAVDriverFactory.class);
        } else if(Computer.getInstance().isWin()){
            assertEquals(AVDriverFactory.getOrInitInstance(Computer.OSType.WIN).getClass(), WinAVDriverFactory.class);
        } else {
            fail();
        }
    }

    @Test
    public void testGetInitInstance() throws Exception, OSNotSupportedError {
        if(Computer.getInstance().isMac()) {
            assertEquals(AVDriverFactory.getInstance().getClass(), MacAVDriverFactory.class);
        } else if(Computer.getInstance().isWin()){
            assertEquals(AVDriverFactory.getInstance().getClass(), WinAVDriverFactory.class);
        } else {
            fail();
        }
    }

    @Test
    public void GetAVDriver() throws Exception {
        assertNotNull(AVDriverFactory.getInstance().getAVDrivers());
        assertTrue(AVDriverFactory.getInstance().getAVDrivers().length >= 1);
    }
}