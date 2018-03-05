import Exceptions.OSNotSupportedError;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by olivier on 2017-03-20.
 */
public class AVFoundationAudioDeviceTest extends AVDeviceTest {

    AVFoundationAudioDevice aDevice;

    @Before
    public void setUp() throws Exception {
        try {
            org.junit.Assume.assumeTrue(Computer.getInstance().isMac());


            //ignore if travis-ci
            try{
                if(System.getenv("TRAVIS").equals("true"))
                    org.junit.Assume.assumeTrue(false);
            }catch (NullPointerException e){
                //if fail, we run the tests
            }

        } catch (OSNotSupportedError e) {
            fail();
        }
        aDevice = new AVFoundationAudioDevice("dummyDevice");
        device = aDevice;
    }

    @Test
    public void activateDeactivate() throws InterruptedException {
        assertFalse(aDevice.isActive());

        aDevice.activate();
        assertTrue(aDevice.isActive());

        aDevice.deactivate();
        Thread.sleep(1000); //wait for the process to stop
        assertFalse(aDevice.isActive());
    }

    @Test
    public void testToString() {

        assertNull(aDevice.toString());

        aDevice.activate();

        assertTrue(aDevice.toString().matches("udp://127.0.0.1:\\d*"));

        aDevice.deactivate();

        assertNull(aDevice.toString());
    }


    @Ignore
    @Test
    public void setSelectedResolution() throws Exception {
    }

    @Ignore
    @Test
    public void getSelectedResolution() throws Exception {
    }

}