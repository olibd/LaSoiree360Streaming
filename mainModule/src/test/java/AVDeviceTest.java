import Exceptions.ResolutionNotSupportedException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.*;

/**
 * Created by olivier on 2017-01-17.
 */
public class AVDeviceTest {

    AVDevice device;
    LinkedList<String> dummyResolutions;

    @Before
    public void setUp() throws Exception {
        device = new AVDevice("dummyDevice");
        dummyResolutions = new LinkedList<>();
        dummyResolutions.add("1280x720");
        dummyResolutions.add("1920x1080");
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void setResolutions() throws Exception {
        device.setResolutions(dummyResolutions);
    }

    @Test
    public void getResolutions() throws Exception {
        setResolutions();
        assertEquals(device.getResolutions(), dummyResolutions);
    }

    @Test
    public void setSelectedResolution() throws Exception {
        setResolutions();
        device.setSelectedResolution("1280x720");
    }

    @Test
    public void setSelectedResolutionOnDeviceWithNoResolutions() throws Exception {
        AVDevice noResolutionDevice = new AVDevice("noResolutionDevice");

        try{
            noResolutionDevice.setSelectedResolution("1280x720");
            fail(); //if no exception is thrown, then the test should fail
        }catch (ResolutionNotSupportedException e){
            //the expected behavior of throwing an exception is reached. So the test passes
        }
    }

    @Test
    public void getSelectedResolution() throws Exception {
        setSelectedResolution();
        assertEquals(device.getSelectedResolution(), "1280x720");
    }

    @Test
    public void getName() throws Exception {
        assertEquals(device.getName(), "dummyDevice");
    }

    @Test
    public void testToString() throws Exception {
        assertEquals(device.toString(), "dummyDevice");
    }

}