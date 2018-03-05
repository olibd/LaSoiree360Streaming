import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by olivier on 2017-02-18.
 *
 * This class is a singleton returning commonly used paths
 */
class PathFactory {

    private static PathFactory instance;
    private Computer.OSType os;

    private PathFactory(Computer.OSType osType){
        os = osType;
    }

    public static PathFactory getInstance() {
        if (instance == null) {
            instance = new PathFactory(Computer.getOSType());
        }
        return instance;
    }

    /**
     * @param objectClass
     * @return the path to the FFMPEG executable relative to the supplied class
     */
    public String getFFMPEGPath(Class objectClass) {

        if (os == Computer.OSType.MAC)
            return loadResource(objectClass, "ffmpeg/mac/ffmpeg");
        else if (os == Computer.OSType.WIN)
            return loadResource(objectClass, "ffmpeg\\win64\\bin\\ffmpeg.exe");

        return null;
    }

    /**
     * Will attempt to load the resource defined by the localPath relative to the given Class. Will return the
     * complete path to the resource relative to the given class
     * @param objectClass
     * @param localPath
     * @return
     */
    private String loadResource(Class objectClass, String localPath) {
        ClassLoader classLoader = objectClass.getClassLoader();
        try {
            return URLDecoder.decode(classLoader.getResource(localPath).getFile(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            //Throwing a runtime exception because UTF-8 is a supported charset according to the javadocs.
            // So it technically will never throw an exception unless the charset is changed to something
            // unsupported. Because of this it doesnt make sense to have the method rethrow a checked exception
            throw new RuntimeException(e);
        }
    }

    /**
     * @param objectClass
     * @return the path to the FFPROBE executable relative to the supplied class
     */
    public String getFFPROBEPath(Class objectClass) {
        ClassLoader classLoader = objectClass.getClassLoader();

        if (os == Computer.OSType.MAC)
            return loadResource(objectClass, "ffmpeg/mac/ffprobe");
        else if (os == Computer.OSType.WIN)
            return loadResource(objectClass, "ffmpeg\\win64\\bin\\ffprobe.exe");

        return null;
    }

}
