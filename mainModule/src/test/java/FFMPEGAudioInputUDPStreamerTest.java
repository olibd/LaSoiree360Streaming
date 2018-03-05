import Exceptions.OSNotSupportedError;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by olivier on 2017-03-20.
 */
public class FFMPEGAudioInputUDPStreamerTest {
    private FFMPEGAudioInputUDPStreamer preprocessor;

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

        FFMPEGAVDevice dummyAVDevice = new FFMPEGAVDevice("Built-in Microphone");
        preprocessor = new FFMPEGAudioInputUDPStreamer("AVFoundation", dummyAVDevice);
    }

    @Test
    public void activateDeactivate() throws InterruptedException {
        assertFalse(preprocessor.isActive());

        preprocessor.activate();
        assertTrue(preprocessor.isActive());

        preprocessor.deactivate();
        Thread.sleep(1000); //wait for the process to stop
        assertFalse(preprocessor.isActive());
    }

    @Test
    public void getDestination() throws Exception {
        assertTrue(preprocessor.getDestination().matches("udp://127.0.0.1:\\d*"));
    }

}