import Drivers.AVDriver;
import Exceptions.OSNotSupportedError;
import Platform.Computer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.*;

/**
 * Created by olivier on 16-11-06.
 */
public class ComputerTest {

    private Computer comp;

    @Before
    public void setUp() throws Exception, OSNotSupportedError {
        comp = Computer.getInstance();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void getInstance() throws Exception {
        assertEquals(comp.getClass(), Computer.class);
    }

    @Test
    public void isMac() throws Exception {
        if(System.getProperty("os.name").startsWith("Mac"))
            assertTrue(comp.isMac());
        else
            assertFalse(comp.isMac());

    }

    @Test
    public void isWin() throws Exception {
        if(System.getProperty("os.name").startsWith("Win"))
            assertTrue(comp.isWin());
        else
            assertFalse(comp.isWin());
    }

    @Test
    public void getOSType() throws Exception {
        if(comp.isWin())
            assertEquals(Computer.OSType.WIN, comp.getOSType());
        else if (comp.isMac())
            assertEquals(Computer.OSType.MAC, comp.getOSType());
    }

    @Test
    public void getAvDriversTest() throws Exception {
        assertNotNull(comp.getAvDrivers());
        assertTrue(comp.getAvDrivers().length >= 1);
    }

    @Test
    public void updateDriverDevicesTest() throws Exception {

        AVDriver[] drivers = comp.updateDriverDevices();

        //Make sure that the returned drivers array is populated and not null
        assertNotNull(drivers);
        assertTrue(drivers.length >= 1);

        //make sure that the devices lists for each driver is populated and not null
        for(AVDriver driver : drivers){
            LinkedList audioDevices = driver.getAudioDevices();
            assertNotNull(audioDevices);

            LinkedList videoDevices = driver.getAudioDevices();
            assertNotNull(videoDevices);
        }
    }
}