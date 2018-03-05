import Devices.AVDevice;
import Devices.DeckLinkDevice;
import Devices.FFMPEGAVDevice;
import Drivers.DeckLinkDriver;
import Drivers.FFMPEGAVDriver;
import Platform.Computer;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.LinkedList;

import static org.junit.Assert.*;

/**
 * Created by olivier on 2017-01-16.
 */
public class DeckLinkDriverTest extends FFMPEGAVDriverTest{

    @Before
    public void setUp() throws IOException, IllegalAccessException {
        org.junit.Assume.assumeTrue(Computer.getInstance().isMac());
        driver = new DeckLinkDriver();
        LinkedList<AVDevice> videoDevices = driver.getVideoDevices();

        //Create a dummy decklink device with dummy resolution and add it to the driver in order to test it without the
        //need for the actual device to by plugged into the computer
        DeckLinkDevice dummyDevice = new DeckLinkDevice("UltraStudio Mini Recorder");
        LinkedList<String> dummyResolutions = new LinkedList<>();
        dummyResolutions.add("1280x720");
        dummyResolutions.add("1920x1080");
        dummyDevice.setResolutions(dummyResolutions);

        videoDevices.add(dummyDevice);
    }

    @Test
    public void getDeviceFromOutputLine() throws Exception {
        FFMPEGAVDevice device = ((FFMPEGAVDriver)driver).getDeviceFromOutputLine("[decklink @ 0x7fe3b0800000] \t'UltraStudio Mini Recorder'");
        assertEquals(device.getName(), "UltraStudio Mini Recorder");
    }

    @Test
    public void getFFMPEGInputCommand() throws Exception {
        driver.getVideoDevices().getFirst().setSelectedResolution("1920x1080");
        driver.selectVideoDevice(driver.getVideoDevices().getFirst());
        String cmd = driver.getFFMPEGInputCommand();
        assertTrue(cmd.matches("UltraStudio Mini Recorder@\\d+"));
    }

    @Override
    public void getFFMPEGVideoInputCommand() throws Exception {
        driver.getVideoDevices().getFirst().setSelectedResolution("1920x1080");
        driver.selectVideoDevice(driver.getVideoDevices().getFirst());
        String cmd = driver.getFFMPEGVideoInputCommand();
        assertTrue(cmd.matches("UltraStudio Mini Recorder@\\d+"));
    }

    @Override
    public void getFFMPEGAudioInputCommand() throws Exception {
        driver.getVideoDevices().getFirst().setSelectedResolution("1920x1080");
        driver.selectVideoDevice(driver.getVideoDevices().getFirst());
        String cmd = driver.getFFMPEGAudioInputCommand();
        assertNull(cmd);
    }

    @Ignore
    @Test
    public void getAudioDevices() throws Exception {
        //Ignored because this driver has no audio devices
    }

    @Test
    public void getVideoDevices() throws Exception {
        super.getVideoDevices();
        assertTrue(driver.getVideoDevices().size() >= 1);
    }

    @Ignore
    @Test
    @Override
    public void selectAudioDevice() throws Exception {
        //Ignored because this driver has no audio devices
    }

    @Ignore
    @Test
    @Override
    public void getSelectedAudioDevice() throws Exception {
        //Ignored because this driver has no audio devices
    }

    @Ignore
    @Test
    @Override
    public void resetSelectedAudioDevice() throws Exception {
        //Ignored because this driver has no audio devices
    }

    @Test
    public void getInputFormat() throws Exception {
        assertEquals(driver.getInputFormat(), "decklink");
    }

}
