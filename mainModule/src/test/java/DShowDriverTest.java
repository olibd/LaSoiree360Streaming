import Devices.FFMPEGAVDevice;
import Drivers.DShowDriver;
import Drivers.FFMPEGAVDriver;
import Platform.Computer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Created by olivier on 2017-01-17.
 */
public class DShowDriverTest extends FFMPEGAVDriverTest {

    @Before
    public void setUp() throws IOException {
        org.junit.Assume.assumeTrue(Computer.getInstance().isWin());
        driver = new DShowDriver();
    }

    @Override
    public void getInputFormat() throws Exception {
        assertEquals(driver.getInputFormat(), "dshow");
    }

    @Test
    public void getDeviceFromOutputLine() throws Exception {
        String line = "[dshow @ 0000000001edeb20]  \"Microsoft Camera Front\"";
        FFMPEGAVDevice device = ((FFMPEGAVDriver)driver).getDeviceFromOutputLine(line);
        assertEquals(device.getName(), "Microsoft Camera Front");
    }

    @Test
    public void getFFMPEGInputCommand() throws Exception {
        driver.selectVideoDevice(driver.getVideoDevices().getFirst());
        driver.selectAudioDevice(driver.getAudioDevices().getFirst());

        assertTrue(driver.getFFMPEGInputCommand().matches("video=.*?:audio=.*?$"));

        driver.resetSelectedVideoDevice();
        assertTrue(driver.getFFMPEGInputCommand().matches("audio=.*?$"));

        driver.selectVideoDevice(driver.getVideoDevices().getFirst());
        driver.resetSelectedAudioDevice();
        assertTrue(driver.getFFMPEGInputCommand().matches("video=.*?$"));
    }

    @Override
    public void getFFMPEGVideoInputCommand() throws Exception {
        driver.selectVideoDevice(driver.getVideoDevices().getFirst());
        driver.selectAudioDevice(driver.getAudioDevices().getFirst());

        assertTrue(driver.getFFMPEGVideoInputCommand().matches("video=.*?$"));

        //test what happens if we remove the video device
        driver.resetSelectedVideoDevice();
        assertEquals(driver.getFFMPEGVideoInputCommand(), "");

    }

    @Override
    public void getFFMPEGAudioInputCommand() throws Exception {
        driver.selectVideoDevice(driver.getVideoDevices().getFirst());
        driver.selectAudioDevice(driver.getAudioDevices().getFirst());

        assertTrue(driver.getFFMPEGAudioInputCommand().matches("audio=.*?$"));

        //test what happens if we remove the video device
        driver.resetSelectedAudioDevice();
        assertEquals(driver.getFFMPEGAudioInputCommand(), "");
    }

}
