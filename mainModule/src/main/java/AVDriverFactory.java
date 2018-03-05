import java.io.IOException;

/**
 * Created by olivier on 16-12-16.
 */
public abstract class AVDriverFactory {

    protected static AVDriverFactory instance;

    /**
     * Init or get the existing instance of AVDriverFactory
     *
     * @param os
     * @return
     * @throws IOException
     */
    public static AVDriverFactory getOrInitInstance(Computer.OSType os) throws IOException {
        if (instance == null) {
            if (os == Computer.OSType.MAC)
                instance = new MacAVDriverFactory();
            else if (os == Computer.OSType.WIN)
                instance = new WinAVDriverFactory();
        }
        return instance;
    }

    /**
     * Returns the instance of the AVDriverFactory
     *
     * @return
     */
    public static AVDriverFactory getInstance() {
        return instance;
    }

    /**
     * Returns array of AVDrivers
     *
     * @return
     */
    public abstract AVDriver[] getAVDrivers();
}
