import Exceptions.OSNotSupportedError;
import org.apache.commons.lang3.reflect.*;

import java.io.IOException;
import java.util.LinkedList;

import static org.junit.Assert.*;

/**
 * Created by olivier on 16-10-13.
 */
public class StreamManagerTest {

   private StreamManager sm;
    private String testPath = "testPath";
    private AVDriver driver;

    @org.junit.Before
    public void setUp() throws IOException, OSNotSupportedError {
        try {
            sm = new StreamManager();
        } catch (IOException e) {
            //ignore if travis-ci
            try{
                if(System.getenv("TRAVIS").equals("true"))
                    org.junit.Assume.assumeTrue(false);
            }catch (NullPointerException e2){
                throw e;
            }
        }
        configureDummyDriver();
    }

    @org.junit.After
    public void tearDown() throws Exception {

    }

    @org.junit.Test
    public void prepareStream() throws Exception {

        AVDevice firstVideoDevice = driver.getVideoDevices().getFirst();
        AVDevice firstAudioDevice = driver.getAudioDevices().getFirst();

        //set the selected devices
        driver.selectVideoDevice(firstVideoDevice);
        driver.selectAudioDevice(firstAudioDevice);

        String output = "/tmp/Soireeoutput.mp4";

        sm.prepareStream(driver, driver, output);

        assertEquals(sm.getInputVideoDriver(), driver);
        assertEquals(sm.getInputAudioDriver(), driver);
        assertEquals(sm.getOutputPath(), output);

        //Use reflection to check if the outputCommandBuilder has been set
        Object cmdBuilder = FieldUtils.readDeclaredField(sm, "outputCommandBuilder", true);
        assertTrue(cmdBuilder != null);

    }

    @org.junit.Test
    public void setOutputPreset() throws Exception {
        prepareStream();

        sm.setOutputPreset(new YoutubePreset());

        //Use reflection to see if the field ffmpegBuilder has been set
        Object ffBuilder = FieldUtils.readDeclaredField(sm, "ffmpegBuilder", true);
        assertTrue(ffBuilder != null);

    }

    @org.junit.Test
    public void setVideoInputDriver() throws Exception {
        AVDriver previousVideoDriver = (AVDriver)FieldUtils.readDeclaredField(sm, "inputVideoDriver", true);
        assertNull(previousVideoDriver);

        sm.setInputVideoDriver(driver);

        AVDriver newVideoDriver = (AVDriver)FieldUtils.readDeclaredField(sm, "inputVideoDriver", true);
        assertEquals(newVideoDriver, driver);
    }

    @org.junit.Test
    public void getVideoInputPath() throws Exception {
        setVideoInputDriver();
        assertEquals(sm.getInputVideoDriver(), driver);
    }

    @org.junit.Test
    public void setAudioInputPath() throws Exception {
        AVDriver previousAudioDriver = (AVDriver)FieldUtils.readDeclaredField(sm, "inputAudioDriver", true);
        assertNull(previousAudioDriver);

        sm.setInputAudioDriver(driver);

        AVDriver newAudioDriver = (AVDriver)FieldUtils.readDeclaredField(sm, "inputAudioDriver", true);
        assertEquals(newAudioDriver, driver);
    }

    @org.junit.Test
    public void getAudioInputPath() throws Exception {
        setAudioInputPath();
        assertEquals(sm.getInputAudioDriver(),driver);
    }

    @org.junit.Test
    public void setOutputPath() throws Exception {
        String previousOutputPath = (String)FieldUtils.readDeclaredField(sm, "output", true);
        assertNotEquals(previousOutputPath, testPath);

        sm.setOutputPath(testPath);

        String newOutputPath = (String)FieldUtils.readDeclaredField(sm, "output", true);
        assertEquals(newOutputPath, testPath);
    }

    @org.junit.Test
    public void getOutputPath() throws Exception {
        setOutputPath();
        assertEquals(sm.getOutputPath(), testPath);
    }

    private void configureDummyDriver() throws IOException {
        driver = new AVFoundationDriver();
        LinkedList<AVDevice> videoDevices = driver.getVideoDevices();
        LinkedList<AVDevice> audioDevices = driver.getAudioDevices();

        AVDevice dummyVideoDevice = new AVDevice("dummy Camera");
        AVDevice dummyAudioDevice = new AVDevice("Line Audio");

        videoDevices.add(dummyVideoDevice);
        audioDevices.add(dummyAudioDevice);
    }
}