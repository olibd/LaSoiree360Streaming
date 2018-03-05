import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * Created by olivier on 2017-03-17.
 */
public abstract class FFMPEGAVDriverTest extends AVDriverTest {
    @Test
    public void startFFMPEGProcess() throws Exception {
        assertNotNull(((FFMPEGAVDriver)driver).startFFMPEGProcess());
    }

    @Test
    public abstract void getDeviceFromOutputLine() throws Exception;

    @Test
    public abstract void getFFMPEGInputCommand() throws Exception;

    @Test
    public abstract void getFFMPEGVideoInputCommand() throws Exception;

    @Test
    public abstract void getFFMPEGAudioInputCommand() throws Exception;
}
