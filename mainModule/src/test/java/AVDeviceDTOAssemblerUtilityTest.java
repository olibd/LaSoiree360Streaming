import Devices.AVDevice;
import Devices.AVDeviceDTO;
import Devices.AVDeviceDTOAssemblerUtility;
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
public class AVDeviceDTOAssemblerUtilityTest {

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
    }

    private void configureDummyDriver() throws IOException {
        LinkedList<AVDevice> videoDevices = driver.getVideoDevices();
        LinkedList<AVDevice> audioDevices = driver.getAudioDevices();

        AVDevice dummyVideoDevice = new AVDevice("dummy Camera");
        AVDevice dummyAudioDevice = new AVDevice("Line Audio");

        videoDevices.add(dummyVideoDevice);
        audioDevices.add(dummyAudioDevice);
    }

    @Test
    public void assemble() throws Exception {
        AVDeviceDTO dto = AVDeviceDTOAssemblerUtility.assemble(driver, device);

        assertEquals(dto.getDriver(), driver);
        assertEquals(dto.getDevice(), device);
    }

    @Test
    public void assembleFromList() throws Exception {
        LinkedList<AVDeviceDTO> dtos = AVDeviceDTOAssemblerUtility.assembleFromList(driver, driver.getVideoDevices());

        //the number of DTO created should be the same as the number of devices in the supplied list
        assertEquals(dtos.size(), driver.getVideoDevices().size());

        for (int i = 0; i < dtos.size(); i++) {
            assertEquals(dtos.get(i).getDevice(), driver.getVideoDevices().get(i));
            assertEquals(dtos.get(i).getDriver(), driver);
        }
    }

}