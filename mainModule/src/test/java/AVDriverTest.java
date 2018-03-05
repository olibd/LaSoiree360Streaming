import Devices.AVDevice;
import Exceptions.AVDeviceDoesNotExistException;
import Drivers.AVDriver;
import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedList;

import static org.junit.Assert.*;

/**
 * Created by olivier on 2017-01-16.
 */
public abstract class AVDriverTest {

    protected AVDriver driver;

    @Test
    public void getAudioDevices() throws Exception {
        LinkedList devices = driver.getAudioDevices();
        assertNotNull(devices);
    }

    @Test
    public void getVideoDevices() throws Exception {
        LinkedList devices = driver.getVideoDevices();
        assertNotNull(devices);
    }

    @Test
    public void selectAudioDevice() throws Exception {
        LinkedList<AVDevice> devices = driver.getAudioDevices();
        AVDevice validAudioDevice = devices.getFirst();
        AVDevice invalidAudioDevice = new AVDevice("invalid");

        //should throw an exception
        try{
            driver.selectAudioDevice(invalidAudioDevice);

        } catch (AVDeviceDoesNotExistException e){

        }

        //should not throw an exception
        driver.selectAudioDevice(validAudioDevice);
    }

    @Test
    public void getSelectedAudioDevice() throws Exception {
        selectAudioDevice();
        assertEquals(driver.getSelectedAudioDevice(), driver.getAudioDevices().getFirst());

    }

    @Test
    public void selectVideoDevice() throws Exception {
        LinkedList<AVDevice> devices = driver.getVideoDevices();
        AVDevice validVideoDevice = devices.getFirst();
        //selectVideoDevice should not accept any external devices
        AVDevice invalidVideoDevice = new AVDevice("invalid");

        //should throw an exception
        try{
            driver.selectVideoDevice(invalidVideoDevice);

        } catch (AVDeviceDoesNotExistException e){
            //we catch it so that it passes the test because this is the expected behavior
        }

        //should not throw an exception
        driver.selectVideoDevice(validVideoDevice);
    }

    @Test
    public void getSelectVideoDevice() throws Exception {
        selectVideoDevice();
        assertEquals(driver.getSelectedVideoDevice(), driver.getVideoDevices().getFirst());
    }

    @Test
    public void resetSelectedVideoDevice() throws Exception {
        driver.selectVideoDevice(driver.getVideoDevices().getFirst());
        assertNotNull(driver.getSelectedVideoDevice());
        driver.resetSelectedVideoDevice();
        assertNull(driver.getSelectedVideoDevice());
    }

    @Test
    public void resetSelectedAudioDevice() throws Exception {
        driver.selectAudioDevice(driver.getAudioDevices().getFirst());
        assertNotNull(driver.getSelectedAudioDevice());
        driver.resetSelectedAudioDevice();
        assertNull(driver.getSelectedAudioDevice());
    }

    @Test
    public abstract void getInputFormat() throws Exception;

    @Test
    public void updateAVdevices() throws Exception {
        HashMap<String, LinkedList<AVDevice>> avDevices = driver.updateAVdevices();
        assertNotNull(avDevices);
        assertNotNull(avDevices.get("video"));
        assertNotNull(avDevices.get("audio"));
    }
}

