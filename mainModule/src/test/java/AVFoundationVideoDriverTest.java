import Devices.AVDevice;
import Devices.FFMPEGAVDevice;
import Exceptions.AVDeviceDoesNotExistException;
import Drivers.AVFoundationVideoDriver;
import Platform.Computer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by olivier on 2017-03-17.
 */
public class AVFoundationVideoDriverTest extends AVFoundationDriverTest {
    @Before
    public void setUp() throws IOException {
        org.junit.Assume.assumeTrue(Computer.getInstance().isMac());
        driver = new AVFoundationVideoDriver();

        LinkedList<AVDevice> videoDevices = driver.getVideoDevices();

        AVDevice dummyVideoDevice = new FFMPEGAVDevice("Facetime Camera");

        videoDevices.add(dummyVideoDevice);
    }

    @Test
    public void getAudioDevices() throws Exception {
        assertTrue(driver.getAudioDevices().size() == 0);
    }

    @Test
    public void selectAudioDevice() throws Exception {
        //selectVideoDevice should not accept any external devices
        AVDevice invalidAudioDevice = new AVDevice("invalid");

        //should throw an exception
        try{
            driver.selectAudioDevice(invalidAudioDevice);

        } catch (AVDeviceDoesNotExistException e){
            //we catch it so that it passes the test because this is the expected behavior
        }
    }

    @Test
    public void getSelectedAudioDevice() throws Exception {
        selectAudioDevice();
        assertNull(driver.getSelectedAudioDevice());

    }

    @Test
    public void resetSelectedAudioDevice() throws Exception {
        driver.resetSelectedAudioDevice();
        assertNull(driver.getSelectedAudioDevice());
    }

    @Override
    public void getFFMPEGAudioInputCommand() throws Exception {
        assertNull(driver.getFFMPEGAudioInputCommand());
    }

    @Override
    public void getFFMPEGVideoInputCommand() throws Exception {
        driver.selectVideoDevice(driver.getVideoDevices().getFirst());

        String cmd = driver.getFFMPEGVideoInputCommand();
        assertTrue(cmd.matches(".*?:"));

        driver.resetSelectedVideoDevice();
        cmd = driver.getFFMPEGVideoInputCommand();
        assertTrue(cmd.matches(""));
    }

    @Override
    public void getFFMPEGInputCommand() throws Exception {
        driver.selectVideoDevice(driver.getVideoDevices().getFirst());

        String cmd = driver.getFFMPEGVideoInputCommand();
        assertTrue(cmd.matches(".*?:"));

        driver.resetSelectedVideoDevice();
        cmd = driver.getFFMPEGVideoInputCommand();
        assertTrue(cmd.matches(""));
    }
}
