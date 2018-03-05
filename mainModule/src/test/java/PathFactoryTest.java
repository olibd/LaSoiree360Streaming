import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

/**
 * Created by olivier on 2017-03-07.
 */
public class PathFactoryTest {

    private PathFactory factory;

    @Before
    public void setUp() throws Exception{
        factory = PathFactory.getInstance();
        assertNotNull(factory);
    }

    @Test
    public void getFFMPEGPath() throws Exception {
        File f = new File(factory.getFFMPEGPath(this.getClass()));
        assertTrue(f.isFile());
    }

    @Test
    public void getFFPROBEPath() throws Exception {
        File f = new File(factory.getFFPROBEPath(this.getClass()));
        assertTrue(f.isFile());
    }

}