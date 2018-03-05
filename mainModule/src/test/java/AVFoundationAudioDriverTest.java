import Exceptions.AVDeviceDoesNotExistException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.LinkedList;

import static org.junit.Assert.*;

/**
 * Created by olivier on 2017-03-17.
 */
public class AVFoundationAudioDriverTest extends AVFoundationDriverTest{
    @Before
    public void setUp() throws IOException {
        org.junit.Assume.assumeTrue(Computer.getInstance().isMac());
        driver = new AVFoundationAudioDriver();

        LinkedList<AVDevice> audioDevices = driver.getAudioDevices();

        AVDevice dummyAudioDevice = new AVFoundationAudioDevice("Line Audio");

        audioDevices.add(dummyAudioDevice);
    }

    @Test
    public void getVideoDevices() throws Exception {
        assertTrue(driver.getVideoDevices().size() == 0);
    }

    @Test
    public void selectVideoDevice() throws Exception {
        //selectVideoDevice should not accept any external devices
        AVDevice invalidVideoDevice = new AVDevice("invalid");

        //should throw an exception
        try{
            driver.selectVideoDevice(invalidVideoDevice);

        } catch (AVDeviceDoesNotExistException e){
            //we catch it so that it passes the test because this is the expected behavior
        }
    }

    @Test
    public void getSelectVideoDevice() throws Exception {
        selectVideoDevice();
        assertNull(driver.getSelectedVideoDevice());
    }

    @Test
    public void resetSelectedVideoDevice() throws Exception {
        driver.resetSelectedVideoDevice();
        assertNull(driver.getSelectedVideoDevice());
    }

    @Test
    @Override
    public void getInputFormat() throws Exception {
        assertNull(driver.getInputFormat());
    }

    @Override
    public void getFFMPEGVideoInputCommand() throws Exception {
        assertNull(driver.getFFMPEGVideoInputCommand());
    }

    @Override
    public void getFFMPEGAudioInputCommand() throws Exception {
        driver.selectAudioDevice(driver.getAudioDevices().getFirst());

        String cmd = driver.getFFMPEGAudioInputCommand();
        assertTrue(cmd.matches("udp://127.0.0.1:\\d*"));
    }

    @Override
    public void getFFMPEGInputCommand() throws Exception {
        driver.selectAudioDevice(driver.getAudioDevices().getFirst());

        String cmd = driver.getFFMPEGInputCommand();
        assertTrue(cmd.matches("udp://127.0.0.1:\\d*"));
    }
}