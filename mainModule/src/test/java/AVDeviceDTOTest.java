import Devices.AVDevice;
import Devices.AVDeviceDTO;
import Drivers.AVDriver;
import Drivers.AVFoundationDriver;
import Drivers.DShowDriver;
import Exceptions.OSNotSupportedError;
import Platform.Computer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.LinkedList;

import static org.junit.Assert.*;

/**
 * Created by olivier on 2017-01-17.
 */
public class AVDeviceDTOTest {

    AVDeviceDTO dto;
    AVDriver driver;
    AVDevice device;

    @Before
    public void setUp() throws Exception, OSNotSupportedError {

        if(Computer.getInstance().isMac()){
            driver = new AVFoundationDriver();
            configureDummyDriver();
        } else if(Computer.getInstance().isWin()){
            driver = new DShowDriver();
            configureDummyDriver();
        } else
            fail();

        device = driver.getVideoDevices().getFirst();
        dto = new AVDeviceDTO(driver, device);
    }

    @Test
    public void getDriver() throws Exception {
        assertEquals(dto.getDriver(), driver);
    }

    @Test
    public void getDevice() throws Exception {
        assertEquals(dto.getDevice(), device);
    }

    @Test
    public void testToString() throws Exception {
        assertEquals(dto.toString(), device.toString());
    }

    private void configureDummyDriver() throws IOException {
        LinkedList<AVDevice> videoDevices = driver.getVideoDevices();
        LinkedList<AVDevice> audioDevices = driver.getAudioDevices();

        AVDevice dummyVideoDevice = new AVDevice("dummy Camera");
        AVDevice dummyAudioDevice = new AVDevice("Line Audio");

        videoDevices.add(dummyVideoDevice);
        audioDevices.add(dummyAudioDevice);
    }

}