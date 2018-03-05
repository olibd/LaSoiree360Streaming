import Devices.DeckLinkDevice;
import Exceptions.OSNotSupportedError;
import Platform.Computer;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.*;

/**
 * Created by olivier on 2017-01-17.
 */
public class DeckLinkDeviceTest extends AVDeviceTest {

    DeckLinkDevice dDevice;

    @Before
    public void setUp() throws Exception {
        try {
            org.junit.Assume.assumeTrue(Computer.getInstance().isMac());
        } catch (OSNotSupportedError e) {
            fail();
        }
        dDevice = new DeckLinkDevice("dummyDevice");
        device = dDevice;
        dummyResolutions = new LinkedList<>();
        dummyResolutions.add("1280x720");
        dummyResolutions.add("1920x1080");
    }

    @Test
    public void getResolutionIndex() throws Exception {
        setSelectedResolution(); //sets the 1280x720 resolution
        assertEquals(dDevice.getResolutionIndex(), 1);
    }

}