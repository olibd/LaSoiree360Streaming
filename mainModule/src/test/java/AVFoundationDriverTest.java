import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.LinkedList;

import static org.junit.Assert.*;

/**
 * Created by olivier on 2017-01-15.
 */
public class AVFoundationDriverTest extends FFMPEGAVDriverTest{

    @Before
    public void setUp() throws IOException {
        org.junit.Assume.assumeTrue(Computer.getInstance().isMac());
        driver = new AVFoundationDriver();

        LinkedList<AVDevice> videoDevices = driver.getVideoDevices();
        LinkedList<AVDevice> audioDevices = driver.getAudioDevices();

        AVDevice dummyVideoDevice = new FFMPEGAVDevice("Facetime Camera");
        AVDevice dummyAudioDevice = new FFMPEGAVDevice("Line Audio");

        videoDevices.add(dummyVideoDevice);
        audioDevices.add(dummyAudioDevice);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void getAudioDevices() throws Exception {
        super.getAudioDevices();
        assertTrue(driver.getAudioDevices().size() >= 1);
    }

    @Test
    public void getVideoDevices() throws Exception {
        super.getVideoDevices();
        assertTrue(driver.getVideoDevices().size() >= 1);
    }

    @Test
    @Override
    public void getInputFormat() throws Exception {
        assertEquals(driver.getInputFormat(), "AVFoundation");
    }

    @Test
    public void getDeviceFromOutputLine() throws Exception {
        String line = "[AVFoundation input device @ 0x7fa0fa40eae0] [1] dummy device";
        FFMPEGAVDevice device = ((FFMPEGAVDriver)driver).getDeviceFromOutputLine(line);
        assertEquals(device.getName(), "dummy device");
    }

    @Test
    public void getFFMPEGInputCommand() throws Exception {
        driver.selectVideoDevice(driver.getVideoDevices().getFirst());
        driver.selectAudioDevice(driver.getAudioDevices().getFirst());

        String cmd = driver.getFFMPEGInputCommand();
        assertTrue(cmd.matches(".*?:.*?$"));

        driver.resetSelectedVideoDevice();
        driver.resetSelectedAudioDevice();
        cmd = driver.getFFMPEGInputCommand();
        assertTrue(cmd.matches("none:none"));

    }

    @Override
    public void getFFMPEGVideoInputCommand() throws Exception {
        driver.selectVideoDevice(driver.getVideoDevices().getFirst());
        driver.selectAudioDevice(driver.getAudioDevices().getFirst());

        String cmd = driver.getFFMPEGVideoInputCommand();
        assertTrue(cmd.matches(".*?:"));

        driver.resetSelectedVideoDevice();
        cmd = driver.getFFMPEGVideoInputCommand();
        assertTrue(cmd.matches(""));
    }

    @Override
    public void getFFMPEGAudioInputCommand() throws Exception {
        driver.selectVideoDevice(driver.getVideoDevices().getFirst());
        driver.selectAudioDevice(driver.getAudioDevices().getFirst());

        String cmd = driver.getFFMPEGAudioInputCommand();
        assertTrue(cmd.matches(":.*?$"));

        driver.resetSelectedAudioDevice();
        cmd = driver.getFFMPEGAudioInputCommand();
        assertTrue(cmd.matches(""));
    }
}
